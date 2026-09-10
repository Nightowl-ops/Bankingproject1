package com.ga.bank.model.account;

import com.ga.bank.model.card.DebitCard;

public class SavingAccount extends Account {

    public SavingAccount(String accountId, double initialBalance, DebitCard debitCard) {
        super(accountId, initialBalance, debitCard);
    }

    public  SavingAccount(String accountId, double balance, DebitCard debitCard, AccountStatus status) {
        super(accountId, balance, debitCard);
        this.status = status;
    }


    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (this.status == AccountStatus.DEACTIVATED) {
            throw new IllegalStateException("Account is DEACTIVATED. Transactions are blocked.");
        }
        if (this.balence - amount < 0) {
            throw new IllegalArgumentException("Insufficient funds. Savings accounts cannot have a negative balance.");
        }
        this.balence -= amount;
    }

        @Override
        public String toString() {
            return "savingsaccount{" +
                    "accountid='" + accountid + '\'' +
                    ", balance=" + balence +
                    ", status=" + status +
                    ", debitcard=" + debtcard +
                    '}';
        }
    }


