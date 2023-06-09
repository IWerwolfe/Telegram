package com.telegrambot.app.services;

import com.telegrambot.app.DTO.Company;
import com.telegrambot.app.DTO.Task;
import com.telegrambot.app.DTO.UserData;
import com.telegrambot.app.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class API1CServicesImpl implements API1CServices {

    @Override
    public Company getCompany(String inn, String kpp) {
        return null;
    }

    @Override
    public UserData getUserData(String phone) {
        return null;
    }

    @Override
    public Task getTask(String id) {
        return null;
    }

    @Override
    public List<Task> getTaskList(String inn) {
        return null;
    }

    @Override
    public List<Task> getTaskList(User user) {
        return null;
    }
}
