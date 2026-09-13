package com.ga.bank.model.transaction;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private final String transactionId;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;

    public Transaction(String sourceAccountId, String targetAccountId, TransactionType type, double amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + transactionId + '\'' +
                ", source='" + (sourceAccountId != null ? sourceAccountId : "N/A") + '\'' +
                ", target='" + (targetAccountId != null ? targetAccountId : "N/A") + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", time=" + timestamp +
                '}';
    }
}