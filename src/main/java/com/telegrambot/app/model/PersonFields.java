package com.telegrambot.app.model;

import com.telegrambot.app.DTO.Gender;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Embeddable
public class PersonFields {

    private String firstName;
    private String lastName;
    private String fatherName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDateTime birthday;
}
