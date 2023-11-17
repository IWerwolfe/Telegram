package com.telegrambot.app.DTO.api.typeОbjects;

import com.telegrambot.app.moskModel.TestDoc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataListResponseTest extends DataResponseTest {

    @Test
    @Override
    void testNoArgsConstructor() {
        DataListResponse<TestDoc> response = new DataListResponse<>();
        checkFillDefaultEntity(response);
        assertNull(response.getList());
    }

    @Test
    @Override
    void testAllArgsConstructor() {
        List<TestDoc> list = new ArrayList<>();
        list.add(new TestDoc("testGuid"));
        list.add(new TestDoc("testGuid1"));

        DataListResponse<TestDoc> response = new DataListResponse<>(true, "test", list);
        checkFillEntity_NonNull(response, "test", true);
        assertNotNull(response.getList());
        assertEquals(response.getList().size(), list.size());

        for (int i = 0; i < list.size(); i++) {
            assertEquals(response.getList().get(i), list.get(i));
        }
    }

    @Test
    @Override
    void testAllArgsConstructor_isNull() {
        DataListResponse<TestDoc> response = new DataListResponse<>(true, null, null);
        checkFillEntity(response, null, true);
        assertNull(response.getList());
    }

    @Test
    @Override
    void testAllArgsConstructor_isAllArgNull() {
        Boolean result = null;
        DataListResponse<TestDoc> response = new DataListResponse<>(result, null, null);
        checkFillEntity(response, null, false);
        assertNull(response.getList());
    }

}