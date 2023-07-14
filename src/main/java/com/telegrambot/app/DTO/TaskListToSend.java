package com.telegrambot.app.DTO;

import com.telegrambot.app.model.task.Task;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskListToSend {
    SortingTaskType sorting;
    List<Task> tasks;
}
