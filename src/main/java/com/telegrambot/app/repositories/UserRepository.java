package com.telegrambot.app.repositories;    /*
 *created by WerWolfe on UserRepository
 */

import com.telegrambot.app.model.user.UserBD;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserBD, Long> {
    Optional<UserBD> findByPhoneIgnoreCase(String phone);

}
