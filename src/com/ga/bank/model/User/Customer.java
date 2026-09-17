package com.ga.bank.model.User;

public class Customer extends User {

    private long lockedUntilTimestamp;

    // Constructor for creating a brand-new Customer
    public Customer(String id, String name, String passwordHash) {
        super(id, name, passwordHash, Role.CUSTOMER);
        this.lockedUntilTimestamp = 0L;
    }

    // Constructor for restoring an existing Customer from file storage
    public Customer(String id, String name, String passwordHash, int failedLoginAttempts, boolean isLocked, long lockedUntilTimestamp) {
        super(id, name, passwordHash, Role.CUSTOMER);
        this.failedLoginAttempts = failedLoginAttempts;
        this.isLocked = isLocked;
        this.lockedUntilTimestamp = lockedUntilTimestamp;
    }

    @Override
    public boolean isLocked() {
        if (this.lockedUntilTimestamp > 0L) {
            // Still within lockout window
            if (System.currentTimeMillis() < this.lockedUntilTimestamp) {
                return true;
            }
            // Timer expired: auto-reset
            this.lockedUntilTimestamp = 0L;
            this.isLocked = false;
            this.failedLoginAttempts = 0;
            return false;
        }
        return this.isLocked;
    }


    public void lockForDuration(long durationMillis) {
        this.isLocked = true;
        this.lockedUntilTimestamp = System.currentTimeMillis() + durationMillis;
    }

    public long getLockedUntilTimestamp() {
        return lockedUntilTimestamp;
    }

    public void setLockedUntilTimestamp(long lockedUntilTimestamp) {
        this.lockedUntilTimestamp = lockedUntilTimestamp;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", failedLoginAttempts=" + failedLoginAttempts +
                ", isLocked=" + this.isLocked + // Read the field directly to prevent side effects during logging
                ", lockedUntilTimestamp=" + lockedUntilTimestamp +
                '}';
    }
}