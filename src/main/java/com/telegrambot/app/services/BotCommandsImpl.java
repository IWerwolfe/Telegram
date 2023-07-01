package com.telegrambot.app.services;

import com.telegrambot.app.DTO.DepartmentResponse;
import com.telegrambot.app.DTO.LegalListData;
import com.telegrambot.app.DTO.SubCommandInfo;
import com.telegrambot.app.DTO.api_1C.CompanyDataResponse;
import com.telegrambot.app.DTO.api_1C.ContractResponse;
import com.telegrambot.app.DTO.api_1C.PartnerResponse;
import com.telegrambot.app.DTO.api_1C.UserDataResponse;
import com.telegrambot.app.DTO.dadata.DaDataParty;
import com.telegrambot.app.DTO.message.Message;
import com.telegrambot.app.components.Buttons;
import com.telegrambot.app.model.command.Command;
import com.telegrambot.app.model.command.CommandCache;
import com.telegrambot.app.model.command.CommandStatus;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.LegalEntity;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import com.telegrambot.app.model.user.UserType;
import com.telegrambot.app.repositories.*;
import com.telegrambot.app.services.converter.ContractConverter;
import com.telegrambot.app.services.converter.DepartmentConverter;
import com.telegrambot.app.services.converter.LegalEntityConverter;
import com.telegrambot.app.services.converter.UserBDConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotCommandsImpl implements com.telegrambot.app.components.BotCommands {

    private final UserRepository userRepository;
    private final UserStatusRepository statusRepository;
    private final LegalEntityRepository legalRepository;
    private final DepartmentRepository departmentRepository;
    private final ContractRepository contractRepository;
    private final CommandCacheRepository commandCacheRepository;
    private final CommandRepository commandRepository;
    private final API1CServicesImpl api1C;
    private final DaDataService daDataService;

    private final LegalEntityConverter legalConverter;
    private final DepartmentConverter departmentConverter;
    private final ContractConverter contractConverter;
    private final UserBDConverter userConverter;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
    private final String REGEX_INN = "^[0-9]{10}|[0-9]{12}$";
    private final String REGEX_FIO = "^(?:[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?\\s+){1,2}[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?$";
    private final String REGEX_PHONE = "^79[0-9]{9}$";
    private static final String SEPARATOR = System.lineSeparator();
    private TelegramBotServices parent;
    private List<CommandCache> commandCacheList = new ArrayList<>();
    private String text;
    private long chatId;
    private UserBD user;

    public void botAnswerUtils(String receivedMessage, long chatId, UserBD userBD) {

        this.chatId = chatId;
        this.user = userBD;

        switch (receivedMessage) {
            case "/start" -> startBot();
            case "/help" -> sendHelpText();
            case "/send_contact" -> getContact();
            case "/afterRegistered" -> afterRegistered();
            case "/registrationSurvey" -> registrationSurvey();
            case "/getUserByPhone" -> getUserByPhone();
            case "/getByInn" -> getByInn();
            case "/need_help" -> createAssistance();
            case "/exit" -> exit();
            default -> sendDefault(receivedMessage);
        }
        if (!commandCacheList.isEmpty()) {
            commandCacheRepository.saveAll(commandCacheList);
        }
    }

    private void createAssistance() {
        String message = Message.getMessageStartCreateAssistance(user.getPerson().getFirstName());
        CommandCache command = getCommandCache(message, "/need_help", "getTopic");
        switch (command.getSubCommand()) {
            case "getTopic" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getMessageStartTopic(),
                        this::createAssistance,
                        "getDescription");
                subCommandGetTextInfo(command, info);
            }
            case "getDescription" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getMessageStartDescription(),
                        Message.getMessageErrorDescription(),
                        this::createAssistance,
                        "getUserName");
                subCommandGetTextInfo(command, info);
            }
            case "getUserName" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getMessageStartName(),
                        Message.getMessageErrorName(),
                        this::createAssistance,
                        "getPhone");
                subCommandGetTextInfo(command, info);
            }
            case "getPhone" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getMessageStartGetPhone(),
                        Message.getMessageUnCorrectGetPhone(),
                        this::createAssistance,
                        "end",
                        REGEX_PHONE
                );
                subCommandGetTextInfo(command, info);
            }
            case "end" -> subCommandEnd();
            default -> sendDefault("");
        }
    }

    public void botAnswerUtils(List<CommandCache> commandCacheList, String text, long chatId, UserBD userBD) {
        this.commandCacheList = commandCacheList;
        this.text = text;

        if (text.equals("/exit")) {
            exit();
        }
        botAnswerUtils(getCommandCache().getCommand(), chatId, userBD);
        if (!commandCacheList.isEmpty()) {
            commandCacheRepository.saveAll(commandCacheList);
        }
    }

    private void getUserByPhone() {

        if (isStart(Message.getMessageStartGetPhone(), "/getUserByPhone", "getPhone")) return;
        CommandCache command = getCommandCache();

        if (command.getSubCommand().equals("getPhone")) {
            if (!text.matches(REGEX_PHONE)) {
                sendMessage(Message.getMessageUnCorrectGetPhone());
                return;
            }
            UserDataResponse userDataResponse = api1C.getUserData(text);
            if (userDataResponse == null || !userDataResponse.isResult()) {
                sendMessage(Message.getMessageNonFindPhone());
                return;
            }
            sendMessage(toStringNonNullFields(userDataResponse));
            subCommandEnd();
        }
    }

    private void getByInn() {

        if (isStart(Message.getMessageStartINN(), "/getByInn", "getByInn")) return;
        CommandCache command = getCommandCache();

        if (command.getSubCommand().equals("getByInn")) {
            if (!text.matches(REGEX_INN)) {
                sendMessage(Message.getMessageUnCorrectINN());
                return;
            }
            DaDataParty daDataParty = daDataService.getCompanyDataByINN(text);
            if (daDataParty == null) {
                sendMessage(Message.getMessageUnCorrectINN());
                return;
            }
            sendMessage(toStringNonNullFields(daDataParty, true));
            subCommandEnd();
        }
    }

    private void registrationSurvey() {
        CommandCache command = getCommandCache();
        switch (command.getSubCommand()) {
            case "getInn" -> subCommandGetInn(command);
            case "getUserName" -> subCommandGetUserName(command);
            case "getPost" -> subCommandGetPost(command);
            case "end" -> subCommandEnd();
            default -> sendDefault("");
        }
    }

    private void afterRegistered() {
        UserDataResponse userDataResponse = api1C.getUserData(user.getPhone());
        if (userDataResponse.isResult()) {
            List<UserStatus> statusList = updateUserAfterRequestAPI(userDataResponse);
            sendMessage(Message.getMessageAfterSendingPhone(user.getPerson().getFirstName(), statusList),
                    Buttons.inlineMarkupDefault(statusList.get(0).getUserType()));
            return;
        }
        addNeSubCommand("/registrationSurvey", "getInn");
        sendMessage(Message.getMessageBeforeSurvey());
        registrationSurvey();
    }

    private void getContact() {
        sendMessage(Message.getMessageBeforeSendingPhone(), Buttons.getContact());
    }

    private void startBot() {
        UserStatus status = statusRepository.findFirstByUserBDOrderByLastUpdateDesc(user);
        if ((status == null || status.getUserType() == UserType.UNAUTHORIZED)
                && user.getPhone() != null
                && !user.getPhone().isEmpty()) {
            UserDataResponse userDataResponse = api1C.getUserData(user.getPhone());
            if (userDataResponse.isResult()) {
                updateUserAfterRequestAPI(userDataResponse);
            }
        }
        sendMessage(Message.getWelcomeMessage(), Buttons.inlineMarkupDefault(status.getUserType()));
    }

    private void sendHelpText() {
        sendMessage(com.telegrambot.app.components.BotCommands.HELP_TEXT);
    }

    private void sendDefault(String receivedMessage) {
        if (receivedMessage.matches(REGEX_INN)) {
            CompanyDataResponse response = api1C.getCompany(receivedMessage);
            LegalListData data = createDataByCompanyDataResponse(response);
            sendMessage(toStringNonNullFields(data));
            return;
        }
        if (receivedMessage.matches(REGEX_PHONE)) {
            UserDataResponse userDataResponse = api1C.getUserData(receivedMessage);
            userConverter.updateEntity(userDataResponse, user);
            userRepository.save(user);
            sendMessage(toStringNonNullFields(user));
            return;
        }
        sendMessage(Message.getDefaultMessageError(user.getPerson().getFirstName()));
    }

    private void exit() {
        commandCacheList = commandCacheRepository.findByUserBDOrderById(user);
        subCommandEnd(CommandStatus.INTERRUPTED_BY_USER);
    }

    private void subCommandGetTextInfo(CommandCache command, SubCommandInfo info) {
        if (isStart(command, info.getStartMessage())) return;
        if (info.getRegex() != null && !text.matches(info.getRegex())) {
            sendMessage(info.getErrorMessage());
            return;
        }
        runHandler(info.getHandler());
        completeSubCommand(command, info.getNextSumCommand());
        runHandler(info.getParent());
    }

    private void subCommandGetInn(CommandCache command) {
        if (isStart(command, Message.getMessageStartINN())) return;
        if (text.matches(REGEX_INN)) {
            createUserStatus(getPartnerByAPI(), "Удаленный пользователь");
            completeSubCommand(command, "getUserName");
            registrationSurvey();
            return;
        }
        sendMessage(Message.getMessageUnCorrectINN());
    }

    private void subCommandGetUserName(CommandCache command) {
        if (isStart(command, Message.getMessageStartUserName())) return;
        if (text.matches(REGEX_FIO)) {
            UserBDConverter.updateUserFIO(text, user.getPerson());
            userRepository.save(user);
            completeSubCommand(command, "getPost");
            registrationSurvey();
            return;
        }
        sendMessage(Message.getMessageUnCorrectUserName());
    }

    private void subCommandGetPost(CommandCache command) {
        if (isStart(command, Message.getMessageStartPost())) return;
        UserStatus status = statusRepository.findFirstByUserBDOrderByLastUpdateDesc(user);
        if (status != null) {
            status.setPost(text);
            statusRepository.save(status);
        }
        completeSubCommand(command, "end");
        registrationSurvey();
        sendMessage("Спасибо за информацию");
    }

    private void subCommandEnd(CommandStatus status) {
        if (!commandCacheList.isEmpty()) {
            Command command = new Command();
            command.setUserBD(user);
            command.setCommand(commandCacheList.get(0).getCommand());
            command.setResult(getStringSubCommand());
            command.setStatus(status);
            command.setDateComplete(LocalDateTime.now());
            commandRepository.save(command);

            log.info(toStringNonNullFields(commandCacheList, true));
            commandCacheRepository.deleteByUserBD(user);
            commandCacheList.clear();
        }
    }

    private void subCommandEnd() {
        subCommandEnd(CommandStatus.COMPLETE);
        sendMessage("Команда упешно обработана");
    }

    private String getStringSubCommand() {
        StringBuilder builder = new StringBuilder();
        for (CommandCache cache : commandCacheList) {
            builder.append(cache.getSubCommand())
                    .append(" -> ")
                    .append(cache.getResult())
                    .append("; count: ")
                    .append(cache.getCountStep())
                    .append(SEPARATOR);
        }
        return builder.toString();
    }

    private Partner getPartnerByAPI() {
        CompanyDataResponse response = api1C.getCompany(text);
        LegalListData data = createDataByCompanyDataResponse(response);
        Partner partner = data.getLegals().isEmpty() ? createLegalByInnFromDaData() : (Partner) data.getLegals().get(0);
        legalRepository.save(partner);
        return partner;
    }

    private Partner createLegalByInnFromDaData() {
        DaDataParty data = daDataService.getCompanyDataByINN(text);
        Partner partner = new Partner();
        partner.setInn(text);
        partner.setComment("created automatically on " + LocalDateTime.now().format(formatter));
        if (data != null) {
            partner.setName(data.getName().getShortWithOpf());
            partner.setKpp(data.getKpp());
            partner.setOGRN(data.getOgrn());
            partner.setCommencement(convertLongToLocalDateTime(data.getState().getRegistrationDate()));
            partner.setDateCertificate(convertLongToLocalDateTime(data.getOgrnDate()));
            partner.setOKPO(data.getOkpo());
        }
        return partner;
    }

    private LocalDateTime convertLongToLocalDateTime(long longDate) {
        return longDate == 0L ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(longDate), ZoneId.systemDefault());
    }

    private void sendMessage(String message) {
        sendMessage(message, null);
    }

    private void sendMessage(String message, ReplyKeyboard keyboard) {
        SendMessage sendMessage = createMessage(message, keyboard);
        parent.sendMassage(sendMessage);
    }

    private CommandCache getCommandCache() {
        return commandCacheList.get(commandCacheList.size() - 1);
    }

    private CommandCache getCommandCache(String message, String nameCommand, String nameSubCommand) {
        if (commandCacheList.isEmpty()) {
            addNeSubCommand(nameCommand, nameSubCommand);
            sendMessage(message);
        }
        return commandCacheList.get(commandCacheList.size() - 1);
    }

    private boolean isStart(CommandCache command, String message) {
        boolean start = command.getCountStep() == 0;
        if (start) {
            sendMessage(message);
        }
        command.setCountStep(command.getCountStep() + 1);
        return start;
    }

    private boolean isStart(String message, String nameCommand, String subCommand) {
        CommandCache command = commandCacheList.isEmpty() ? addNeSubCommand(nameCommand, subCommand) : getCommandCache();
        return isStart(command, message);
    }

    private void completeSubCommand(CommandCache command, String subCommand) {

        CommandCache commandCache = new CommandCache();
        commandCache.setSubCommand(subCommand);
        commandCache.setUserBD(user);
        commandCache.setCommand(command.getCommand());
        commandCache.setCountStep(0L);
        commandCacheList.add(commandCache);

        command.setComplete(true);
        command.setResult(text);
    }

    private SendMessage createMessage(String text, ReplyKeyboard keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(keyboard);
        return message;
    }

    private SendMessage createMessage(String text) {
        return createMessage(text, null);
    }

    private SendMessage createMessage() {
        return createMessage("", null);
    }

    private List<UserStatus> updateUserAfterRequestAPI(UserDataResponse userResponse) {

        userConverter.updateEntity(userResponse, user);
        user.setPhone(userResponse.getPhone().isEmpty() ? user.getPhone() : userResponse.getPhone());
        userRepository.save(user);

        List<UserStatus> userStatuses = new ArrayList<>();
        if (userResponse.getStatusList() == null || userResponse.getStatusList().isEmpty()) return userStatuses;

        userStatuses = userResponse.getStatusList().stream()
                .map(statusResponse -> {
                    Optional<LegalEntity> optional = legalRepository.findByGuid(statusResponse.getGuid());
                    LegalEntity legal = optional.orElse(getLegalEntityByGuid(statusResponse.getGuid()));
                    return createUserStatus(legal, statusResponse.getPost());
                })
                .collect(Collectors.toList());
        return userStatuses;
    }

    private LegalEntity getLegalEntityByGuid(String guid) {
        CompanyDataResponse dataResponse = api1C.getCompanyByGuid(guid);
        LegalListData data = createDataByCompanyDataResponse(dataResponse);
        return data.getLegals().isEmpty() ? null : data.getLegals().get(0);
    }

    private LegalListData createDataByCompanyDataResponse(CompanyDataResponse dataResponse) {
        LegalListData data = new LegalListData();
        if (dataResponse != null && dataResponse.isResult()) {
            data.setLegals(createLegalEntities(dataResponse.getLegalEntities()));
            data.setDepartments(createDepartments(dataResponse.getDepartments()));
            data.setContracts(createContracts(dataResponse.getContracts()));
        }
        return data;
    }

    private List<Contract> createContracts(List<ContractResponse> list) {
        return list.stream()
                .map(contractConverter::convertToEntity)
                .map(contract -> contractRepository.save((Contract) contract))
                .collect(Collectors.toList());
    }

    private List<Department> createDepartments(List<DepartmentResponse> list) {
        return list.stream()
                .map(departmentConverter::convertToEntity)
                .map(department -> departmentRepository.save((Department) department))
                .collect(Collectors.toList());
    }

    private List<LegalEntity> createLegalEntities(List<PartnerResponse> list) {
        return list.stream()
                .map(legalConverter::convertToEntity)
                .map(legal -> legalRepository.save((LegalEntity) legal))
                .collect(Collectors.toList());
    }

    public UserType getUserTypeByPost(String post) {
        String REGEX_DiRECTOR = ".*\\B(президент|директор|ректор|глава|председатель|предприниматель|управляющий)\\B.*|\\Bип\s+\\B.*|ип";
        String REGEX_ADMIN = ".*\\B(бухгалтер|администратор)\\B.*";

        if (post == null) {
            return UserType.USER;
        }
        post = post.toLowerCase();
        if (post.matches(REGEX_DiRECTOR)) {
            return UserType.DIRECTOR;
        }
        if (post.matches(REGEX_ADMIN)) {
            return UserType.ADMINISTRATOR;
        }
        return UserType.USER;
    }

    private UserStatus createUserStatus(UserType userType, LegalEntity legalEntity, String post) {
        UserStatus userStatus = new UserStatus();
        userStatus.setUserBD(user);
        userStatus.setUserType(userType);
        userStatus.setLastUpdate(LocalDateTime.now());
        userStatus.setLegal(legalEntity);
        userStatus.setPost(post);
        return statusRepository.save(userStatus);
    }

    private UserStatus createUserStatus(LegalEntity legalEntity, String post) {
        return createUserStatus(getUserTypeByPost(post), legalEntity, post);
    }

    private CommandCache addNeSubCommand(String name, String subCommand) {
        CommandCache command = new CommandCache();
        command.setCommand(name);
        command.setSubCommand(subCommand);
        command.setUserBD(user);
        command.setCountStep(0L);
        commandCacheList.add(command);
        return command;
    }

    public void setParent(TelegramBotServices parent) {
        this.parent = parent;
    }

    public String toStringNonNullFields(Object object, String tab) {
        return toStringNonNullFields(object, tab, false);
    }

    private static void runHandler(Runnable handler) {
        if (handler == null) {
            log.error("Trying to execute an empty handler");
            return;
        }
        try {
            handler.run();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String toStringNonNullFields(Object object, String tab, boolean isNested) {
        if (object == null) {
            return "";
        }
        List<Field> allFields = getFieldObject(object);
        StringBuilder stringBuilder = new StringBuilder();
        Object value = null;

        for (Field field : allFields) {
            try {
                field.setAccessible(true);
                value = field.get(object);
            } catch (Exception e) {
                log.error(e.getMessage());
                continue;
            }
            if (value == null) {
                continue;
            }
            stringBuilder.append(tab).append(field.getName()).append(": ");
            if (value instanceof Iterable) {
                toStringIterable(tab, stringBuilder, (Iterable<?>) value, isNested);
                continue;
            }
            if (value.getClass().isArray()) {
                toStringArray(tab, stringBuilder, value, isNested);
                continue;
            }
            boolean isContains = field.getType().getName().startsWith("com.telegrambot.app");
            boolean isDesiredType = !field.getType().isPrimitive() && !field.getType().isEnum();
            if (isDesiredType && isContains && isNested) {
                toStringObject(tab, stringBuilder, value, isNested);
                continue;
            }
            stringBuilder.append(value).append(SEPARATOR);
        }
        return stringBuilder.toString();
    }

    private List<Field> getFieldObject(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> allFields = new ArrayList<>();
        while (clazz != null && clazz.getTypeName().startsWith("com.telegrambot.app")) {
            Field[] declaredFields = clazz.getDeclaredFields();
            allFields.addAll(Arrays.asList(declaredFields));
            clazz = clazz.getSuperclass();
        }
        return allFields;
    }

    private void toStringObject(String tab, StringBuilder stringBuilder, Object value, boolean isNested) {
        stringBuilder.append(SEPARATOR)
                .append(toStringNonNullFields(value, tab + "\t", isNested))
                .append(SEPARATOR);
    }

    private void toStringArray(String tab, StringBuilder stringBuilder, Object value, boolean isNested) {
        stringBuilder.append(SEPARATOR);
        for (int i = 0; i < Array.getLength(value); i++) {
            Object objectValue = Array.get(value, i);
            if (objectValue != null) {
                addStringInCycle(tab, stringBuilder, i, objectValue, isNested);
            }
        }
    }

    private void toStringIterable(String tab, StringBuilder stringBuilder, Iterable<?> value, boolean isNested) {
        int index = 0;
        stringBuilder.append(SEPARATOR);
        for (Object element : value) {
            if (element != null) {
                addStringInCycle(tab, stringBuilder, index, element, isNested);
                index++;
            }
        }
    }

    private void addStringInCycle(String tab, StringBuilder stringBuilder, int index, Object element, boolean isNested) {
        stringBuilder.append(tab)
                .append("\t")
                .append("[").append(index).append("]: ")
                .append(toStringNonNullFields(element, "\t", isNested))
                .append(SEPARATOR);
    }

    public String toStringNonNullFields(Object object, boolean isNested) {
        return toStringNonNullFields(object, "", isNested);
    }

    public String toStringNonNullFields(Object object) {
        return toStringNonNullFields(object, false);
    }
}
