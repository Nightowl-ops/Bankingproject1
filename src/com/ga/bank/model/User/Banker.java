package com.ga.bank.model.User;

public class Banker extends User {

    public Banker(String id, String name, String passwordHash) {
        super(id, name, passwordHash, Role.BANKER);
    }

    @Override
    public String toString() {
        return "banker{" +
                "id= " + id + "" +
                ", name = " + name +" "+
                ", role= " + role +
                "}";


    }}