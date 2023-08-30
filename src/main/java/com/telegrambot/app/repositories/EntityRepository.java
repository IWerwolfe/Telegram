package com.telegrambot.app.repositories;

import com.telegrambot.app.model.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface EntityRepository<T extends Entity> extends JpaRepository<T, Long> {

    Optional<T> findBySyncDataNotNullAndSyncData_Guid(String guid);

    List<T> findBySyncDataNull();

}
