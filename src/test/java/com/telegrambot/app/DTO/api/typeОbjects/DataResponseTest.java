package com.telegrambot.app.DTO.api.typeОbjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataResponseTest {

    @Test
    void testNoArgsConstructor() {
        DataResponse response = new DataResponse();
        checkFillDefaultEntity(response);
    }

    @Test
    void testAllArgsConstructor() {
        DataResponse response = new DataResponse(true, "test");
        checkFillEntity_NonNull(response, "test", true);
    }

    @Test
    void testAllArgsConstructor_isNull() {
        DataResponse response = new DataResponse(true, null);
        checkFillEntity(response, null, true);
    }

    @Test
    void testAllArgsConstructor_isAllArgNull() {
        Boolean result = null;
        DataResponse response = new DataResponse(result, null);
        checkFillEntity(response, null, false);
    }


    void checkFillEntity_NonNull(DataResponse response, String error, Boolean result) {
        assertNotNull(response.isResult());
        assertNotNull(response.getError());
        checkFillEntity(response, error, result);
    }

    void checkFillEntity(DataResponse response, String error, Boolean result) {
        assertNotNull(response);
        assertEquals(response.isResult(), result);
        assertEquals(response.getError(), error);
    }

    void checkFillDefaultEntity(DataResponse response) {
        checkFillEntity_NonNull(response, "", false);
    }
}