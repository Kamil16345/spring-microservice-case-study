package com.betacom.demo.repository;

import com.betacom.demo.model.security.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.login = :login")
    User findUserByLogin(String login);
}
