package com.ga.bank.model.User;

public class Customer extends User{
    private int failedloginattempts;
    private long lovkeduntiltimestamp;

// this is the constructor when creating a new user

    public Customer(String id, String name, String passwordHash){
        super(id,name,passwordHash,Role.CUSTOMER);
        this.failedloginattempts=0;
        this.lovkeduntiltimestamp=0L;
    }
    // this is the constructor to deal with an already existing account

    public Customer(String id, String name, String passwordHash, int lovkeduntiltimestamp, long lockedUntilTimestamp) {
        super(id, name, passwordHash, Role.CUSTOMER);
        this.failedloginattempts = failedloginattempts;
        this.lovkeduntiltimestamp = lovkeduntiltimestamp;
    }

    public int getFailedloginattempts() {
        return failedloginattempts;
    }

    public void setFailedloginattempts(int failedloginattempts) {
        this.failedloginattempts = failedloginattempts;
    }

    public void incramentfaliedattemts(){
        this.failedloginattempts++;
    }
    public void resetfailedattempts(){
        this.failedloginattempts=0;
    }

    public long getLovkeduntiltimestamp() {
        return lovkeduntiltimestamp;
    }

    public void setLovkeduntiltimestamp(long lovkeduntiltimestamp) {
        this.lovkeduntiltimestamp = lovkeduntiltimestamp;
    }
// this is unix epoch time this is the time millisecond that have elapsed since january 1 1970
    //when you call it will return a huge number aprroximalty 1788984708000
    //
    public boolean isLocked() {
        return System.currentTimeMillis() < this.lovkeduntiltimestamp;
    }
    @Override
    public String toString(){
        return  "customer {"+"id =" +id + ", name " +name+ " ,role"+role+" , failedloginattemps ="+ failedloginattempts + " , lockeduntiltimestamp "+ lovkeduntiltimestamp+ "} ";
    }

}

