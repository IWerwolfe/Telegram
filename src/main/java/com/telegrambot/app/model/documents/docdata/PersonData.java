package com.telegrambot.app.model.documents.docdata;

import com.telegrambot.app.DTO.Gender;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class PersonData {

    private String firstName;
    private String lastName;
    private String fatherName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDateTime birthday;

    public PersonData(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
