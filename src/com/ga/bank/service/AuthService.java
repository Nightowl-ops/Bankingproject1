package com.ga.bank.service;

import com.ga.bank.model.User.User;
import com.ga.bank.repository.UserRepository;
import com.ga.bank.util.PasswordUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class AuthService {
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION_SECONDS = 60; // 1 minute lock

    private final UserRepository userRepository;
    private LocalDateTime lockoutTimestamp;
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String name, String password) {
        // 1. Check if system is currently under the 1-minute lockout timer
        if (isLockedOut()) {
            long secondsRemaining = getRemainingLockoutSeconds();
            throw new IllegalStateException("Security alert: System is temporarily locked. Try again in "
                    + secondsRemaining + " seconds.");
        }

        // 2. Fetch user from repository
        Optional<User> userOpt = userRepository.findbyname(name);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + name);
        }

        User user = userOpt.get();

        // 3. Verify encrypted password hash
        if (!PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);

            if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.setLocked(true);
                this.lockoutTimestamp = LocalDateTime.now();
                userRepository.update(user);
                throw new IllegalStateException("Security alert: 3 consecutive failed attempts. System locked for 1 minute.");
            }

            userRepository.update(user);
            int remainingAttempts = MAX_FAILED_ATTEMPTS - user.getFailedLoginAttempts();
            throw new IllegalArgumentException("Invalid credentials. Attempts remaining: " + remainingAttempts);
        }

        // 4. Successful login -> reset attempt counter & set session user
        user.setFailedLoginAttempts(0);
        user.setLocked(false);
        this.lockoutTimestamp = null;
        userRepository.update(user);

        this.currentUser = user;
        return user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public boolean isLockedOut() {
        if (lockoutTimestamp == null) {
            return false;
        }
        long secondsElapsed = Duration.between(lockoutTimestamp, LocalDateTime.now()).getSeconds();
        if (secondsElapsed >= LOCKOUT_DURATION_SECONDS) {
            lockoutTimestamp = null;
            return false;
        }
        return true;
    }

    public long getRemainingLockoutSeconds() {
        if (lockoutTimestamp == null) {
            return 0;
        }
        long secondsElapsed = Duration.between(lockoutTimestamp, LocalDateTime.now()).getSeconds();
        return Math.max(0, LOCKOUT_DURATION_SECONDS - secondsElapsed);
    }
}