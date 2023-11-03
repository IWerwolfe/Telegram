package com.telegrambot.app.model.reference;

import com.telegrambot.app.model.types.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task_statuses")
@Slf4j
public class TaskStatus extends Reference {

    @Transient
    private static TaskStatus closedStatus;
    @Transient
    private static TaskStatus initialStatus;

    public TaskStatus(String guid) {
        super(guid);
    }

    public TaskStatus(String guid, String name) {
        this(guid);
        setName(name);
    }

    public static TaskStatus getClosedStatus() {
        return closedStatus;
    }

    public static void setClosedStatus(TaskStatus closedStatus) {
        TaskStatus.closedStatus = closedStatus;
    }

    public static TaskStatus getInitialStatus() {
        return initialStatus;
    }

    public static void setInitialStatus(TaskStatus initialStatus) {
        TaskStatus.initialStatus = initialStatus;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
