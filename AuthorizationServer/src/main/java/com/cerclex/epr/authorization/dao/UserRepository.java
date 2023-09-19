package com.cerclex.epr.authorization.dao;

import com.cerclex.epr.authorization.dtos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String Id);

    Optional<User> findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    HashSet<User> findByUsernameContaining(String username);

    HashSet<User> findByFullnameContaining(String fullname);
}
