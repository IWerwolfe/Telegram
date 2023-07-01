package com.telegrambot.app.model.task;

import com.telegrambot.app.model.EntityBD_1C;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("managers")
public class Manager extends EntityBD_1C {
}
