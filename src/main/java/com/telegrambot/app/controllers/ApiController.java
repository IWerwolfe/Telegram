package com.telegrambot.app.controllers;

import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataListResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataResponse;
import com.telegrambot.app.services.api.ApiInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ApiController {

    private final ApiInService api;

    @GetMapping("/task")
    public ResponseEntity<TaskDataResponse> getTask(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getTask(guid));
    }

    @GetMapping("/tasks")
    public ResponseEntity<TaskDataListResponse> getTasks(@RequestParam("guidManager") String guidManager,
                                                         @RequestParam("guidCompany") String guidCompany,
                                                         @RequestParam("guidDepartment") String guidDepartment) {
        return ResponseEntity.ok(api.getTasks(guidManager, guidCompany, guidDepartment));
    }
}
