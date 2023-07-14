package com.telegrambot.app.model.task;

import com.telegrambot.app.model.EntityBD_1C;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("task_statuses")
@Slf4j
public class TaskStatus extends EntityBD_1C {

    @Transient
    private static TaskStatus defaultClosedStatus;
    @Transient
    private static TaskStatus defaultInitialStatus;

    public TaskStatus(String guid) {
        setGuid(guid);
    }

    public TaskStatus(String guid, String name) {
        setGuid(guid);
        setName(name);
    }

    public static TaskStatus getDefaultClosedStatus() {
        return defaultClosedStatus;
    }

    public static void setDefaultClosedStatus(TaskStatus defaultClosedStatus) {
        TaskStatus.defaultClosedStatus = defaultClosedStatus;
    }

    public static TaskStatus getDefaultInitialStatus() {
        return defaultInitialStatus;
    }

    public static void setDefaultInitialStatus(TaskStatus defaultInitialStatus) {
        TaskStatus.defaultInitialStatus = defaultInitialStatus;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
