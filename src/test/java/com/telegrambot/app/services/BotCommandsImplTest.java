package com.telegrambot.app.services;

import org.springframework.stereotype.Component;

@Component
public class BotCommandsImplTest {
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private UserStatusRepository statusRepository;
//    @Mock
//    private LegalEntityRepository legalRepository;
//    @Mock
//    private DepartmentRepository departmentRepository;
//    @Mock
//    private ContractRepository contractRepository;
//    @Mock
//    private CommandCacheRepository commandCacheRepository;
//    @Mock
//    private CommandRepository commandRepository;
//    @Mock
//    private DaDataClient dataService;
//    @Mock
//    private ApiClient api1C;
//    @Mock
//    private DaDataClient daDataService;
//    @Mock
//    private PartnerConverter legalConverter;
//    @Mock
//    private DepartmentConverter departmentConverter;
//    @Mock
//    private ContractConverter contractConverter;
//    @Mock
//    private UserBDConverter userConverter;
//    @Mock
//    private TaskDocRepository taskRepository;
//    @Mock
//    private TaskDocConverter taskConverter;
//    @Mock
//    private CardDocRepository cardDocRepository;
//    @Mock
//    private BotConfig bot;
//    @Mock
//    private ToStringServices toStringServices;
//    private final BotCommandsImpl botCommands;
//
//
//    private static final Logger logger = LoggerFactory.getLogger(BotCommandsImplTest.class);
//
//    public BotCommandsImplTest() {
//        MockitoAnnotations.openMocks(this);
//        this.botCommands = new BotCommandsImpl(
//                cardDocRepository,
//                taskRepository,
//                userRepository,
//                statusRepository,
//                legalRepository,
//                departmentRepository,
//                contractRepository,
//                commandCacheRepository,
//                commandRepository,
//                api1C,
//                daDataService,
//                legalConverter,
//                departmentConverter,
//                contractConverter,
//                userConverter,
//                taskConverter,
//                toStringServices,
//                bot
//        );
//    }
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("Проверка преобразования должности в статус пользователя")
//    public void testGetUserTypeByPost() {
//        checkStringType(getPostListOther(), UserType.USER);
//        checkStringType(getPostListDirector(), UserType.DIRECTOR);
//        checkStringType(getPostListAdmin(), UserType.ADMINISTRATOR);
//    }
//
//    private void checkStringType(List<String> strings, UserType result) {
//        for (String string : strings) {
//            Assert.assertEquals("Проверяем \"" + string + "\"", result, botCommands.getUserTypeByPost(string));
//            logger.info("Строка \"{}\" успешно проверена", string);
//        }
//    }
//
//    private List<String> getPostListDirector() {
//        ArrayList<String> post = new ArrayList<>();
//        post.add("Директор");
//        post.add("ИП");
//        post.add("ИП Гудков В.А.");
//        post.add("иНдиВидуальНЫЙ ПреДприниМатель");
//        post.add("ректор");
//        post.add("Председатель Правления Снт  \"Фортуна\"");
//        post.add("ПрезидентКомпании");
//        post.add("генеральный директор компании");
//        return post;
//    }
//
//    private List<String> getPostListAdmin() {
//        ArrayList<String> post = new ArrayList<>();
//        post.add("Бухгалтер-администратор");
//        post.add("Главный Бухгалтер");
//        return post;
//    }
//
//    private List<String> getPostListOther() {
//        ArrayList<String> post = new ArrayList<>();
//        post.add(null);
//        post.add("Сотрудник");
//        post.add("Непонятная должность");
//        post.add("");
//        post.add("Директoр с латинским символом");
//        post.add("ИПподром");
//        post.add("Админ");
//        post.add("Иванов ИП");
//        return post;
//    }
}