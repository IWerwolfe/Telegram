package com.telegrambot.app.repositories;

import com.telegrambot.app.model.DefaultDocParam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultDocParamRepository extends JpaRepository<DefaultDocParam, Long> {
}