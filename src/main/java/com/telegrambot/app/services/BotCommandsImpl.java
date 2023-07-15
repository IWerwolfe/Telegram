package com.telegrambot.app.services;

import com.telegrambot.app.DTO.Currency;
import com.telegrambot.app.DTO.*;
import com.telegrambot.app.DTO.api_1C.*;
import com.telegrambot.app.DTO.dadata.DaDataParty;
import com.telegrambot.app.DTO.message.Message;
import com.telegrambot.app.components.Buttons;
import com.telegrambot.app.config.BotConfig;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.command.Command;
import com.telegrambot.app.model.command.CommandCache;
import com.telegrambot.app.model.command.CommandStatus;
import com.telegrambot.app.model.documents.doc.service.Task;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.LegalEntity;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.model.reference.Reference;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.task.TaskType;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import com.telegrambot.app.model.user.UserType;
import com.telegrambot.app.repositories.*;
import com.telegrambot.app.services.converter.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotCommandsImpl implements com.telegrambot.app.components.BotCommands {

    private final TaskRepository taskRepository;
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
    private final TaskConverter taskConverter;

    private final BotConfig bot;

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

        String[] param = receivedMessage.split(":");
        receivedMessage = param.length > 0 ? param[0] : receivedMessage;
        text = param.length > 1 && text == null ? param[1] : text;

        switch (receivedMessage) {
            case "/start" -> startBot();
            case "/help" -> sendHelpText();
            case "/send_contact" -> getContact();
            case "/afterRegistered" -> afterRegistered();
            case "/registrationSurvey" -> registrationSurvey();
            case "/getUserByPhone" -> getUserByPhone();
            case "/getByInn" -> getByInn();
            case "/get_task" -> getTask();
            case "/need_help" -> createAssistance();
            case "getTask" -> getDescriptionTask();
            case "descriptor" -> editDescriptionTask();
            case "comment" -> editCommentTask();
            case "cancel" -> editCancelTask();
            case "/setBalance" -> setBalance();
//            case "/need_help" -> createAssistance();
            case "/exit" -> exit();
            default -> sendDefault(receivedMessage);
        }
        if (!commandCacheList.isEmpty()) {
            commandCacheRepository.saveAll(commandCacheList);
        }
        text = null;
    }

    private void setBalance() {
        CommandCache command = getCommandCache("", "/setBalance", "getFormOfPayment");
        switch (command.getSubCommand()) {
            case "getFormOfPayment" -> {
//                if (text == null || text.isEmpty()) {
//                    sendMessage(Message.getFormOfPayment(), Buttons.getInlineByEnumFormOfPay("/setBalance"));
//                }
                SubCommandInfo info = new SubCommandInfo(Message.getFormOfPayment(),
                        Message.getErrorDescription(),
                        this::setBalance,
                        "getSum",
                        null,
                        Buttons.getInlineByEnumFormOfPay("/setBalance"));
                subCommandGetTextInfo(command, info);
            }
            case "getSum" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getInputSum(),
                        Message.getErrorDescription(),
                        this::setBalance,
                        "getPay",
                        "[0-9]{3,}");
                subCommandGetTextInfo(command, info);
            }
            case "getPay" -> {
                SendInvoice invoice = new SendInvoice();
                invoice.setChatId(chatId);
                invoice.setTitle("Внесение денег на баланс");
                invoice.setCurrency(Currency.RUB.name());
                invoice.setPayload("U:" + user.getGuid());
                invoice.setProviderToken(bot.getPayment());
                invoice.setDescription("Для продолжения следуйте подсказкам системы");
                int sum = 100 * Integer.parseInt(getResultSubCommandFromCache("getSum"));
                LabeledPrice labeledPrice = new LabeledPrice("Внесение денег на баланс", sum);
                invoice.setPrices(List.of(labeledPrice));
                parent.sendMassage(invoice);
                completeSubCommand(command, "createPayDoc");
            }
            case "createPayDoc" -> subCommandCreateTask();
            case "end" -> subCommandEnd();
            default -> sendDefault("");
        }
    }

    private void editDescriptionTask() {
        editTask(Message.getEditTextTask("описание"), "descriptor", this::editDescriptionTask);
    }

    private void editCommentTask() {
        editTask(Message.getEditTextTask("комментарий"), "comment", this::editCommentTask);
    }

    private void editCancelTask() {
        editTask(Message.getWhenCancelTask(), "cancel", this::editCancelTask);
    }

    private void editTask(String message, String nameCommand, Runnable parent) {
        CommandCache command = getCommandCache("", nameCommand + ":", "code");
        String subCommand = "edit" + nameCommand;
        switch (command.getSubCommand()) {
            case "code" -> {
                completeSubCommand(command, subCommand, getTaskCode());
                runHandler(parent);
            }
            case "editTask" -> subCommandEditTask(parent);
            case "end" -> subCommandEnd();
            default -> {
                if (command.getSubCommand().equals(subCommand)) {
                    SubCommandInfo info = new SubCommandInfo(message,
                            Message.errorWhenEditTask(),
                            parent,
                            "editTask");
                    subCommandGetTextInfo(command, info);
                }
            }
        }
    }

    private void subCommandEditTask(Runnable parent) {
        if (commandCacheList.isEmpty()) {
            exit(Message.getIncorrectTask());
            return;
        }

        String code = getResultSubCommandFromCache("code");
        Optional<Task> optional = taskRepository.findById(Long.valueOf(code));
        if (optional.isEmpty()) {
            exit(Message.getIncorrectTask());
            return;
        }
        Task task = optional.get();
        String prefix = "Дополнено " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + " -> ";
        task.setDescription(getResultFromSubCommand("editDescriptor", task.getDescription(), prefix));
        task.setComment(getResultFromSubCommand("editComment", task.getComment(), prefix));
        task.setDecision(getResultFromSubCommand("editDecision", task.getDecision(), prefix));
        String message = "";
        message = getResultSubCommandFromCache("editCancel");
        if (!message.isEmpty()) {
            closingTask(task, message, false, "Закрыто пользователем:");
        }
        message = getResultSubCommandFromCache("closed");
        if (!message.isEmpty()) {
            closingTask(task, message, true);
        }
        taskRepository.save(task);
        sendMessage(Message.getSuccessfullyCreatingTask(task));
        completeSubCommand(getCommandCache(), "end");
        runHandler(parent);
    }

    private String getResultFromSubCommand(String nameSubCommand, String field, String prefix) {
        String message = getResultSubCommandFromCache(nameSubCommand);
        if (message.isEmpty()) {
            return field;
        }
        return field == null || field.isEmpty() ? message : prefix + SEPARATOR + message + SEPARATOR + field;
    }

    private void closingTask(Task task, String message, boolean successfully) {
        task.setStatus(TaskStatus.getDefaultClosedStatus());
        task.setClosingDate(LocalDateTime.now());
        task.setSuccessfully(successfully);
        String decision = task.getDecision();
        task.setDecision(decision == null || decision.isEmpty() ? message : message + SEPARATOR + decision);
    }

    private void closingTask(Task task, String message, boolean successfully, String prefix) {
        message = prefix.isEmpty() ? message : prefix + SEPARATOR + message;
        closingTask(task, message, successfully);
    }

    private void getDescriptionTask() {
        Optional<Task> optional = taskRepository.findById(Long.valueOf(text));
        if (optional.isEmpty()) {
            sendMessage("Task not found");
            return;
        }
        sendMessage(optional.get().toString(true), Buttons.getInlineMarkupEditTask(optional.get()));
    }

    private String getTaskCode() {
        return "0".repeat(9 - text.length()) + text;
    }

    private void createAssistance() {
        String message = Message.getStartCreateAssistance(user.getPerson().getFirstName());
        CommandCache command = getCommandCache(message, "/need_help", "getTopic");
        switch (command.getSubCommand()) {
            case "getTopic" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getStartTopic(),
                        this::createAssistance,
                        "getDescription");
                subCommandGetTextInfo(command, info);
            }
            case "getDescription" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getStartDescription(),
                        Message.getErrorDescription(),
                        this::createAssistance,
                        "getPhone");
                subCommandGetTextInfo(command, info);
            }
            case "getPhone" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getStartGetPhone(),
                        Message.getUnCorrectGetPhone(),
                        this::createAssistance,
                        "getUserName",
                        REGEX_PHONE
                );
                subCommandGetTextInfo(command, info);
            }
            case "getUserName" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getStartName(),
                        Message.getErrorName(),
                        this::createAssistance,
                        "createTask");
                subCommandGetTextInfo(command, info);
            }
            case "createTask" -> subCommandCreateTask();
            case "end" -> subCommandEnd();
            default -> sendDefault("");
        }
    }

    private void getTask() {
        List<UserStatus> statuses = statusRepository.findByUserBDOrderByLastUpdateDesc(user);
        switch (statuses.size()) {
            case 0 -> sendMessage(Message.getSearchErrors());
            case 1 -> sendTaskList(getSortedTask(getTaskListToSend(statuses.get(0))));
            default -> statuses.forEach(s -> {
                TaskListToSend taskListToSend = getTaskListToSend(s);
                sendMessage(Message.getSearch(getNameEntity(s.getLegal()), taskListToSend.getTasks().size()));
                sendTaskList(getSortedTask(taskListToSend));
            });
        }
    }

    private void sendTaskList(Map<String, List<Task>> sortedTask) {
        if (sortedTask.isEmpty()) {
            sendMessage(Message.getSearchErrors());
        }
        sortedTask.keySet().forEach(sortName -> {
            List<Task> taskList = sortedTask.get(sortName);
            String message = Message.getSearchGrouping(sortName, taskList.size());
            ReplyKeyboard keyboard = Buttons.getInlineMarkupByTasks(taskList);
            sendMessage(message, keyboard);
        });
    }

    private Map<String, List<Task>> getSortedTask(TaskListToSend taskListToSend) {
        return taskListToSend.getTasks().stream()
                .collect(Collectors.groupingBy(task -> switch (taskListToSend.getSorting()) {
                    case STATUS -> getNameEntity(task.getStatus());
                    case PARTNER -> getNameEntity(task.getPartnerData().getPartner());
                    default -> getNameEntity(task.getPartnerData().getDepartment());
                }));
    }

    private TaskListToSend getTaskListToSend(UserStatus status) {
        List<Task> tasks = new ArrayList<>();
        SortingTaskType sortingType = SortingTaskType.DEPARTMENT;
        switch (status.getUserType()) {
            case UNAUTHORIZED, USER -> {
                if (TaskStatus.getDefaultClosedStatus() != null) {
                    tasks = taskRepository.findByCreatorAndStatusNotOrderByDateAsc(user, TaskStatus.getDefaultClosedStatus());
                }
            }
            case ADMINISTRATOR -> tasks = status.getDepartment() == null ?
                    getTaskListByApiByCompany(Request1CConverter.convertToGuid(status.getLegal())) :
                    getTaskListByApiByDepartment(Request1CConverter.convertToGuid(status.getDepartment()));
            case DIRECTOR -> tasks = getTaskListByApiByCompany(Request1CConverter.convertToGuid(status.getLegal()));
            default -> {
                if (user.getIsMaster()) {
                    tasks = getTaskListByApiByManager();
                    sortingType = SortingTaskType.PARTNER;
                }
            }
        }
        return new TaskListToSend(sortingType, tasks);
    }

    private String getNameEntity(Entity entity) {
        if (entity instanceof Reference ref) {
            return ref.getName();
        }
        if (entity == null) {
            return "";
        }
        return entity.getClass().getSimpleName();
    }

    private List<Task> getTaskListByApiByManager() {
        //TODO исправить на запрос из БД
        return createTasks(api1C.getTaskListDataByManager(user.getGuid()));
    }

    private List<Task> getTaskListByApiByCompany(String guidCompany) {
        //TODO исправить на запрос из БД
        return createTasks(api1C.getTaskListDataByCompany(guidCompany));
    }

    private List<Task> getTaskListByApiByDepartment(String guidDepartment) {
        //TODO исправить на запрос из БД
        return createTasks(api1C.getTaskListDataByDepartment(guidDepartment));
    }

    private List<Task> getTaskListByApiByUser() {
        //TODO исправить на запрос из БД
        return createTasks(api1C.getTaskListDataByUser(user.getGuid()));
    }

    private List<Task> createTasks(TaskDataListResponse response) {
        List<Task> tasks = new ArrayList<>();
        if (!response.isResult()) {
            return tasks;
        }
        for (TaskResponse task : response.getTasks()) {
            tasks.add(taskConverter.convertToEntity(task));
        }
        taskRepository.saveAll(tasks);
        return tasks;
    }

    private void subCommandCreateTask() {
        if (commandCacheList.size() == 0) {
            exit(Message.getIncorrectTask());
            return;
        }
        Task task = new Task();
        task.setCreator(user);
        task.setAuthor(user.getGuid());
        task.setStatus(TaskStatus.getDefaultInitialStatus());
        UserDataResponse userData = api1C.getUserData(getResultSubCommandFromCache("getPhone"));
        if (userData != null && userData.getStatusList() != null && userData.getStatusList().size() >= 1) {
            task.setPartnerData(getPartnerDateByAPI(userData.getStatusList().get(0).getGuid()));
        }
        task.setDescription(getDescriptionBySubCommand());
        task.setType(TaskType.getDefaultType());

        TaskCreateResponse createResponse = api1C.createTask(taskConverter.convertTaskToTaskResponse(task));
        if (createResponse != null && createResponse.isResult()) {
            task.setSyncData(new SyncData(createResponse.getGuid(), createResponse.getCode()));
        }

        taskRepository.save(task);
        sendMessage(Message.getSuccessfullyCreatingTask(task));
//        sendToWorkGroup(task.toString());

        completeSubCommand(getCommandCache(), "end");
        createAssistance();
    }

    private String getResultSubCommandFromCache(String subCommand) {
        Optional<String> result = commandCacheList.stream()
                .filter(command -> command.getSubCommand().equalsIgnoreCase(subCommand))
                .map(CommandCache::getResult)
                .findFirst();

        return result.orElse("");
    }

    public void botAnswerUtils(List<CommandCache> commandCacheList, String text, long chatId, UserBD userBD) {
        this.commandCacheList = commandCacheList;
        this.text = text;

        if (text != null && text.equals("/exit")) {
            exit(Message.getExitCommand(getCommandCache().getCommand()));
        }
        botAnswerUtils(getCommandCache().getCommand(), chatId, userBD);
        if (!commandCacheList.isEmpty()) {
            commandCacheRepository.saveAll(commandCacheList);
        }
    }

    private void getUserByPhone() {

        if (isStart(Message.getStartGetPhone(), "/getUserByPhone", "getPhone")) return;
        CommandCache command = getCommandCache();

        if (command.getSubCommand().equals("getPhone")) {
            if (!text.matches(REGEX_PHONE)) {
                sendMessage(Message.getUnCorrectGetPhone());
                return;
            }
            UserDataResponse userDataResponse = api1C.getUserData(text);
            if (userDataResponse == null || !userDataResponse.isResult()) {
                sendMessage(Message.getNonFindPhone());
                return;
            }
            sendMessage(toStringNonNullFields(userDataResponse));
            subCommandEnd();
        }
    }

    private void getByInn() {

        if (isStart(Message.getStartINN(), "/getByInn", "getByInn")) return;
        CommandCache command = getCommandCache();

        if (command.getSubCommand().equals("getByInn")) {
            if (!text.matches(REGEX_INN)) {
                sendMessage(Message.getUnCorrectINN());
                return;
            }
            DaDataParty daDataParty = daDataService.getCompanyDataByINN(text);
            if (daDataParty == null) {
                sendMessage(Message.getUnCorrectINN());
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
            sendMessage(Message.getAfterSendingPhone(user.getPerson().getFirstName(), statusList),
                    Buttons.inlineMarkupDefault(statusList.get(0).getUserType()));
            return;
        }
        addNeSubCommand("/registrationSurvey", "getInn");
        sendMessage(Message.getBeforeSurvey());
        registrationSurvey();
    }

    private void getContact() {
        sendMessage(Message.getBeforeSendingPhone(), Buttons.getContact());
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
            CompanyDataResponse response = api1C.getCompanyData(receivedMessage);
            LegalListData data = createDataByCompanyDataResponse(response);
            sendMessage(toStringNonNullFields(data));
            return;
        }
        if (receivedMessage.matches(REGEX_PHONE)) {
//            UserDataResponse userDataResponse = api1C.getUserData(receivedMessage);
//            userConverter.updateEntity(userDataResponse, user);
//            userRepository.save(user);
            Optional<UserBD> optional = userRepository.findByPhone(receivedMessage);
            if (optional.isPresent()) {
                List<UserStatus> statusList = statusRepository.findByUserBDOrderByLastUpdateDesc(optional.get());
                String string = toStringNonNullFields(optional.get()) +
                        SEPARATOR + SEPARATOR + "Statuses: " +
                        toStringIterableNonNull(statusList, false);
                sendMessage(string);
                return;
            }
        }
        if (receivedMessage.matches("000[0-9]{6}")) {
            TaskDataResponse taskResponse = api1C.getTaskByCode(receivedMessage);
            if (taskResponse != null && taskResponse.isResult()) {
                Task task = taskConverter.convertToEntity(taskResponse.getTask());
                taskRepository.save(task);
                sendMessage(task.toString(true));
                return;
            }
        }
        sendMessage(Message.getDefaultMessageError(user.getPerson().getFirstName()));
    }

    private void exit() {
        commandCacheList = commandCacheRepository.findByUserBDOrderById(user);
        subCommandEnd(CommandStatus.INTERRUPTED_BY_USER);
    }

    private void exit(String message) {
        exit();
        sendMessage(message, Buttons.inlineMarkupDefault(statusRepository.findFirstByUserBDOrderByLastUpdateDesc(user).getUserType()));
    }

    private void subCommandGetTextInfo(CommandCache command, SubCommandInfo info) {
        if (isStart(command, info.getStartMessage(), info.getKeyboard())) return;
        if (info.getRegex() != null && !text.matches(info.getRegex())) {
            sendMessage(info.getErrorMessage());
            return;
        }
        runHandler(info.getHandler());
        completeSubCommand(command, info.getNextSumCommand());
        runHandler(info.getParent());
    }

    private void subCommandGetInn(CommandCache command) {
        if (isStart(command, Message.getStartINN())) return;
        if (text.matches(REGEX_INN)) {
            createUserStatus(getPartnerByAPI(), "Удаленный пользователь");
            completeSubCommand(command, "getUserName");
            registrationSurvey();
            return;
        }
        sendMessage(Message.getUnCorrectINN());
    }

    private void subCommandGetUserName(CommandCache command) {
        if (isStart(command, Message.getStartUserName())) return;
        if (text.matches(REGEX_FIO)) {
            UserBDConverter.updateUserFIO(text, user.getPerson());
            userRepository.save(user);
            completeSubCommand(command, "getPost");
            registrationSurvey();
            return;
        }
        sendMessage(Message.getUnCorrectUserName());
    }

    private void subCommandGetPost(CommandCache command) {
        if (isStart(command, Message.getStartPost())) return;
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

    private Partner getPartnerByAPI(String text) {
        CompanyDataResponse response = text.matches(REGEX_INN) ? api1C.getCompanyData(text) : api1C.getCompanyByGuid(text);
        LegalListData data = createDataByCompanyDataResponse(response);
        Partner partner = data.getLegals() == null || data.getLegals().isEmpty() ?
                createLegalByInnFromDaData() :
                (Partner) data.getLegals().get(0);
        legalRepository.save(partner);
        return partner;
    }

    private PartnerData getPartnerDateByAPI(String text) {
        CompanyDataResponse response = text.matches(REGEX_INN) ? api1C.getCompanyData(text) : api1C.getCompanyByGuid(text);
        LegalListData data = createDataByCompanyDataResponse(response);
        PartnerData partnerData = new PartnerData();

        if (data.getLegals().isEmpty()) {
            Partner partner = createLegalByInnFromDaData();
            partnerData.setPartner(legalRepository.save(partner));
            partnerData.setContract(contractRepository.save(new Contract(partner)));
            return partnerData;
        }
        Partner partner = (Partner) data.getLegals().get(0);
        partnerData.setPartner(partner);
        partnerData.setContract(data.getContracts().isEmpty() ? contractRepository.save(new Contract(partner)) : data.getContracts().get(0));
        partnerData.setDepartment(data.getDepartments().size() == 1 ? data.getDepartments().get(0) : null);

        return partnerData;
    }

    private Partner getPartnerByAPI() {
        return getPartnerByAPI(text);
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
            partner.setCommencement(Request1CConverter.convertLongToLocalDateTime(data.getState().getRegistrationDate()));
            partner.setDateCertificate(Request1CConverter.convertLongToLocalDateTime(data.getOgrnDate()));
            partner.setOKPO(data.getOkpo());
        }
        return partner;
    }

    private String getDescriptionBySubCommand() {
        StringBuilder description = new StringBuilder();
        description.append(getResultSubCommandFromCache("getTopic"))
                .append(SEPARATOR)
                .append(getResultSubCommandFromCache("getDescription"))
                .append(SEPARATOR)
                .append(getResultSubCommandFromCache("getPhone"))
                .append(" (").append(getResultSubCommandFromCache("getUserName")).append(")");
        return description.toString();
    }

    private void sendMessage(String message) {
        sendMessage(message, null);
    }

    private void sendMessage(String message, ReplyKeyboard keyboard) {
        try {
            SendMessage sendMessage = createMessage(message, keyboard);
            parent.sendMassage(sendMessage);
        } catch (Exception e) {
            log.error("an error occurred while sending a message to the user {} \r\nError: {}",
                    user.getId(),
                    e.getMessage());
        }
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
        return isStart(command, message, null);
    }

    private boolean isStart(CommandCache command, String message, ReplyKeyboard keyboard) {
        boolean start = command.getCountStep() == 0;
        if (start) {
            sendMessage(message, keyboard);
        }
        command.setCountStep(command.getCountStep() + 1);
        return start;
    }

    private boolean isStart(String message, String nameCommand, String subCommand) {
        CommandCache command = commandCacheList.isEmpty() ? addNeSubCommand(nameCommand, subCommand) : getCommandCache();
        return isStart(command, message);
    }

    private void completeSubCommand(CommandCache command, String subCommand, String result) {

        CommandCache commandCache = new CommandCache();
        commandCache.setSubCommand(subCommand);
        commandCache.setUserBD(user);
        commandCache.setCommand(command.getCommand());
        commandCache.setCountStep(0L);
        commandCacheList.add(commandCache);

        command.setComplete(true);
        command.setResult(result);
    }

    private void completeSubCommand(CommandCache command, String subCommand) {
        completeSubCommand(command, subCommand, text);
    }

    private SendMessage createMessage(String text, ReplyKeyboard keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(keyboard);
        return message;
    }

    private void sendToWorkGroup(String text) {
        //TODO дописать эту процедуру
        SendMessage message = new SendMessage();
        message.setChatId(-1001380655854L);
        message.setText("Создана новая задача " + text);
        parent.sendMassage(message);
    }

    private SendMessage createMessage(String text) {
        return createMessage(text, null);
    }

    private SendMessage createMessage() {
        return createMessage("", null);
    }

    private List<UserStatus> updateUserAfterRequestAPI(UserDataResponse userData) {

        userConverter.updateEntity(userData, user);
        userRepository.save(user);

        //TODO Добавить обработку задач

        List<UserStatus> userStatuses = new ArrayList<>();
        if (userData.getStatusList() == null || userData.getStatusList().isEmpty()) return userStatuses;

        statusRepository.deleteByUserBD(user);
        userStatuses = userData.getStatusList().stream()
                .map(statusResponse -> {
                    Optional<LegalEntity> optional = legalRepository.findBySyncDataNotNullAndSyncData_Guid(statusResponse.getGuid());
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
        Map<String, List<ContractResponse>> groupedByGuidPartner = list.stream()
                .collect(Collectors.groupingBy(ContractResponse::getGuidPartner));
        List<Contract> saveContracts = new ArrayList<>();

        for (String key : groupedByGuidPartner.keySet()) {

            Optional<LegalEntity> optional = legalRepository.findBySyncDataNotNullAndSyncData_Guid(key);
            if (optional.isEmpty()) {
                continue;
            }
            Partner partner = (Partner) optional.get();
            List<Contract> contracts = contractRepository.findByPartnerOrderByIdAsc(partner);
            List<ContractResponse> contractResponses = groupedByGuidPartner.get(key);

            for (int i = 0; i < contractResponses.size(); i++) {
                Contract contract = contracts.size() < i ?
                        contractConverter.updateEntity(contractResponses.get(i), contracts.get(i)) :
                        contractConverter.convertToEntity(contractResponses.get(i));
                saveContracts.add(contract);
            }
        }
        contractRepository.saveAll(saveContracts);
        return saveContracts;
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

    private String toStringIterableNonNull(Iterable<?> value, boolean isNested) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        toStringIterable("", stringBuilder, value, isNested);
        return stringBuilder.append("]").toString();
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
