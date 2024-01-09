package com.telegrambot.app.services.api;

import com.telegrambot.app.client.ApiClient;
import com.telegrambot.app.config.Connector1C;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApiClientTest {


    @Mock
    private Connector1C connector1C;

    @InjectMocks
    private ApiClient apiOutService;


}