package com.supportbot.repository;

import com.supportbot.moskModel.TestDoc;
import com.supportbot.repositories.EntityRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public interface TestDocRepository extends EntityRepository<TestDoc> {
}
