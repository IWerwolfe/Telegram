package com.telegrambot.app.model.task;

import com.telegrambot.app.model.EntityBD_1C;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("task_statuses")
public class TaskStatus extends EntityBD_1C {
}
