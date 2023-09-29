package com.telegrambot.app.repositories.user;

import com.telegrambot.app.model.reference.legalentity.LegalEntity;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserStatusRepository extends CrudRepository<UserStatus, Long> {
    @Query("select u from UserStatus u where u.userBD = ?1 and u.legal = ?2")
    List<UserStatus> findByUserBDAndLegal(UserBD userBD, LegalEntity legal);

    @Query("select u from UserStatus u where u.userBD = ?1")
    List<UserStatus> findByUserBD(UserBD userBD);

    List<UserStatus> findByUserBDAndLegalNotNull(UserBD userBD);

    @Transactional
    void deleteByUserBD(UserBD userBD);

    List<UserStatus> findByUserBDOrderByLastUpdateDesc(UserBD userBD);

    UserStatus findFirstByUserBDOrderByLastUpdateDesc(UserBD userBD);

    void deleteByIdAllIgnoreCase(Long id);

}
