package com.telegrambot.app.DTO.api.typeОbjects;

import com.telegrambot.app.moskModel.TestDoc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataEntityResponseTest extends DataResponseTest {

    @Test
    void testNoArgsConstructor() {
        DataEntityResponse<TestDoc> response = new DataEntityResponse<>();
        checkFillDefaultEntity(response);
        assertNull(response.getEntity());
    }

    @Test
    void testAllArgsConstructor() {
        TestDoc testDoc = new TestDoc("testGuid");

        DataEntityResponse<TestDoc> response = new DataEntityResponse<>(true, "test", testDoc);
        checkFillEntity_NonNull(response, "test", true);
        assertNotNull(response.getEntity());
        assertEquals(response.getEntity().getGuidEntity(), "testGuid");
    }

    @Test
    @Override
    void testAllArgsConstructor_isNull() {
        DataEntityResponse<TestDoc> response = new DataEntityResponse<>(true, null, null);
        checkFillEntity(response, null, true);
        assertNull(response.getEntity());
    }

    @Test
    @Override
    void testAllArgsConstructor_isAllArgNull() {
        Boolean result = null;
        DataEntityResponse<TestDoc> response = new DataEntityResponse<>(result, null, null);
        checkFillEntity(response, null, false);
        assertNull(response.getEntity());
    }
}