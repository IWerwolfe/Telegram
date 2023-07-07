package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.Gender;
import com.telegrambot.app.DTO.api_1C.UserDataResponse;
import com.telegrambot.app.model.PersonFields;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserBDConverter extends Request1CConverter {

    private final UserRepository userRepository;

    @Override
    public <T, R> T updateEntity(R dto, T entity) {
        if (dto instanceof UserDataResponse response && entity instanceof UserBD entityBD) {
            if (entityBD.getPerson() == null) {
                entityBD.setPerson(new PersonFields());
            }
            entityBD.setGuid(response.getGuid());
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
    public <T, R> T getOrCreateEntity(R dto) {
        if (dto instanceof UserDataResponse response) {
            Optional<UserBD> existingEntity = userRepository.findByPhoneIgnoreCase(response.getPhone());
            return (T) existingEntity.orElseGet(() -> new UserBD(response.getPhone()));
        }
        return null;
    }

    public static void updateUserFIO(String fio, PersonFields personFields) {
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
