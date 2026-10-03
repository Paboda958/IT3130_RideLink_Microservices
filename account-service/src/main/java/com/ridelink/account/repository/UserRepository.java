package com.ridelink.account.repository;

import com.ridelink.account.model.Role;
import com.ridelink.account.model.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
    boolean existsByEmail(String email);
    List<UserAccount> findByRole(Role role);
}
