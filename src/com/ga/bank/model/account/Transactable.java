package com.ga.bank.model.account;




public interface Transactable{
    void deposite(double amount);
    void withdraw(double amount);
    void transfer(Account targetAccount , double amount);
}




