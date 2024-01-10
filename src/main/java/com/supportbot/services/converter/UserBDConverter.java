package com.supportbot.services.converter;

import com.supportbot.DTO.api.other.UserResponse;
import com.supportbot.DTO.types.Gender;
import com.supportbot.model.documents.docdata.PersonData;
import com.supportbot.model.user.UserBD;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserBDConverter {

    public UserResponse convertToResponse(UserBD entity) {
        return null;
    }

    public UserBD updateEntity(UserResponse response, UserBD entityBD) {
        if (response == null || entityBD == null) {
            return entityBD;
        }

        entityBD.setSyncData(response.getGuid());

        if (entityBD.getPerson() == null) {
            entityBD.setPerson(new PersonData());
        }
        entityBD.getPerson().setGender(Converter.convertToEnum(response.getGender(), Gender.class));
        entityBD.setNotValid(Converter.convertToBoolean(response.getNotValid()));
//            entityBD.setIsEmployee(convertToBoolean(response.getIsEmployee()));
        entityBD.setIsMaster(Converter.convertToBoolean(response.getIsMaster()));
        entityBD.getPerson().setBirthday(Converter.convertToLocalDateTime(response.getBirthday()));
        updateUserFIO(response.getFio(), entityBD.getPerson());
        return entityBD;
    }

    public static void updateUserFIO(String fio, PersonData personFields) {
        if (fio == null || fio.isEmpty()) {
            return;
        }

        String[] strings = fio.replaceAll("[^a-zA-Zа-яА-ЯёЁ\s-]", "").split("\\s+");

        personFields.setLastName(strings[0]);
        if (strings.length > 1) {
            personFields.setFirstName(strings[1]);
        }
        if (strings.length > 2) {
            String fatherName = Arrays
                    .stream(strings, 2, strings.length)
                    .collect(Collectors.joining(" "));
            personFields.setFatherName(fatherName);
        }
    }
}
