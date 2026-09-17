package com.ga;

import com.ga.bank.model.User.Banker;
import com.ga.bank.model.User.Customer;
import com.ga.bank.model.User.Role;
import com.ga.bank.model.User.User;
import com.ga.bank.model.account.Account;
import com.ga.bank.model.account.ChekingAccount;
import com.ga.bank.model.account.SavingAccount;
import com.ga.bank.model.card.CardType;
import com.ga.bank.model.card.DebitCard;
import com.ga.bank.model.transaction.Transaction;
import com.ga.bank.repository.AccountRepository;
import com.ga.bank.repository.FileAccountRepository;
import com.ga.bank.repository.FileTransactionRepository;
import com.ga.bank.repository.FileUserRepository;
import com.ga.bank.repository.TransactionRepository;
import com.ga.bank.repository.UserRepository;
import com.ga.bank.service.AuthService;
import com.ga.bank.service.TransactionService;
import com.ga.bank.util.PasswordUtils;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UserRepository userRepo = new FileUserRepository();
        AccountRepository accountRepo = new FileAccountRepository();
        TransactionRepository txRepo = new FileTransactionRepository();

        AuthService authService = new AuthService(userRepo);
        TransactionService txService = new TransactionService(accountRepo, txRepo);

        bootstrapBankerIfEmpty(userRepo);

        System.out.println("=========================================");
        System.out.println("       WELCOME TO ACME BANK SYSTEM       ");
        System.out.println("=========================================");

        boolean running = true;
        while (running) {
            if (!authService.isAuthenticated()) {
                System.out.println("\n1. Login");
                System.out.println("2. Exit");
                System.out.print("Select an option: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> handleLogin(authService);
                    case "2" -> {
                        System.out.println("Exiting ACME Banking. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid selection. Try again.");
                }
            } else {
                User currentUser = authService.getCurrentUser();
                if (currentUser.getRole() == Role.BANKER) {
                    showBankerMenu((Banker) currentUser, userRepo, accountRepo, authService);
                } else {
                    showCustomerMenu((Customer) currentUser, accountRepo, txService, authService);
                }
            }
        }
    }

    private static void handleLogin(AuthService authService) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        try {
            User user = authService.login(username, password);
            System.out.println("\n>>> Login successful! Welcome " + user.getName() + " [" + user.getRole() + "]");
        } catch (Exception e) {
            System.out.println("\n[!] Error: " + e.getMessage());
        }
    }

    private static void showBankerMenu(Banker banker, UserRepository userRepo, AccountRepository accountRepo, AuthService authService) {
        System.out.println("\n--- BANKER PORTAL (" + banker.getName() + ") ---");
        System.out.println("1. Create New Customer & Accounts");
        System.out.println("2. View All Bank Accounts");
        System.out.println("3. Logout");
        System.out.print("Enter command: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> createCustomerFlow(userRepo, accountRepo);
            case "2" -> {
                List<Account> accounts = accountRepo.findAll();
                System.out.println("\n--- Total Registered Accounts: " + accounts.size() + " ---");
                for (Account acc : accounts) {
                    System.out.println(acc);
                }
            }
            case "3" -> {
                authService.logout();
                System.out.println("Logged out successfully.");
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void createCustomerFlow(UserRepository userRepo, AccountRepository accountRepo) {
        System.out.println("\n--- REGISTER NEW CUSTOMER ---");
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Customer ID (e.g. C-101): ");
        String id = scanner.nextLine().trim();
        System.out.print("Set Account Password: ");
        String password = scanner.nextLine().trim();

        String encryptedHash = PasswordUtils.hashPassword(password);
        Customer customer = new Customer(id, name, encryptedHash);
        userRepo.save(customer);

        System.out.println("Customer record created.");
        System.out.print("Account Type to open (1: Checking, 2: Savings, 3: Both): ");
        String typeChoice = scanner.nextLine().trim();

        CardType cardType = selectCardType();
        DebitCard card = new DebitCard("CARD-" + (System.currentTimeMillis() % 1000000), cardType);

        if ("1".equals(typeChoice) || "3".equals(typeChoice)) {
            System.out.print("Initial deposit for Checking account: ");
            double chkDeposit = Double.parseDouble(scanner.nextLine().trim());
            Account chk = new ChekingAccount("CHK-" + id, chkDeposit, card);
            accountRepo.save(chk);
            System.out.println("Opened Checking Account: CHK-" + id + " with linked " + cardType);
        }

        if ("2".equals(typeChoice) || "3".equals(typeChoice)) {
            System.out.print("Initial deposit for Savings account: ");
            double savDeposit = Double.parseDouble(scanner.nextLine().trim());
            Account sav = new SavingAccount("SAV-" + id, savDeposit, card);
            accountRepo.save(sav);
            System.out.println("Opened Savings Account: SAV-" + id + " with linked " + cardType);
        }
    }

    private static CardType selectCardType() {
        System.out.println("Select Mastercard Tier:");
        System.out.println("1. Mastercard Platinum");
        System.out.println("2. Mastercard Titanium");
        System.out.println("3. Mastercard Standard");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        return switch (choice) {
            case "1" -> CardType.MASTER_PLATINUM;
            case "2" -> CardType.MASTER_TITANIUM;
            default -> CardType.MASTER_STANDARD;
        };
    }

    private static void showCustomerMenu(Customer customer, AccountRepository accountRepo, TransactionService txService, AuthService authService) {
        System.out.println("\n--- CUSTOMER PORTAL (" + customer.getName() + ") ---");
        System.out.println("1. View My Accounts & Balances");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Transfer Money");
        System.out.println("5. Detailed Account Statement (All Transactions)");
        System.out.println("6. Filter Transactions");
        System.out.println("7. Logout");
        System.out.print("Enter command: ");
        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1" -> {
                    List<Account> accounts = accountRepo.findAll().stream()
                            .filter(a -> a.getAccountid().endsWith(customer.getId()))
                            .toList();
                    System.out.println("\n--- My Accounts ---");
                    for (Account a : accounts) {
                        System.out.println(a.getAccountid() + " | Type: " + a.getClass().getSimpleName()
                                + " | Balance: $" + a.getBalence() + " | Status: " + a.getStatus());
                    }
                }
                case "2" -> {
                    System.out.print("Enter Account ID: ");
                    String accId = scanner.nextLine().trim();
                    System.out.print("Enter Deposit Amount: ");
                    double amount = Double.parseDouble(scanner.nextLine().trim());
                    Transaction tx = txService.deposit(accId, amount, true);
                    System.out.println("Deposit successful! New Balance: $" + tx.getPostBalance());
                }
                case "3" -> {
                    System.out.print("Enter Account ID: ");
                    String accId = scanner.nextLine().trim();
                    System.out.print("Enter Withdrawal Amount: ");
                    double amount = Double.parseDouble(scanner.nextLine().trim());
                    Transaction tx = txService.withdraw(accId, amount);
                    System.out.println("Withdrawal successful! New Balance: $" + tx.getPostBalance());
                }
                case "4" -> {
                    System.out.print("Enter Source Account ID: ");
                    String srcId = scanner.nextLine().trim();
                    System.out.print("Enter Target Account ID: ");
                    String tgtId = scanner.nextLine().trim();
                    System.out.print("Enter Transfer Amount: ");
                    double amount = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Is this your own account? (y/n): ");
                    boolean isOwn = scanner.nextLine().trim().equalsIgnoreCase("y");

                    Transaction tx = txService.transfer(srcId, tgtId, amount, isOwn);
                    System.out.println("Transfer successful! Remaining Balance: $" + tx.getPostBalance());
                }
                case "5" -> {
                    System.out.print("Enter Account ID for Statement: ");
                    String accId = scanner.nextLine().trim();
                    Optional<Account> acc = accountRepo.findById(accId);
                    if (acc.isPresent()) {
                        System.out.println("\n================ ACCOUNT STATEMENT ================");
                        System.out.println("Account: " + accId + " | Current Balance: $" + acc.get().getBalence());
                        System.out.println("--------------------------------------------------");
                        List<Transaction> list = txService.getFilteredTransactions(accId, "all");
                        printTransactions(list);
                    } else {
                        System.out.println("Account not found.");
                    }
                }
                case "6" -> {
                    System.out.print("Enter Account ID: ");
                    String accId = scanner.nextLine().trim();
                    System.out.println("Filters: today | yesterday | last_7_days | last_30_days");
                    System.out.print("Enter filter condition: ");
                    String filter = scanner.nextLine().trim();
                    List<Transaction> list = txService.getFilteredTransactions(accId, filter);
                    printTransactions(list);
                }
                case "7" -> {
                    authService.logout();
                    System.out.println("Logged out successfully.");
                }
                default -> System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.out.println("\n[!] Transaction Error: " + e.getMessage());
        }
    }

    private static void printTransactions(List<Transaction> list) {
        if (list.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.printf("%-20s | %-15s | %-10s | %-12s\n", "Date/Time", "Type", "Amount", "Post-Balance");
        System.out.println("------------------------------------------------------------------");
        for (Transaction tx : list) {
            String timeStr = tx.getTimestamp() != null ? tx.getTimestamp().toString() : "";
            if (timeStr.length() > 19) {
                timeStr = timeStr.substring(0, 19);
            }
            System.out.printf("%-20s | %-15s | $%-9.2f | $%-11.2f\n",
                    timeStr,
                    tx.getType(),
                    tx.getAmount(),
                    tx.getPostBalance());
        }
    }

    private static void bootstrapBankerIfEmpty(UserRepository userRepo) {
        if (userRepo.findall().isEmpty()) {
            Banker defaultAdmin = new Banker("B-01", "admin", PasswordUtils.hashPassword("admin123"));
            userRepo.save(defaultAdmin);
            System.out.println("[Init] Created default Banker account -> Username: admin | Password: admin123");
        }
    }
}