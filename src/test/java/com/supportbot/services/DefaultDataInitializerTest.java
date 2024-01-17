package com.supportbot.services;

import com.supportbot.DTO.api.other.DefaultDataResponse;
import com.supportbot.DTO.api.typeObjects.EntityResponse;
import com.supportbot.client.ApiClient;
import com.supportbot.config.Connector1C;
import com.supportbot.model.DefaultDocParam;
import com.supportbot.model.EntityDefaults;
import com.supportbot.repositories.BankAccountRepository;
import com.supportbot.repositories.CashDeskKkmRepository;
import com.supportbot.repositories.CashDeskRepository;
import com.supportbot.repositories.DefaultDocParamRepository;
import com.supportbot.repositories.command.CompanyRepository;
import com.supportbot.repositories.reference.DivisionRepository;
import com.supportbot.repositories.reference.PayTerminalRepository;
import com.supportbot.repositories.reference.TaskStatusRepository;
import com.supportbot.repositories.reference.TaskTypeRepository;
import com.supportbot.services.converter.*;
import com.supportbot.tools.FileReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultDataInitializerTest {

    @Mock
    private ApiClient api1C;

    @Mock
    private DefaultDocParamRepository defaultDocParamRepository;

    @Mock
    private TaskTypeRepository typeRepository;

    @Mock
    private TaskStatusRepository statusRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private DivisionRepository divisionRepository;

    @Mock
    private PayTerminalRepository payTerminalRepository;

    @Mock
    private CashDeskKkmRepository cashDeskKkmRepository;

    @Mock
    private CashDeskRepository cashDeskRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private TaskStatusConverter statusConverter;

    @InjectMocks
    private TaskTypeConverter typeConverter;

    @InjectMocks
    private ManagerConverter managerConverter;

    @InjectMocks
    private CompanyConverter companyConverter;

    @InjectMocks
    private DivisionConverter divisionConverter;

    @InjectMocks
    private BankConverter bankConverter;

    @InjectMocks
    private BankAccountConverter bankAccountConverter;

    @InjectMocks
    private PayTerminalConverter payTerminalConverter;

    @InjectMocks
    private CashDeskKkmConverter cashDeskKkmConverter;

    @InjectMocks
    private CashDeskConverter cashDeskConverter;

    @InjectMocks
    private Connector1C connector1C;

    @InjectMocks
    private EntityDefaults entityDefaults;

    @Mock
    private DefaultDataInitializer initializer;


    private final String DEFAULT_NAME_CLOSED = "Завершена";
    private final String DEFAULT_NAME_INIT = "Ожидает обработки";
    private final String DEFAULT_NAME_TYPE = "Обращение через чат бот";

    String filePath;

    @Test
    void testRunWhenApi1CReturnsData() throws Exception {

        filePath = "src/test/resource/jsonResponse/defaultData/DefaultData_correct.json";

        DefaultDataResponse mockData = createMockData(filePath);
        when(api1C.getDefaultData()).thenReturn(mockData);


        initializer.run();

        DefaultDocParam defaultDocParam = entityDefaults.getDefaultDocParam();

        assertNotNull(defaultDocParam);

        // Assert
        // Add assertions to verify that the convertData method was called
//        verify(initializer, times(1)).convertData(eq(mockData));
//        verify(initializer, never()).initDefault();
    }

    @Test
    void testRunWhenApi1CReturnsNull() throws Exception {
        // Arrange
        when(api1C.getDefaultData()).thenReturn(null);

        // Act
        initializer.run();


        DefaultDocParam defaultDocParam = entityDefaults.getDefaultDocParam();

        assertNotNull(defaultDocParam);


        // Assert
        // Add assertions to verify that the initDefault method was called
//        verify(initializer, times(1)).initDefault();
//        verify(initializer, never()).convertData(any());
    }

    @Test
    void testRunWhenApi1CThrowsException() throws Exception {
        // Arrange
        when(api1C.getDefaultData()).thenThrow(new RuntimeException("Simulating API failure"));

        // Act
        initializer.run();

        // Assert
        // Add assertions to verify that the initDefault method was called
//        verify(initializer, times(1)).initDefault();
//        verify(initializer, never()).convertData(any());
    }

    // Helper method to create a mock DefaultDataResponse
    private DefaultDataResponse createMockData(String filePath) {
        return EntityResponse.createToJson(FileReader.readJsonFile(filePath), DefaultDataResponse.class);
    }
}