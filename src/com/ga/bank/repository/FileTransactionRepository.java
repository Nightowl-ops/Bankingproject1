package com.ga.bank.repository;

import com.ga.bank.model.transaction.Transaction;
import com.ga.bank.model.transaction.TransactionType;
import com.ga.bank.util.FileStorageUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileTransactionRepository implements TransactionRepository {

    private final String baseDir;

    public FileTransactionRepository() {
        this("data/transactions");
    }

    public FileTransactionRepository(String baseDir) {
        this.baseDir = baseDir;
        FileStorageUtils.ensureDirectoryExists(baseDir);
    }

    @Override
    public void save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Cannot save null transaction.");
        }

        // Line format: TX_ID|SOURCE_ID|TARGET_ID|TYPE|AMOUNT|POST_BALANCE|TIMESTAMP
        String line = String.join("|",
                transaction.getTransactionId(),
                transaction.getSourceAccountId() != null ? transaction.getSourceAccountId() : "NULL",
                transaction.getTargetAccountId() != null ? transaction.getTargetAccountId() : "NULL",
                transaction.getType().name(),
                String.valueOf(transaction.getAmount()),
                String.valueOf(transaction.getPostBalance()),
                transaction.getTimestamp().toString()
        );

        // Append to source account ledger
        if (transaction.getSourceAccountId() != null) {
            String sourceFile = baseDir + "/Transactions-" + transaction.getSourceAccountId() + ".txt";
            appendLine(sourceFile, line);
        }

        // Append to target account ledger if different from source
        if (transaction.getTargetAccountId() != null
                && !transaction.getTargetAccountId().equalsIgnoreCase(transaction.getSourceAccountId())) {
            String targetFile = baseDir + "/Transactions-" + transaction.getTargetAccountId() + ".txt";
            appendLine(targetFile, line);
        }
    }

    @Override
    public List<Transaction> findByAccountId(String accountId) {
        if (accountId == null) return Collections.emptyList();
        String filePath = baseDir + "/Transactions-" + accountId + ".txt";
        return parseTransactionLines(FileStorageUtils.readLines(filePath));
    }

    @Override
    public List<Transaction> findByAccountIdAndDate(String accountId, LocalDate date) {
        if (accountId == null || date == null) return Collections.emptyList();
        return findByAccountId(accountId).stream()
                .filter(tx -> tx.getTimestamp() != null && tx.getTimestamp().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findAll() {
        File folder = new File(baseDir);
        File[] files = folder.listFiles((dir, name) -> name.startsWith("Transactions-") && name.endsWith(".txt"));

        if (files == null) return Collections.emptyList();

        Set<String> seenIds = new LinkedHashSet<>();
        List<Transaction> uniqueTransactions = new ArrayList<>();

        for (File file : files) {
            List<Transaction> parsed = parseTransactionLines(FileStorageUtils.readLines(file.getAbsolutePath()));
            for (Transaction tx : parsed) {
                if (seenIds.add(tx.getTransactionId())) {
                    uniqueTransactions.add(tx);
                }
            }
        }
        return uniqueTransactions;
    }

    private void appendLine(String filePath, String line) {
        Path path = Paths.get(filePath);
        try {
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.write(
                    path,
                    List.of(line),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Failed to append transaction line to " + filePath + ": " + e.getMessage());
        }
    }

    private List<Transaction> parseTransactionLines(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] parts = line.split("\\|");
            if (parts.length < 7) continue;

            String txId = parts[0];
            String sourceId = "NULL".equalsIgnoreCase(parts[1]) ? null : parts[1];
            String targetId = "NULL".equalsIgnoreCase(parts[2]) ? null : parts[2];
            TransactionType type = TransactionType.valueOf(parts[3]);
            double amount = Double.parseDouble(parts[4]);
            double postBalance = Double.parseDouble(parts[5]);
            LocalDateTime timestamp = LocalDateTime.parse(parts[6]);

            Transaction tx = new Transaction(sourceId, targetId, type, amount, postBalance, txId, timestamp);
            transactions.add(tx);
        }
        return transactions;
    }
}