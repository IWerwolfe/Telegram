package com.telegrambot.app.model.documents.docdata;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SyncDataTest {

    @Test
    public void testSyncDataConstructorWithGuid() {
        String guid = "123";
        SyncData syncData = new SyncData(guid);
        assertEquals(guid, syncData.getGuid());
        assertNotNull(syncData.getLastUpdate());
    }

    @Test
    public void testSyncDataConstructorWithGuidAndCode() {
        String guid = "123";
        String code = "ABC";
        SyncData syncData = new SyncData(guid, code);
        assertEquals(guid, syncData.getGuid());
        assertEquals(code, syncData.getCode());
        assertNotNull(syncData.getLastUpdate());
    }

    @Test
    public void testToStringWhenSynced() {
        String guid = "123";
        String code = "ABC";
        SyncData syncData = new SyncData(guid, code);
        String expected = "Синхронизирован " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")) +
                System.lineSeparator() +
                "guid: " + guid + System.lineSeparator() +
                "code: " + code;
        assertEquals(expected, syncData.toString());
    }

    @Test
    public void testToStringWhenSyncedIsCodeNull() {
        String guid = "123";
        String code = null;
        SyncData syncData = new SyncData(guid, code);
        String expected = "Синхронизирован " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")) +
                System.lineSeparator() +
                "guid: " + guid + System.lineSeparator();
        assertEquals(expected, syncData.toString());
    }

    @Test
    public void testToStringWhenSyncedIsGuidNull() {
        String guid = null;
        String code = "ABC";
        SyncData syncData = new SyncData(guid, code);
        String expected = "НЕ синхронизировано";
        assertEquals(expected, syncData.toString());
    }

    @Test
    public void testToStringWhenNotSynced() {
        SyncData syncData = new SyncData(null);
        String expected = "НЕ синхронизировано";
        assertEquals(expected, syncData.toString());
    }

}