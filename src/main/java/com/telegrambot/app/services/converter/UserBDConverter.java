package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.UserDataResponse;
import com.telegrambot.app.DTO.api_1C.typesОbjects.Entity1C;
import com.telegrambot.app.DTO.types.Gender;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.documents.docdata.PersonData;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserBDConverter extends Converter1C {

    private final UserRepository userRepository;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof UserDataResponse response && entity instanceof UserBD entityBD) {
            if (entityBD.getPerson() == null) {
                entityBD.setPerson(new PersonData());
            }
            entityBD.setSyncData(new SyncData(response.getGuid(), response.getCode()));
            entityBD.getPerson().setGender(convertToEnum(response.getGender(), Gender.class));
            entityBD.setNotValid(convertToBoolean(response.getNotValid()));
//            entityBD.setIsEmployee(convertToBoolean(response.getIsEmployee()));
            entityBD.setIsMaster(convertToBoolean(response.getIsMaster()));
            entityBD.getPerson().setBirthday(convertToLocalDateTime(response.getBirthday()));
            updateUserFIO(response.getFio(), entityBD.getPerson());
            return (T) entityBD;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, userRepository, UserBD.class);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, userRepository, UserBD.class, isSaved);
    }

    public static void updateUserFIO(String fio, PersonData personFields) {
        if (fio == null || fio.isEmpty()) {
            return;
        }
        String[] strings = fio.split("\\s+");

        if (strings.length > 0) {
            personFields.setLastName(strings[0]);
        }
        if (strings.length > 1) {
            personFields.setFirstName(strings[1]);
        }
        if (strings.length > 2) {
            String fatherName = Arrays.stream(strings, 2, strings.length)
                    .collect(Collectors.joining(" "));
            personFields.setFatherName(fatherName);
        }
    }
}
