package com.telegrambot.app.services;

import com.telegrambot.app.model.user.UserType;
import com.telegrambot.app.repositories.*;
import com.telegrambot.app.services.converter.*;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotCommandsImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatusRepository statusRepository;
    @Mock
    private LegalEntityRepository legalRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private CommandCacheRepository commandCacheRepository;
    @Mock
    private CommandRepository commandRepository;
    @Mock
    private DaDataService dataService;
    @Mock
    private API1CServicesImpl api1C;
    @Mock
    private DaDataService daDataService;
    @Mock
    private LegalEntityConverter legalConverter;
    @Mock
    private DepartmentConverter departmentConverter;
    @Mock
    private ContractConverter contractConverter;
    @Mock
    private UserBDConverter userConverter;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskConverter taskConverter;

    private final BotCommandsImpl botCommands;

    private static final Logger logger = LoggerFactory.getLogger(BotCommandsImplTest.class);

    public BotCommandsImplTest() {
        MockitoAnnotations.openMocks(this);
        this.botCommands = new BotCommandsImpl(
                taskRepository,
                userRepository,
                statusRepository,
                legalRepository,
                departmentRepository,
                contractRepository,
                commandCacheRepository,
                commandRepository,
                api1C,
                daDataService,

                legalConverter,
                departmentConverter,
                contractConverter,
                userConverter,
                taskConverter);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Проверка преобразования должности в статус пользователя")
    public void testGetUserTypeByPost() {
        checkStringType(getPostListOther(), UserType.USER);
        checkStringType(getPostListDirector(), UserType.DIRECTOR);
        checkStringType(getPostListAdmin(), UserType.ADMINISTRATOR);
    }

    private void checkStringType(List<String> strings, UserType result) {
        for (String string : strings) {
            Assert.assertEquals("Проверяем \"" + string + "\"", result, botCommands.getUserTypeByPost(string));
            logger.info("Строка \"{}\" успешно проверена", string);
        }
    }

    private List<String> getPostListDirector() {
        ArrayList<String> post = new ArrayList<>();
        post.add("Директор");
        post.add("ИП");
        post.add("ИП Гудков В.А.");
        post.add("иНдиВидуальНЫЙ ПреДприниМатель");
        post.add("ректор");
        post.add("Председатель Правления Снт  \"Фортуна\"");
        post.add("ПрезидентКомпании");
        post.add("генеральный директор компании");
        return post;
    }

    private List<String> getPostListAdmin() {
        ArrayList<String> post = new ArrayList<>();
        post.add("Бухгалтер-администратор");
        post.add("Главный Бухгалтер");
        return post;
    }

    private List<String> getPostListOther() {
        ArrayList<String> post = new ArrayList<>();
        post.add(null);
        post.add("Сотрудник");
        post.add("Непонятная должность");
        post.add("");
        post.add("Директoр с латинским символом");
        post.add("ИПподром");
        post.add("Админ");
        post.add("Иванов ИП");
        return post;
    }
}