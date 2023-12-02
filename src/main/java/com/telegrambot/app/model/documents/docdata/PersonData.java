package com.telegrambot.app.model.documents.docdata;

import com.telegrambot.app.DTO.types.Gender;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class PersonData {

    @JoinColumn(name = "first_name")
    private String firstName = "";
    @JoinColumn(name = "last_name")
    private String lastName = "";
    @JoinColumn(name = "father_name")
    private String fatherName = "";
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDateTime birthday;

    public PersonData(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            string.append(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            string.append(" ")
                    .append(lastName);
        }
        if (fatherName != null && !fatherName.isEmpty()) {
            string.append(" ")
                    .append(fatherName);
        }
        return string.toString();
    }

    public String toStringFull() {
        StringBuilder string = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            string.append(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            string.append(" ")
                    .append(lastName);
        }
        if (fatherName != null && !fatherName.isEmpty()) {
            string.append(" ")
                    .append(fatherName);
        }
        if (birthday != null) {
            string.append(", ")
                    .append(birthday)
                    .append(" года рождения");
        }
        if (gender != null) {
            string.append(", ")
                    .append("пол ")
                    .append(gender.getLabel());
        }
        return string.toString();
    }
}
