package com.telegrambot.app.repository;

import com.telegrambot.app.moskModel.TestRef;
import com.telegrambot.app.repositories.EntityRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public interface TestRefRepository extends EntityRepository<TestRef> {
}
