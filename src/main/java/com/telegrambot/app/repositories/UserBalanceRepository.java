package com.telegrambot.app.repositories;

import com.telegrambot.app.model.balance.UserBalance;
import com.telegrambot.app.model.user.UserBD;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserBalanceRepository extends CrudRepository<UserBalance, Long> {
    Optional<UserBalance> findByUser(UserBD user);
}