package com.ga.bank.model.User;

public abstract class User {

    protected final String id;
    protected final String name;
    // we havent made the password to be final why so that we can make a setter
    //reason why would we do that because a user might reset his or her password
    //but the name id and role will not change for that customer

    protected String passwordHash;
    // thise is object of role so it will be able to see access the two roles we have which are either banker or customer

    protected final Role role ;




    public User(String id ,String name , String passwordHash,Role role){
        this.id=id;
        this.name=name;
        this.passwordHash=passwordHash;
        this.role=role;


    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }
    // the reason for not printing the user password is because it is secure and shouldnt be shown so easliy
    public String toString(){
        return "user {" + "id = " + id + " ,"+"name"+ name +" "+"role"+ role+" }";
    }
}
