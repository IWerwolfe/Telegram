package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.other.UserResponse;
import com.telegrambot.app.DTO.types.Gender;
import com.telegrambot.app.model.documents.docdata.PersonData;
import com.telegrambot.app.model.user.UserBD;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserBDConverterTest {


    @InjectMocks
    private UserBDConverter userConverter;

    @Test
    void updateEntity_shouldUpdateEntityCorrectly() {
        UserResponse response = new UserResponse();
        response.setGuid("123456");
        response.setGender("MALE");
        response.setNotValid(true);
        response.setIsMaster(false);
        response.setBirthday(new Date());
        response.setFio("Last First Father");

        UserBD entityBD = new UserBD();

        userConverter.updateEntity(response, entityBD);

        assertNotNull(entityBD.getSyncData());
        assertEquals(entityBD.getSyncData().getGuid(), "123456");
        assertNotNull(entityBD.getSyncData().getLastUpdate());

        assertNotNull(entityBD.getPerson());
        assertEquals(entityBD.getPerson().getGender(), Gender.MALE);
        assertTrue(entityBD.getNotValid());
        assertFalse(entityBD.getIsMaster());
        assertNotNull(entityBD.getPerson().getBirthday());
    }

    @Test
    void updateEntity_shouldHandleNullPerson() {
        UserResponse response = new UserResponse();
        response.setGuid("123456");
        response.setGender("MALE");

        UserBD entityBD = new UserBD();

        userConverter.updateEntity(response, entityBD);

        assertNotNull(entityBD.getPerson());
        assertEquals(entityBD.getPerson().getGender(), Gender.MALE);
    }

    @Test
    void updateEntity_shouldHandleNullResponse() {

        UserResponse response = null;

        UserBD entityBD = new UserBD();
        entityBD.setPhone("1234");
        entityBD.setIsBot(true);

        userConverter.updateEntity(response, entityBD);

        assertNotNull(entityBD);
        assertEquals(entityBD.getPhone(), "1234");
        assertTrue(entityBD.getIsBot());
    }

    @Test
    void updateEntity_shouldHandleNullEntityBD() {
        UserResponse response = new UserResponse();
        response.setGuid("123456");
        response.setGender("MALE");

        UserBD entityBD = null;

        userConverter.updateEntity(response, entityBD);

        assertNull(entityBD);
    }

    @Test
    void updateUserFIO_shouldNotUpdatePersonFieldsForNullOrEmptyString() {
        PersonData personFields = new PersonData();

        UserBDConverter.updateUserFIO("", personFields);
        personDataIsEmpty(personFields);

        UserBDConverter.updateUserFIO(null, personFields);
        personDataIsEmpty(personFields);
    }

    @Test
    void updateUserFIO_shouldUpdatePersonFieldsCorrectlyForSingleWord() {
        PersonData personFields = new PersonData();

        UserBDConverter.updateUserFIO("LastName", personFields);
        assertEquals(personFields.getLastName(), "LastName");

        assertNotNull(personFields.getFirstName());
        assertNotNull(personFields.getFatherName());

        assertEquals(personFields.getFirstName(), "");
        assertEquals(personFields.getFatherName(), "");
    }

    @Test
    void updateUserFIO_shouldUpdatePersonFieldsCorrectlyForTwoWords() {
        PersonData personFields = new PersonData();

        UserBDConverter.updateUserFIO("LastName FirstName", personFields);
        assertEquals(personFields.getLastName(), "LastName");
        assertEquals(personFields.getFirstName(), "FirstName");

        assertNotNull(personFields.getFatherName());
        assertEquals(personFields.getFatherName(), "");
    }

    @Test
    void updateUserFIO_shouldUpdatePersonFieldsCorrectlyForMoreThanTwoWords() {
        PersonData personFields = new PersonData();

        UserBDConverter.updateUserFIO("LastName FirstName FatherName", personFields);
        assertEquals(personFields.getLastName(), "LastName");
        assertEquals(personFields.getFirstName(), "FirstName");
        assertEquals(personFields.getFatherName(), "FatherName");
    }

    @Test
    void updateUserFIO_shouldUpdatePersonFieldsCorrectlyForMoreThanThreeWords() {
        PersonData personFields = new PersonData();

        UserBDConverter.updateUserFIO("LastName FirstName FatherName FatherNameOgly FatherNameFather", personFields);
        assertEquals(personFields.getLastName(), "LastName");
        assertEquals(personFields.getFirstName(), "FirstName");
        assertEquals(personFields.getFatherName(), "FatherName FatherNameOgly FatherNameFather");
    }

    @Test
    void updateUserFIO_shouldUpdatePersonFieldsCorrectlyForMoreThanThreeWordsForMoreSpace() {
        PersonData personFields = new PersonData();

        UserBDConverter.updateUserFIO("LastName   FirstName  FatherName FatherNameOgly FatherNameFather", personFields);
        assertEquals(personFields.getLastName(), "LastName");
        assertEquals(personFields.getFirstName(), "FirstName");
        assertEquals(personFields.getFatherName(), "FatherName FatherNameOgly FatherNameFather");
    }

    @Test
    void updateUserFIO_shouldUpdatePersonFieldsCorrectlyForMoreThanThreeWordsForUnWordSymbol() {
        PersonData personFields = new PersonData();

        UserBDConverter.updateUserFIO("LastName, Firs+tName  FatherName FatherNameOgly; FatherNameFather", personFields);
        assertEquals(personFields.getLastName(), "LastName");
        assertEquals(personFields.getFirstName(), "FirstName");
        assertEquals(personFields.getFatherName(), "FatherName FatherNameOgly FatherNameFather");
    }

    @Test
    void updateUserFIO_shouldUpdatePersonFieldsCorrectlyForDoubleLastName() {
        PersonData personFields = new PersonData();

        UserBDConverter.updateUserFIO("LastName-LastName FirstName FatherName", personFields);
        assertEquals(personFields.getLastName(), "LastName-LastName");
        assertEquals(personFields.getFirstName(), "FirstName");
        assertEquals(personFields.getFatherName(), "FatherName");
    }

    @Test
    void updateUserFIO_shouldUpdatePersonFieldsCorrectlyForRussian() {
        PersonData personFields = new PersonData();

        UserBDConverter.updateUserFIO("Воробьёв Иван Ахмедов оглы Рахманович", personFields);
        assertEquals(personFields.getLastName(), "Воробьёв");
        assertEquals(personFields.getFirstName(), "Иван");
        assertEquals(personFields.getFatherName(), "Ахмедов оглы Рахманович");
    }

    private static void personDataIsEmpty(PersonData personFields) {
        assertNotNull(personFields.getLastName());
        assertNotNull(personFields.getFirstName());
        assertNotNull(personFields.getFatherName());

        assertEquals(personFields.getLastName(), "");
        assertEquals(personFields.getFirstName(), "");
        assertEquals(personFields.getFatherName(), "");
    }
}