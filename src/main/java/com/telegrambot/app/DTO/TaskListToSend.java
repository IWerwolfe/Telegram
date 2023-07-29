package com.telegrambot.app.DTO;

import com.telegrambot.app.DTO.types.SortingTaskType;
import com.telegrambot.app.model.documents.doc.service.Task;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskListToSend {
    SortingTaskType sorting;
    List<Task> tasks;
}
