package com.telegrambot.app.model;

import com.telegrambot.app.DTO.Gender;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Embeddable
@NoArgsConstructor
public class PersonFields {

    private String firstName;
    private String lastName;
    private String fatherName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDateTime birthday;

    public PersonFields(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
