package com.telegrambot.app.repositories;    /*
 *created by WerWolfe on UserRepository
 */

import com.telegrambot.app.model.user.UserBD;

import java.util.Optional;

public interface UserRepository extends EntityRepository<UserBD> {
    Optional<UserBD> findByPhone(String phone);

    Optional<UserBD> findByPhoneIgnoreCase(String phone);

}
