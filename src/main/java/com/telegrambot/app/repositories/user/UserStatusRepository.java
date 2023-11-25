package com.telegrambot.app.repositories.user;

import com.telegrambot.app.model.reference.legalentity.LegalEntity;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserStatusRepository extends CrudRepository<UserStatus, Long> {
    @Query("select u from UserStatus u where u.legal = ?1")
    List<UserStatus> findByLegal(LegalEntity legal);

    @Query("select u from UserStatus u where u.user = ?1 and u.legal = ?2")
    List<UserStatus> findByUserAndLegal(UserBD user, LegalEntity legal);

    @Query("select u from UserStatus u where u.user = ?1")
    List<UserStatus> findByUser(UserBD user);

    List<UserStatus> findByUserAndLegalNotNull(UserBD user);

    @Transactional
    void deleteByUser(UserBD user);

    List<UserStatus> findByUserOrderByLastUpdateDesc(UserBD user);

    UserStatus findFirstByUserOrderByLastUpdateDesc(UserBD user);

    void deleteByIdAllIgnoreCase(Long id);

}
