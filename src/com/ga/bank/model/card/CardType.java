package com.ga.bank.model.card;

public enum CardType {

    // these are the object for the constructor class
    MASTER_PLATINUM(20000.0, 40000.0, 80000.0, 100000.0, 200000.0),
    MASTER_TITANIUM(10000.0, 20000.0, 40000.0, 100000.0, 200000.0),
    MASTER_STANDARD(5000.0,  10000.0, 20000.0, 100000.0, 200000.0);

    // setting the varbale needed for the card limits
private final double dailywithdrawlimit;
private final double dailytransferlimit;
private final double dailytransferownlimit;
private final double dailydepositownlimit;
private final double dailydepositlimit;

// making the constructor
    CardType(double dailywithdrawlimit,double dailytransferlimit,double dailytransferownlimit,double dailydepositownlimit,double dailydepositlimit){
        this.dailydepositlimit=dailydepositlimit;
        this.dailytransferlimit=dailytransferlimit;
        this.dailywithdrawlimit=dailywithdrawlimit;
        this.dailytransferownlimit=dailytransferownlimit;
        this.dailydepositownlimit=dailydepositownlimit;



    }

    public double getDailywithdrawlimit() {
        return dailywithdrawlimit;
    }

    public double getDailytransferlimit() {
        return dailytransferlimit;
    }

    public double getDailytransferownlimit() {
        return dailytransferownlimit;
    }

    public double getDailydepositownlimit() {
        return dailydepositownlimit;
    }

    public double getDailydepositlimit() {
        return dailydepositlimit;
    }
}
