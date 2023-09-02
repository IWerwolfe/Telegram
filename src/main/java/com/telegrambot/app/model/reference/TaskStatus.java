package com.telegrambot.app.model.reference;

import com.telegrambot.app.model.documents.docdata.SyncData;
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
    private static TaskStatus defaultClosedStatus;
    @Transient
    private static TaskStatus defaultInitialStatus;

    public TaskStatus(String guid) {
        this.setSyncData(new SyncData(guid));
    }

    public TaskStatus(String guid, String name) {
        this(guid);
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
