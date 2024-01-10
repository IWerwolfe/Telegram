package com.supportbot.repositories.balance;

import com.supportbot.model.balance.UserBalance;
import com.supportbot.model.user.UserBD;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserBalanceRepository extends CrudRepository<UserBalance, Long> {
    Optional<UserBalance> findByUser(UserBD user);
}