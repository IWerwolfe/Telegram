package com.supportbot.repositories.user;    /*
 *created by WerWolfe on UserRepository
 */

import com.supportbot.model.user.UserBD;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserBD, Long> {
    Optional<UserBD> findByPhone(String phone);

    Optional<UserBD> findByPhoneIgnoreCase(String phone);

}
