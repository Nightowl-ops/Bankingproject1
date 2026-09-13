package com.ga.bank.repository;

import com.ga.bank.model.User.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findbyid(String id);
    Optional<User> findbyname(String name);
    List<User> findall();
    void update(User user);
}