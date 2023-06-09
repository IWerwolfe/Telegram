package com.telegrambot.app.repositories;    /*
 *created by WerWolfe on UserRepository
 */

import com.telegrambot.app.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
