package com.ga.bank.repository;

import com.ga.bank.model.transaction.Transaction;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findByAccountId(String accountId);
    List<Transaction> findByAccountIdAndDate(String accountId, LocalDate date);
    List<Transaction> findAll();
}