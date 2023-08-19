package com.telegrambot.app.services.api;

import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataListResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskResponse;
import com.telegrambot.app.model.documents.doc.service.Task;
import com.telegrambot.app.repositories.TaskRepository;
import com.telegrambot.app.services.converter.TaskConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiInServiceImpl implements ApiInService {

    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;

    @Override
    public TaskDataResponse getTask(String guid) {
        Optional<Task> optional = taskRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        TaskDataResponse dataResponse = new TaskDataResponse();
        dataResponse.setResult(optional.isPresent());
        dataResponse.setTask(optional.<TaskResponse>map(taskConverter::convertToResponse).orElse(null));
        dataResponse.setError(optional.isEmpty() ? "Data not found" : "");
        return dataResponse;
    }

    @Override
    public TaskDataListResponse getTasks(String guidManager, String guidCompany, String guidDepartment) {

        if ()
            return null;
    }
}
