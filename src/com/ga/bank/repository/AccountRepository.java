package com.ga.bank.repository;
import com.ga.bank.model.account.Account;
import java.util.List;
import java.util.Optional;


public interface AccountRepository {
    void save(Account account);
    Optional<Account> findById(String accountId);
    Optional<Account> findByCardNumber(String cardNumber);
    List<Account> findAll();
    void update(Account account);
}
