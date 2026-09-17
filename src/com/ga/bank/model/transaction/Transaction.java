package com.ga.bank.model.transaction;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private final String transactionId;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final TransactionType type;
    private final double amount;
    private final double postBalance;
    private final LocalDateTime timestamp;

    // Constructor for creating brand new transactions in memory
    public Transaction(String sourceAccountId, String targetAccountId, TransactionType type, double amount, double postBalance) {
        this.transactionId = UUID.randomUUID().toString();
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.timestamp = LocalDateTime.now();
    }

    // Overloaded constructor for rebuilding stored transactions from file
    public Transaction(String sourceAccountId, String targetAccountId, TransactionType type, double amount, double postBalance, String transactionId, LocalDateTime timestamp) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.transactionId = transactionId;
        this.timestamp = timestamp;
    }


    public TransactionType getType() {
        return type;
    }

    public String getTargetAccountId() {
        return targetAccountId;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public double getPostBalance() {
        return postBalance;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + transactionId + '\'' +
                ", source='" + (sourceAccountId != null ? sourceAccountId : "N/A") + '\'' +
                ", target='" + (targetAccountId != null ? targetAccountId : "N/A") + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", postBalance=" + postBalance +
                ", time=" + timestamp +
                '}';
    }
}