package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.convector.TestDocConvector;
import com.telegrambot.app.convector.TestRefConvector;
import com.telegrambot.app.mockResponse.testDoc.TestDocResponse;
import com.telegrambot.app.mockResponse.testDoc.TestRefResponse;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.model.reference.legalentity.*;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.moskModel.TestDoc;
import com.telegrambot.app.moskModel.TestRef;
import com.telegrambot.app.repository.TestDocRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@PrepareForTest(Converter.class)
class ConverterTest {

    @Mock
    private TestDocRepository repository;

    @InjectMocks
    private TestDocConvector converter;

    @InjectMocks
    private TestRefConvector refConvector;

    Date date = Date.from(Instant.now());
    LocalDateTime dateTime = Converter.convertToLocalDateTime(date);

    private static List<TestDocResponse> getResponses() {
        TestDocResponse response = new TestDocResponse();
        TestDocResponse response1 = new TestDocResponse();

        response.setGuid("testGuid");
        response1.setGuid("testGuid1");

        return List.of(response, response1);
    }


    private TestDoc createFillTestDoc() {
        TestDoc doc = new TestDoc("testGuid");
        doc.setDate(dateTime);
        doc.setComment(doc.getComment());
        doc.setCompany(new Company("guidCompany"));
        doc.setDivision(new Division("guidDivision"));
        doc.setManager(new Manager("guidManager"));
        doc.setTotalAmount(123);
        doc.setMarkedForDel(true);
        doc.setAuthor("author");

        Partner partner = new Partner("guidPartner");
        Department department = new Department("guidDepartment");
        Contract contract = new Contract("guidContract");
        PartnerData partnerData = new PartnerData(partner, department, contract);
        doc.setPartnerData(partnerData);
        return doc;
    }

    @Test
    void convertToInteger_NullString_ReturnsZero() {
        String sum = null;
        Integer result = Converter.convertToInteger(sum);
        assertEquals(0, result);
    }

    @Test
    void convertToInteger_EmptyString_ReturnsZero() {
        String sum = "";
        Integer result = Converter.convertToInteger(sum);
        assertEquals(0, result);
    }

    @Test
    void convertToInteger_ValidString_ReturnsParsedInteger() {
        String sum = "123";
        Integer result = Converter.convertToInteger(sum);
        assertEquals(123, result);
    }

    @Test
    void convertToInteger_InvalidString_ReturnsZero() {
        String sum = "abc";
        Integer result = Converter.convertToInteger(sum);
        assertEquals(0, result);
    }

    @Test
    void convertDocToResponse_ValidDocument_ReturnsEntityDocResponseWithCorrectValues() {

        TestDoc doc = createFillTestDoc();

        TestDocResponse result = converter.convertDocToResponse(doc, TestDocResponse.class);

        assertNotNull(result);
        assertEquals(result.getDate(), date);
        assertEquals(doc.getComment(), result.getComment());
        assertEquals(doc.getCompany().getGuidEntity(), result.getGuidCompany());
        assertEquals(doc.getDivision().getGuidEntity(), result.getGuidDivision());
        assertEquals(doc.getManager().getGuidEntity(), result.getGuidManager());
        assertEquals(doc.getTotalAmount().toString(), result.getTotalAmount());
        assertEquals(doc.getMarkedForDel(), result.getMarkedForDel());
        assertEquals(doc.getAuthor(), result.getGuidAuthor());
        assertEquals(doc.getPartnerData().getPartner().getGuidEntity(), result.getGuidPartner());
        assertEquals(doc.getPartnerData().getDepartment().getGuidEntity(), result.getGuidDepartment());
        assertEquals(doc.getPartnerData().getContract().getGuidEntity(), result.getGuidContract());
    }

    @Test
    void fillResponseToDoc_ShouldFillDocumentWithResponseValues() {

        TestDoc testDoc = new TestDoc();
        TestDocResponse testResponse = new TestDocResponse();
        testResponse.setDate(date);
        testResponse.setComment("Test Comment");
        testResponse.setTotalAmount("42");
        testResponse.setMarkedForDel(true);
        testResponse.setGuidAuthor("testAuthor");

        converter.fillResponseToDoc(testDoc, testResponse);

        assertEquals(dateTime, testDoc.getDate());
        assertEquals(42, testDoc.getTotalAmount());
        assertEquals("Test Comment", testDoc.getComment());
        assertTrue(testDoc.getMarkedForDel());
        assertEquals("testAuthor", testDoc.getAuthor());
    }

    @Test
    void convertReferenceToResponse_shouldConvertReferenceToResponse() {

        TestRef testRef = new TestRef("TestGuid");
        testRef.setName("TestName");
        testRef.setMarkedForDel(false);

        TestRefResponse result = refConvector.convertToResponse(testRef);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("TestName");
        assertThat(result.getGuid()).isEqualTo("TestGuid");
        assertThat(result.getMarkedForDel()).isFalse();
    }

    @Test
    void convertReferenceToResponse_shouldHandleNullReferenceEntity() {
        TestRefResponse result = converter.convertReferenceToResponse(null, TestRefResponse.class);
        assertThat(result).isNotNull();
    }

