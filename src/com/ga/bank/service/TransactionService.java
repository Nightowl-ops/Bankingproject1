package com.ga.bank.service;

import com.ga.bank.model.account.Account;
import com.ga.bank.model.account.AccountStatus;
import com.ga.bank.model.account.ChekingAccount;
import com.ga.bank.model.card.CardType;
import com.ga.bank.model.card.DebitCard;
import com.ga.bank.model.transaction.Transaction;
import com.ga.bank.model.transaction.TransactionType;
import com.ga.bank.repository.AccountRepository;
import com.ga.bank.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction withdraw(String accountId, double amount) {
        Account account = getAccountOrThrow(accountId);

        // 1. Enforce Card Daily Limit based on Mastercard Tier
        DebitCard card = account.getDebtcard();
        if (card != null && card.getCardType() != null) {
            double dailyWithdrawn = getDailyTotal(accountId, TransactionType.WITHDRAW);
            double dailyLimit = getWithdrawDailyLimit(card.getCardType());

            if (dailyWithdrawn + amount > dailyLimit) {
                throw new IllegalStateException("Daily withdrawal ceiling reached for " + card.getCardType()
                        + ". Max allowed: $" + dailyLimit + ", Already used today: $" + dailyWithdrawn);
            }
        }

        // 2. Perform withdraw on domain model
        account.withdraw(amount);
        accountRepository.update(account);

        // 3. Write immutable record with post-transaction balance
        Transaction tx = new Transaction(
                account.getAccountid(),
                null,
                TransactionType.WITHDRAW,
                amount,
                account.getBalence()
        );
        transactionRepository.save(tx);
        return tx;
    }

    public Transaction deposit(String accountId, double amount, boolean isOwnAccount) {
        Account account = getAccountOrThrow(accountId);

        // 1. Enforce Card Daily Limit based on Mastercard Tier
        DebitCard card = account.getDebtcard();
        if (card != null && card.getCardType() != null) {
            double dailyDeposited = getDailyTotal(accountId, TransactionType.DEPOSITE);
            double limit = getDepositDailyLimit(card.getCardType(), isOwnAccount);

            if (dailyDeposited + amount > limit) {
                throw new IllegalStateException("Daily deposit limit reached ($" + limit + "). Already deposited today: $" + dailyDeposited);
            }
        }

        // 2. Perform deposit on domain model
        account.deposite(amount);

        // If checking account was deactivated, check if the deposit settled the negative balance
        if (account instanceof ChekingAccount checking && account.getStatus() == AccountStatus.DEACTIVATED) {
            if (checking.reactivate()) {
                System.out.println("Notice: Negative balance resolved. Account has been reactivated.");
            }
        }

        accountRepository.update(account);

        // 3. Write immutable record with post-transaction balance
        Transaction tx = new Transaction(
                null,
                account.getAccountid(),
                TransactionType.DEPOSITE,
                amount,
                account.getBalence()
        );
        transactionRepository.save(tx);
        return tx;
    }

    public Transaction transfer(String sourceAccountId, String targetAccountId, double amount, boolean isOwnAccount) {
        if (sourceAccountId.equalsIgnoreCase(targetAccountId)) {
            throw new IllegalArgumentException("Source and target accounts must be different.");
        }

        Account source = getAccountOrThrow(sourceAccountId);
        Account target = getAccountOrThrow(targetAccountId);

        // Select exact TransactionType based on own vs other account
        TransactionType transferType = isOwnAccount ? TransactionType.TRAHSFER_OWN : TransactionType.TRANSFER_OTHER;

        DebitCard card = source.getDebtcard();
        if (card != null && card.getCardType() != null) {
            double dailyTransferred = getDailyTotal(sourceAccountId, transferType);
            double transferLimit = getTransferDailyLimit(card.getCardType(), isOwnAccount);

            if (dailyTransferred + amount > transferLimit) {
                throw new IllegalStateException("Daily transfer limit breached for " + card.getCardType()
                        + ". Max allowed: $" + transferLimit + ", Already transferred today: $" + dailyTransferred);
            }
        }

        source.withdraw(amount);
        target.deposite(amount);

        // If receiving account was a deactivated checking account, check reactivation
        if (target instanceof ChekingAccount checking && target.getStatus() == AccountStatus.DEACTIVATED) {
            if (checking.reactivate()) {
                System.out.println("Notice: Negative balance resolved. Target account has been reactivated.");
            }
        }

        accountRepository.update(source);
        accountRepository.update(target);

        // Records post-transaction balance of source account
        Transaction tx = new Transaction(
                source.getAccountid(),
                target.getAccountid(),
                transferType,
                amount,
                source.getBalence()
        );
        transactionRepository.save(tx);
        return tx;
    }

    public double getDailyTotal(String accountId, TransactionType type) {
        List<Transaction> todaysTx = transactionRepository.findByAccountIdAndDate(accountId, LocalDate.now());
        return todaysTx.stream()
                .filter(tx -> tx.getType() == type)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public List<Transaction> getFilteredTransactions(String accountId, String filter) {
        List<Transaction> all = transactionRepository.findByAccountId(accountId);
        LocalDate today = LocalDate.now();

        return switch (filter.toLowerCase()) {
            case "today" -> all.stream()
                    .filter(t -> t.getTimestamp().toLocalDate().isEqual(today))
                    .collect(Collectors.toList());
            case "yesterday" -> all.stream()
                    .filter(t -> t.getTimestamp().toLocalDate().isEqual(today.minusDays(1)))
                    .collect(Collectors.toList());
            case "last_7_days" -> all.stream()
                    .filter(t -> !t.getTimestamp().toLocalDate().isBefore(today.minusDays(7)))
                    .collect(Collectors.toList());
            case "last_30_days" -> all.stream()
                    .filter(t -> !t.getTimestamp().toLocalDate().isBefore(today.minusDays(30)))
                    .collect(Collectors.toList());
            default -> all;
        };
    }

    private Account getAccountOrThrow(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
    }

    private double getWithdrawDailyLimit(CardType type) {
        if (type == null) {
            return 0.0;
        }
        return type.getDailywithdrawlimit();
    }

    private double getTransferDailyLimit(CardType type, boolean isOwnAccount) {
        if (type == null) {
            return 0.0;
        }
        return isOwnAccount
                ? type.getDailytransferownlimit()
                : type.getDailytransferlimit();
    }

    private double getDepositDailyLimit(CardType type, boolean isOwnAccount) {
        if (type == null) {
            return 0.0;
        }
        return isOwnAccount
                ? type.getDailydepositownlimit()
                : type.getDailydepositlimit();
    }
}