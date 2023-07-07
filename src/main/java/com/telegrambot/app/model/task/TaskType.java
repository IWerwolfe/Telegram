package com.telegrambot.app.model.task;

import com.telegrambot.app.model.EnumBD1C;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("task_types")
public class TaskType extends EnumBD1C {

    @Transient
    private static TaskType defaultType;

    public TaskType(String name) {
        setName(name);
    }

    public static TaskType getDefaultType() {
        return defaultType;
    }

    public static void setDefaultType(TaskType defaultType) {
        TaskType.defaultType = defaultType;
    }
}
