package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.model.documents.doc.service.TaskDoc;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.repositories.doc.TaskDocRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConverterTest {

    @Mock
    private TaskDocRepository repository;

    @Test
    void convertToResponse() {
    }

    @Test
    void updateEntity() {
    }

    @Test
    void getOrCreateEntity() {
    }

    @Test
    void testGetOrCreateEntity() {
    }

    @Test
    void convertToEntityList() {
    }

    @Test
    void convertDocToResponse() {
    }

    @Test
    void fillResponseToDoc() {
    }

    @Test
    void convertReferenceToResponse() {
    }

    @Test
    void convertToEntity() {
    }

    @Test
    void testConvertToEntityList() {
    }

    @Test
    void convertToEntityListIsSave() {
    }

    @Test
    void getOrCreateEntity_withNonNullGuidAndEntityExists_shouldReturnExistingEntity() {
        String guid = "testGuid";
        TaskDoc existingEntity = new TaskDoc(guid);
        when(repository.findBySyncDataNotNullAndSyncData_Guid(guid)).thenReturn(Optional.of(existingEntity));

        TaskDoc result = Converter.getOrCreateEntity(guid, repository, TaskDoc.class);

        assertThat(result).isEqualTo(existingEntity);
        verify(repository, never()).save(any());
    }

    @Test
    void getOrCreateEntity_withNonNullGuidAndEntityDoesNotExist_shouldCreateAndSaveNewEntity() {
        String guid = "testGuid";
        TaskDoc result;
        when(repository.findBySyncDataNotNullAndSyncData_Guid(guid)).thenReturn(Optional.empty());

        result = Converter.getOrCreateEntity(guid, repository, TaskDoc.class, true);
        verify(repository, times(1)).save(any());

        result = Converter.getOrCreateEntity(guid, repository, TaskDoc.class, false);
        assertThat(result).isNotNull();
        verify(repository, times(0)).save(any());
    }

    @Test
    void getOrCreateEntity_withNullGuid_shouldReturnNull() {
        EntityResponse response = null;
        TaskDoc result = Converter.getOrCreateEntity(response, repository, TaskDoc.class);

        assertThat(result).isNull();
        verify(repository, never()).save(any());
        verify(repository, never()).findBySyncDataNotNullAndSyncData_Guid(any());
    }

    @Test
    void getOrCreateEntity_withEmptyGuid_shouldReturnNull() {
        TaskDoc result = Converter.getOrCreateEntity("", repository, TaskDoc.class);

        assertThat(result).isNull();
        verify(repository, never()).save(any());
        verify(repository, never()).findBySyncDataNotNullAndSyncData_Guid(any());
    }

    @Test
    void convertToLocalDateTime_shouldReturnNullForNullDate() {
        assertThat(Converter.convertToLocalDateTime(null)).isNull();
    }

    @Test
    void convertToLocalDateTime_shouldReturnLocalDateTimeForNonNullDate() {
        Date date = new Date();
        LocalDateTime result = Converter.convertToLocalDateTime(date);
        assertThat(result).isNotNull();
    }

    @Test
    void convertToDate_shouldReturnNullForNullLocalDateTime() {
        assertThat(Converter.convertToDate(null)).isNull();
    }

    @Test
    void convertToDate_shouldReturnDateForNonNullLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now(); // replace with your specific LocalDateTime
        Date result = Converter.convertToDate(localDateTime);
        assertThat(result).isNotNull();
    }

    @Test
    void convertLongToLocalDateTime_shouldReturnNullForZeroLong() {
        assertThat(Converter.convertLongToLocalDateTime(0L)).isNull();
    }

    @Test
    void convertLongToLocalDateTime_shouldReturnLocalDateTimeForNonZeroLong() {
        long nonZeroLong = 1637172800000L;
        LocalDateTime result = Converter.convertLongToLocalDateTime(nonZeroLong);
        assertThat(result).isNotNull();
    }

    @Test
    void convertToGuid_shouldReturnEmptyStringForNullEntity() {
        String result = Converter.convertToGuid(null);
        assertThat(result).isEmpty();
    }

    @Test
    void convertToGuid_shouldReturnEmptyStringForNullSyncData() {
        Entity entity = new TaskDoc();
        String result = Converter.convertToGuid(entity);
        assertThat(result).isEmpty();
    }

    @Test
    void convertToGuid_shouldReturnGuidFromSyncData() {
        String expectedGuid = "testGuid";
        Entity entity = new TaskDoc(expectedGuid);
        String result = Converter.convertToGuid(entity);
        assertThat(result).isEqualTo(expectedGuid);
    }

    @Test
    void convertToGuid_shouldReturnEmptyStringForNullSyncDataGuid() {
        Entity entity = new TaskDoc();
        entity.setSyncData(new SyncData(null, "testCode"));
        String result = Converter.convertToGuid(entity);
        assertThat(result).isNull();
    }

    enum TestEnum {
        VALUE_ONE,
        VALUE_TWO
    }

    @Test
    void convertToEnum_shouldReturnNullForNullOrEmptyValue() {
        assertThat(Converter.convertToEnum(null, TestEnum.class)).isNull();
        assertThat(Converter.convertToEnum("", TestEnum.class)).isNull();
    }

    @Test
    void convertToEnum_shouldReturnEnumForValidValue() {
        assertThat(Converter.convertToEnum("VALUE_ONE", TestEnum.class)).isEqualTo(TestEnum.VALUE_ONE);
        assertThat(Converter.convertToEnum("value_two", TestEnum.class)).isEqualTo(TestEnum.VALUE_TWO);
    }

    @Test
    void convertToEnum_shouldReturnNullForInvalidValue() {
        assertThat(Converter.convertToEnum("INVALID_VALUE", TestEnum.class)).isNull();
    }

    @Test
    void convertToBoolean_shouldReturnTrueForTrueBoolean() {
        assertThat(Converter.convertToBoolean(true)).isTrue();
    }

    @Test
    void convertToBoolean_shouldReturnFalseForFalseBoolean() {
        assertThat(Converter.convertToBoolean(false)).isFalse();
    }

    @Test
    void convertToBoolean_shouldReturnFalseForNullBoolean() {
        assertThat(Converter.convertToBoolean(null)).isFalse();
    }
}