package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.UserResponse;
import com.telegrambot.app.DTO.types.Gender;
import com.telegrambot.app.model.documents.docdata.PersonData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserBDConverter {

    public UserResponse convertToResponse(com.telegrambot.app.model.user.UserBD entity) {
        return null;
    }

    public com.telegrambot.app.model.user.UserBD updateEntity(UserResponse response, com.telegrambot.app.model.user.UserBD entityBD) {
        if (entityBD.getPerson() == null) {
            entityBD.setPerson(new PersonData());
        }
        entityBD.getPerson().setGender(Converter1C.convertToEnum(response.getGender(), Gender.class));
        entityBD.setNotValid(Converter1C.convertToBoolean(response.getNotValid()));
//            entityBD.setIsEmployee(convertToBoolean(response.getIsEmployee()));
        entityBD.setIsMaster(Converter1C.convertToBoolean(response.getIsMaster()));
        entityBD.getPerson().setBirthday(Converter1C.convertToLocalDateTime(response.getBirthday()));
        updateUserFIO(response.getFio(), entityBD.getPerson());
        return entityBD;
    }

    public static void updateUserFIO(String fio, PersonData personFields) {
        if (fio == null || fio.isEmpty()) {
            return;
        }
        String[] strings = fio.split("\\s+");
        switch (strings.length) {
            case 0:
                return;
            case 1:
                personFields.setLastName(strings[0]);
            case 2:
                personFields.setFirstName(strings[1]);
            default: {
                if (strings.length < 3) {
                    return;
                }
                String fatherName = Arrays
                        .stream(strings, 2, strings.length)
                        .collect(Collectors.joining(" "));
                personFields.setFatherName(fatherName);
            }
        }
    }
}