    @Test
    void convertToEntity_shouldConvertResponseToEntityAndUpdate() {

        TestDocResponse response = new TestDocResponse();
        response.setGuid("testGuid");
        response.setDate(date);
        response.setCode("001");

        TestDoc existingEntity = new TestDoc("testGuid");
        existingEntity.getSyncData().setCode("001");
        existingEntity.setDate(dateTime);

//        when(repository.findBySyncDataNotNullAndSyncData_Guid("testGuid")).thenReturn(Optional.of(existingEntity));
        when(repository.save(any(TestDoc.class))).thenReturn(new TestDoc("testGuid"));

        TestDoc result = converter.convertToEntity(response);

        assertThat(result).isNotNull();
        assertThat(result.getGuidEntity()).isEqualTo("testGuid");
        assertThat(result.getDate()).isEqualTo(dateTime);

//        verify(converter, times(1)).convertToEntity(any());
//        verify(repository, times(1)).save(any());
//        verify(repository, times(1)).findBySyncDataNotNullAndSyncData_Guid(any());
    }

    @Test
    void convertToEntityList_withNullList_shouldReturnEmptyList() {
        List<TestDocResponse> inputList = null;
        List<TestDoc> result = converter.convertToEntityList(inputList, converter);
        assertThat(result).isEmpty();
    }

    @Test
    void convertToEntityList_withEmptyList_shouldReturnEmptyList() {
        List<TestDocResponse> inputList = new ArrayList<>();
        List<TestDoc> result = converter.convertToEntityList(inputList, converter);
        assertThat(result).isEmpty();
    }

    @Test
    void convertToEntityList_withNonEmptyList_shouldReturnEntityList() {

        when(repository.save(any(TestDoc.class))).thenReturn(new TestDoc("testGuid"));
        List<TestDoc> result = converter.convertToEntityList(getResponses(), converter);

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isInstanceOf(TestDoc.class);
        assertThat(result.get(1)).isInstanceOf(TestDoc.class);
    }

    @Test
    void convertToEntityListIsSave_withNullList_shouldReturnEmptyList() {
        List<TestDocResponse> inputList = null;

        List<TestDoc> result = converter.convertToEntityListIsSave(inputList, converter, repository);

        assertThat(result).isEmpty();
        verify(repository, never()).saveAllAndFlush(any());
    }

    @Test
    void convertToEntityListIsSave_withEmptyList_shouldReturnEmptyList() {
        List<TestDocResponse> inputList = new ArrayList<>();

        List<TestDoc> result = converter.convertToEntityListIsSave(inputList, converter, repository);

        assertThat(result).isEmpty();
        verify(repository, never()).saveAllAndFlush(any());
    }

    @Test
    void convertToEntityListIsSave_withNonEmptyList_shouldReturnEntityListAndSaveInRepository() {

        List<TestDoc> entityList = List.of(new TestDoc(), new TestDoc());
        List<TestDocResponse> responseList = getResponses();

        when(repository.saveAllAndFlush(anyList())).thenReturn(entityList);

        List<TestDoc> result = Converter.convertToEntityListIsSave(responseList, converter, repository);

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isInstanceOf(TestDoc.class);
        assertThat(result.get(1)).isInstanceOf(TestDoc.class);
        verify(repository, times(1)).saveAllAndFlush(any());
    }

    @Test
    void getOrCreateEntity_withNonNullGuidAndEntityExists_shouldReturnExistingEntity() {
        String guid = "testGuid";
        TestDoc existingEntity = new TestDoc(guid);
        when(repository.findBySyncDataNotNullAndSyncData_Guid(guid)).thenReturn(Optional.of(existingEntity));

        TestDoc result = Converter.getOrCreateEntity(guid, repository, TestDoc.class);

        assertThat(result).isEqualTo(existingEntity);
        verify(repository, never()).save(any());
    }

    @Test
    void getOrCreateEntity_withNonNullGuidAndEntityDoesNotExist_shouldCreateAndSaveNewEntity() {
        String guid = "testGuid";
        TestDoc result;

        when(repository.findBySyncDataNotNullAndSyncData_Guid(guid)).thenReturn(Optional.empty());
        when(repository.save(any(TestDoc.class))).thenReturn(new TestDoc(guid));

        result = Converter.getOrCreateEntity(guid, repository, TestDoc.class, false);
        verify(repository, times(1)).findBySyncDataNotNullAndSyncData_Guid(guid);
        assertThat(result).isNotNull();

        result = Converter.getOrCreateEntity(guid, repository, TestDoc.class, true);

        assertThat(result).isNotNull();
        verify(repository, times(2)).findBySyncDataNotNullAndSyncData_Guid(guid);
        verify(repository, times(1)).save(any(TestDoc.class));

        assertThat(result.getGuidEntity()).isEqualTo(guid);
    }

    @Test
    void getOrCreateEntity_withNullGuid_shouldReturnNull() {
        EntityResponse response = null;
        TestDoc result = converter.getOrCreateEntity(response);

        assertThat(result).isNull();
        verify(repository, never()).save(any());
        verify(repository, never()).findBySyncDataNotNullAndSyncData_Guid(any());
    }

    @Test
    void getOrCreateEntity_withEmptyGuid_shouldReturnNull() {
        TestDoc result = converter.getOrCreateEntity("", true);

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
        Entity entity = new TestDoc();
        String result = Converter.convertToGuid(entity);
        assertThat(result).isEmpty();
    }

    @Test
    void convertToGuid_shouldReturnGuidFromSyncData() {
        String expectedGuid = "testGuid";
        Entity entity = new TestDoc(expectedGuid);
        String result = Converter.convertToGuid(entity);
        assertThat(result).isEqualTo(expectedGuid);
    }

    @Test
    void convertToGuid_shouldReturnEmptyStringForNullSyncDataGuid() {
        Entity entity = new TestDoc();
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