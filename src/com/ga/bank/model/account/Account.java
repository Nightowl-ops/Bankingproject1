package com.ga.bank.model.account;

import com.ga.bank.model.card.DebitCard;



public abstract class Account implements Transactable{//

    // we didnt male it private so we dont have to deal with the setting and getting and protected gets passed to the children

    protected final String accountid;
    protected double balence;
    protected DebitCard debtcard;
    protected AccountStatus status;

    public Account(String accountid,double balence,DebitCard debtcard){
        this.accountid=accountid;
        this.balence=balence;
        this.debtcard=debtcard;
        this.status=AccountStatus.ACTIVE;


   }


   // will start with the logic of the deposite and withdraw

    @Override
    public void deposite(double amount){
        if(amount <= 0){
            // should i throw an unexpected argument here????? but i should add to the methods in the interfaces throw exception :/ ??

            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        this.balence+=amount;

    }
    @Override
    public void transfer(Account targetAccount, double amount) {

        if (targetAccount == null) {
            throw new IllegalArgumentException("Target account cannot be null.");
        }
        this.withdraw(amount);
        targetAccount.deposite(amount);
    }

    public String getAccountid() {
        return accountid;
    }

    public AccountStatus getStatus() {
        return status;

    }
        public void setStatus(AccountStatus status) {
            this.status = status;
        }


    public double getBalence() {
        return balence;
    }

    public DebitCard getDebtcard() {
        return debtcard;
    }

    public void setDebtcard(DebitCard debtcard) {
        this.debtcard = debtcard;
    }
    @Override
    public String toString() {
        return "account {" +
                "accountId  " + accountid +
                " balance " + balence +
                " status " + status +
                ", debitcard =" + debtcard +
                "}";
    }

}
