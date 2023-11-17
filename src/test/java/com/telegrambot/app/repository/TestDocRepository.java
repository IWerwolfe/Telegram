package com.telegrambot.app.repository;

import com.telegrambot.app.moskModel.TestDoc;
import com.telegrambot.app.repositories.EntityRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public interface TestDocRepository extends EntityRepository<TestDoc> {
}
