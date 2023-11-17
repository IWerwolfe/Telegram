package com.telegrambot.app.DTO.api.typeОbjects;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EntityResponseTest {

    @InjectMocks
    private EntityResponse entityResponse;

    private String json = "{\"code\":\"testCode\",\"guid\":\"testGuid\",\"name\":\"testName\",\"markedForDel\":true,\"guidAuthor\":\"testGuidAuthor\"}";
    private String uncorrectJson = "{\"code\":\"testCode\",\"guid\":\"testGuid\",\"name\":\"testName\",\"markedForDel\":true,\"guidAuthor\":\"testGuidAuthor\",\"testField\":\"testResult\"}";

    @Test
    void testNoArgsConstructor() {
        EntityResponse response = new EntityResponse();
        assertNotNull(response);
    }

    @Test
    void testAllArgsConstructor() {
        EntityResponse response = new EntityResponse("testCode", "testGuid", "testName", true, "testGuidAuthor");
        assertNotNull(response);
        checkingFillFields(response);
    }

    @Test
    void testCreateToJson() throws JsonProcessingException {
        EntityResponse.fillToJson(json, EntityResponse.class, entityResponse);
        checkingFillFields(entityResponse);
    }

    @Test
    void testCreateToJson_jsonIsNull() throws JsonProcessingException {
        entityResponse.setName("ok");
        EntityResponse.fillToJson(null, EntityResponse.class, entityResponse);

        assertNotNull(entityResponse);
        assertEquals("ok", entityResponse.getName());
    }

    @Test
    void testCreateToJson_entityIsNull() throws JsonProcessingException {
        EntityResponse entity = null;
        EntityResponse.fillToJson(json, EntityResponse.class, entity);
        assertNull(entity);
    }

    private void checkingFillFields(EntityResponse response) {
        assertEquals("testCode", response.getCode());
        assertEquals("testGuid", response.getGuid());
        assertEquals("testName", response.getName());
        assertEquals(true, response.getMarkedForDel());
        assertEquals("testGuidAuthor", response.getGuidAuthor());
    }

    @Test
    void testCreateToJsonToManyField() throws JsonProcessingException {
        EntityResponse.fillToJson(uncorrectJson, EntityResponse.class, entityResponse);
        checkingFillFields(entityResponse);
    }

    @Test
    void testCreateToJsonException() throws JsonProcessingException {

        String json = "invalidJson";
        entityResponse.setName("ok");

        EntityResponse.fillToJson(json, EntityResponse.class, entityResponse);

        assertNotNull(entityResponse);
        assertNull(entityResponse.getCode());
        assertNull(entityResponse.getGuid());
        assertNull(entityResponse.getGuidAuthor());
        assertEquals("ok", entityResponse.getName());
    }
}