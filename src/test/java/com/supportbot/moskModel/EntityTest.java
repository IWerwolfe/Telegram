package com.supportbot.moskModel;

import com.supportbot.model.documents.docdata.SyncData;
import com.supportbot.model.types.Entity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityTest {

    @Test
    void testEntity() {
        // Создаем заглушку для Entity
        Entity mockEntity = Mockito.mock(Entity.class);

        // Задаем поведение заглушки
        Mockito.when(mockEntity.getId()).thenReturn(1L);
        Mockito.when(mockEntity.getSyncData()).thenReturn(new SyncData("testGuid"));
        Mockito.when(mockEntity.getAuthor()).thenReturn("testAuthor");
        Mockito.when(mockEntity.getMarkedForDel()).thenReturn(false);

        // Выполняем действия с заглушкой
        Long id = mockEntity.getId();
        SyncData syncData = mockEntity.getSyncData();
        String author = mockEntity.getAuthor();
        Boolean markedForDel = mockEntity.getMarkedForDel();

        // Проверяем результаты
        assertEquals(1L, id);
        assertEquals("testGuid", syncData.getGuid());
        assertEquals("testAuthor", author);
        assertEquals(false, markedForDel);
    }
}
