package com.telegrambot.app.services;

import com.telegrambot.app.DTO.Company;
import com.telegrambot.app.DTO.Task;
import com.telegrambot.app.DTO.UserData;
import com.telegrambot.app.model.User;

import java.util.List;

public interface API1CServices {

    Company getCompany(String inn, String kpp);

    UserData getUserData(String phone);

    Task getTask(String id);

    List<Task> getTaskList(String inn);

    List<Task> getTaskList(User user);
}
