package com.supportbot.DTO;

import com.supportbot.DTO.types.SortingTaskType;
import com.supportbot.model.documents.doc.service.TaskDoc;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskListToSend {
    SortingTaskType sorting;
    List<TaskDoc> taskDocs;
}
