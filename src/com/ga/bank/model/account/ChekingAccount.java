package com.ga.bank.model.account;

import com.ga.bank.model.card.DebitCard;

public class ChekingAccount extends Account implements Transactable{

    public static final double  overdraftfee = 35.0;
    public static final double  negativebalencewithdrawcap= 100.0;
    public static final int maxoverdrafthitting =2;


    private int overdraftcount;

    // two construtors one for the user new is introduced
    public ChekingAccount (String accountid, double initalbalence, DebitCard debitcard){

        super(accountid,initalbalence,debitcard);
        this.overdraftcount=0;

    }
    // and the second is for when a user already in the database info
    public ChekingAccount(String accountId, double balance, DebitCard debitCard, AccountStatus status, int overdraftcount) {
        super(accountId, balance, debitCard);
        this.status = status;
        this.overdraftcount = overdraftcount;

    }




    // adding the withdraw methods
    @Override
    public void withdraw (double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("withdraw amount must be greater than zero ");

        }
        if(this.status == AccountStatus.DEACTIVATED){
            throw new IllegalArgumentException("account is deactivated due to hitting the overdraft limit ");


        }

        if(this.balence <0 && amount >negativebalencewithdrawcap){
            throw new IllegalArgumentException("Account balence is negative maximun withdrawl allowed is  "+ negativebalencewithdrawcap);



        }

        this.balence -=amount;

        if(this.balence<0){
            this.balence -= overdraftfee;
            this.overdraftcount ++;

            if(this.overdraftcount >= maxoverdrafthitting){
                this.status = AccountStatus.DEACTIVATED;

            }}}

    public int getOverdraftcount() {
        return overdraftcount;
    }

    public void setOverdraftcount(int overdraftcount) {
        this.overdraftcount = overdraftcount;
    }


    public void resetOverdraftCount() {
        this.overdraftcount = 0;
    }

    @Override
    public String toString() {
        return "checkingaccount{" +
                "accountid='" + accountid + " " +
                ", balance=" + balence +
                ", status=" + status +
                ", overdraftCount=" + overdraftcount +
                ", debitCard=" + debtcard +
                "}";
    }
    /**
     * Reactivates the account if the customer has resolved the negative balance.
     * Resets status to ACTIVE and clears the overdraft counter.
     */
    public boolean reactivate() {
        if (this.balence >= 0) {
            this.status = AccountStatus.ACTIVE;
            this.overdraftcount = 0;
            return true;
        }
        return false;
    }
}