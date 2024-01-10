package com.supportbot.DTO.types;

import com.supportbot.model.documents.docdata.SyncData;
import com.supportbot.model.types.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task_types")
public class TaskType extends Reference {

    @Transient
    private static TaskType defaultType;

    public TaskType(String guid) {
        this.setSyncData(new SyncData(guid));
    }

    public TaskType(String guid, String name) {
        this(guid);
        setName(name);
    }

    public static TaskType getDefaultType() {
        return defaultType;
    }

    public static void setDefaultType(TaskType defaultType) {
        TaskType.defaultType = defaultType;
    }
}
