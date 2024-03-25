package com.supportbot.model.documents.docdata;

import com.supportbot.DTO.types.Gender;
import com.supportbot.utils.TextUtils;
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

        return TextUtils.assembleString(firstName,
                lastName);
    }

    public String toStringFull() {

        String birthdayText = (birthday == null || birthday.getYear() < 1900) ?
                "" : String.format(", %s года рождения", birthday.format(TextUtils.DATE_FORMATTER));
        String genderText = (gender == null) ?
                "" : String.format(", пол %s", gender);

        return TextUtils.assembleString(firstName,
                fatherName,
                lastName,
                birthdayText,
                genderText);
    }
}
