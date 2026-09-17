package com.ga.bank.repository;

import com.ga.bank.model.account.Account;
import com.ga.bank.model.account.AccountStatus;
import com.ga.bank.model.account.ChekingAccount;
import com.ga.bank.model.account.SavingAccount;
import com.ga.bank.model.card.CardType;
import com.ga.bank.model.card.DebitCard;
import com.ga.bank.util.FileStorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileAccountRepository implements AccountRepository {

    private final String baseDir;

    public FileAccountRepository() {
        this("data/accounts");
    }

    public FileAccountRepository(String baseDir) {
        this.baseDir = baseDir;
        FileStorageUtils.ensureDirectoryExists(baseDir);
    }

    @Override
    public void save(Account account) {
        if (account == null || account.getAccountid() == null) {
            throw new IllegalArgumentException("Cannot save null account or account without account ID.");
        }
        writeAccountToFile(account);
    }

    @Override
    public void update(Account account) {
        if (account == null || account.getAccountid() == null) {
            throw new IllegalArgumentException("Cannot update null account.");
        }
        writeAccountToFile(account);
    }

    @Override
    public Optional<Account> findById(String accountId) {
        if (accountId == null) {
            return Optional.empty();
        }
        String fullPath = baseDir + "/" + accountId + ".txt";
        return Optional.ofNullable(parseAccountFile(fullPath));
    }

    @Override
    public Optional<Account> findByCardNumber(String cardNumber) {
        if (cardNumber == null) {
            return Optional.empty();
        }
        return findAll().stream()
                .filter(acc -> acc.getDebtcard() != null
                        && cardNumber.equalsIgnoreCase(acc.getDebtcard().getCardNumber()))
                .findFirst();
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        File folder = new File(baseDir);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null) {
            return accounts;
        }

        for (File file : files) {
            Account acc = parseAccountFile(file.getAbsolutePath());
            if (acc != null) {
                accounts.add(acc);
            }
        }
        return accounts;
    }

    private String formatAccountToLine(Account account) {
        // this is to determine if the account is pointing to a saving or chekcing account

        String accountType = (account instanceof SavingAccount) ? "SAVINGS" : "CHECKING";
// a card varbale will be used to hold the debtcard which will contain the vard id and the cardtype

        // or in other words it Extract card metadata if present
        DebitCard card = account.getDebtcard();

        String cardNumber = (card != null && card.getCardNumber() != null) ? card.getCardNumber() : "NULL";
        // this is to get the cardtype if it exist .name() is used to get the enum and convert it to string

        String cardType = (card != null && card.getCardType() != null) ? card.getCardType().name() : "NULL";

        // Extract overdraft count if checking account
        // this will check if the overdraft was made or not

        int overdraftCount = (account instanceof ChekingAccount checking) ? checking.getOverdraftcount() : 0;

        // Line format: ACCOUNT_ID|BALANCE|STATUS|TYPE|CARD_NUMBER|CARD_TYPE|OVERDRAFT_COUNT
        return String.join("|",
                account.getAccountid(),
                String.valueOf(account.getBalence()),
                // this is used to get status of the account if its null it be active
                //if it is not null it will take the account status
                account.getStatus() != null ? account.getStatus().name() : AccountStatus.ACTIVE.name(),
                accountType,
                cardNumber,
                cardType,
                String.valueOf(overdraftCount)
        );
    }

    // this methods is to from account to the txt file
    // formate the account to line to the line is passed to the wrilesline which will be take and written to the file path location
    // and if its not exist its created
    private void writeAccountToFile(Account account) {
        String fullPath = baseDir + "/" + account.getAccountid() + ".txt";
        String line = formatAccountToLine(account);
        FileStorageUtils.writeLines(fullPath, List.of(line));
    }

    private Account parseAccountFile(String fullPath) {
        try {
            List<String> lines = FileStorageUtils.readLines(fullPath);
            if (lines.isEmpty()) {
                return null;
            }

            String[] parts = lines.get(0).split("\\|");
            if (parts.length < 4) {
                return null;
            }

            String accountId = parts[0];
            double balance = Double.parseDouble(parts[1]);
            AccountStatus status = AccountStatus.valueOf(parts[2]);
            String type = parts[3];

            // Reconstruct DebitCard if present in file
            DebitCard debitCard = null;
            if (parts.length >= 6 && !"NULL".equalsIgnoreCase(parts[4])) {
                String cardNum = parts[4];
                CardType cardType = !"NULL".equalsIgnoreCase(parts[5]) ? CardType.valueOf(parts[5]) : null;
                debitCard = new DebitCard(cardNum, cardType);
            }

            // Reconstruct overdraft counter if present
            int overdraftCount = 0;
            if (parts.length >= 7) {
                overdraftCount = Integer.parseInt(parts[6]);
            }

            Account account;
            if ("SAVINGS".equalsIgnoreCase(type)) {
                account = new SavingAccount(accountId, balance, debitCard, status);
            } else {
                account = new ChekingAccount(accountId, balance, debitCard, status, overdraftCount);
            }

            return account;
        } catch (Exception e) {
            return null;
        }
    }
}