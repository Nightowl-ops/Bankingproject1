package com.ga.bank.model.card;

public class DebitCard {
    //because of the final key word we need to create a consturctor becuase it say you said its final what is final ? so you create the constructor

private final String CardNumber;
private final CardType cardType;

//this is the constructor
    // the card is created object
    // then it is passed to the debtcard object with the cardtype as the parameter
    public DebitCard(String cardNumber, CardType cardType) {
        this.CardNumber = cardNumber;
        this.cardType = cardType;
    }
// the setters and getters are not for the this class its is to be used to get the value in other classes
    public String getCardNumber() {
        return CardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    //since we are in the same class we can use he varbales you can use the getter it would get the same reuslt
    @Override
    public String toString(){
        return "DebitCard { "+ "cardnumber " + getCardNumber();
    }
}
