package com.telegrambot.app.services;

import com.telegrambot.app.DTO.SubCommandInfo;
import com.telegrambot.app.DTO.TaskListToSend;
import com.telegrambot.app.DTO.api_1C.BalanceResponse;
import com.telegrambot.app.DTO.api_1C.SyncDataResponse;
import com.telegrambot.app.DTO.api_1C.UserDataResponse;
import com.telegrambot.app.DTO.api_1C.legal.PartnerListData;
import com.telegrambot.app.DTO.api_1C.legal.partner.ContractResponse;
import com.telegrambot.app.DTO.api_1C.legal.partner.DepartmentResponse;
import com.telegrambot.app.DTO.api_1C.legal.partner.PartnerDataResponse;
import com.telegrambot.app.DTO.api_1C.legal.partner.PartnerResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataListResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskResponse;
import com.telegrambot.app.DTO.dadata.DaDataParty;
import com.telegrambot.app.DTO.message.Message;
import com.telegrambot.app.DTO.types.Currency;
import com.telegrambot.app.DTO.types.PaymentType;
import com.telegrambot.app.DTO.types.SortingTaskType;
import com.telegrambot.app.DTO.types.TaskType;
import com.telegrambot.app.components.Buttons;
import com.telegrambot.app.config.BotConfig;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.EntitySavedEvent;
import com.telegrambot.app.model.balance.LegalBalance;
import com.telegrambot.app.model.balance.UserBalance;
import com.telegrambot.app.model.command.Command;
import com.telegrambot.app.model.command.CommandCache;
import com.telegrambot.app.model.command.CommandStatus;
import com.telegrambot.app.model.documents.doc.payment.CardDoc;
import com.telegrambot.app.model.documents.doc.service.Task;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.LegalEntity;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.model.reference.Reference;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import com.telegrambot.app.model.user.UserType;
import com.telegrambot.app.repositories.*;
import com.telegrambot.app.services.api.ApiOutServiceImpl;
import com.telegrambot.app.services.api.DaDataService;
import com.telegrambot.app.services.converter.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotCommandsImpl implements BotCommands {
    private final LegalBalanceRepository legalBalanceRepository;
    private final UserBalanceRepository userBalanceRepository;

    private final CardDocRepository cardDocRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserStatusRepository statusRepository;
    private final PartnerRepository partnerRepository;
    private final DepartmentRepository departmentRepository;
    private final ContractRepository contractRepository;
    private final CommandCacheRepository commandCacheRepository;
    private final CommandRepository commandRepository;
    private final ApiOutServiceImpl api1C;
    private final DaDataService daDataService;

    private final ApplicationEventPublisher eventPublisher;

    private final PartnerConverter partnerConverter;
    private final DepartmentConverter departmentConverter;
    private final ContractConverter contractConverter;
    private final UserBDConverter userConverter;
    private final TaskConverter taskConverter;
    private final ToStringServices toStringServices;
    private final BalanceService balanceService;

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
        botAnswerUtils(receivedMessage);
    }

    public void botAnswerUtils(List<CommandCache> commandCacheList, String text, long chatId, UserBD userBD) {
        this.commandCacheList = commandCacheList;
        this.text = text;
        this.user = userBD;
        this.chatId = chatId;

        if (text != null && text.equals("/exit")) {
            exit(Message.getExitCommand(getCommandCache().getCommand()));
        } else {
            botAnswerUtils(getCommandCache().getCommand());
        }
        if (!commandCacheList.isEmpty()) {
            commandCacheRepository.saveAll(commandCacheList);
        }
    }

    public void botAnswerUtils(String receivedMessage) {

        if (receivedMessage == null) {
            return;
        }

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
            case "/add_balance" -> addBalance();
            case "/get_balance" -> getBalance();
            case "pay" -> payTask();
//            case "/need_help" -> createAssistance();
            case "/exit" -> exit();
            default -> sendDefault(receivedMessage);
        }
        if (!commandCacheList.isEmpty()) {
            commandCacheRepository.saveAll(commandCacheList);
        }
        text = null;
    }

    private void getBalance() {
        List<LegalBalance> balances = getBalances();
        if (balances == null || balances.isEmpty()) {
            sendMessage(Message.getBalanceUser(getBalanceByUser()));
            return;
        }
        StringBuilder text = new StringBuilder();
        for (LegalBalance balance : balances) {
            text.append(Message.getBalanceLegal(balance.getLegal(), balance.getAmount()))
                    .append(SEPARATOR);
        }
        sendMessage(text.toString());
    }

    private List<LegalBalance> getBalances() {
        List<UserStatus> status = statusRepository.findByUserBDAndLegalNotNull(user);
        if (!status.isEmpty()) {
            List<LegalEntity> legals = getLegalByUserStatus(status);
            return legalBalanceRepository.findByLegalInOrderByLegal_NameAsc(legals);
        }
        return null;
    }

    private List<LegalEntity> getLegalByUserStatus(List<UserStatus> userStatuses) {
        List<LegalEntity> legals = new ArrayList<>();
        userStatuses.forEach(s -> legals.add(s.getLegal()));
        return legals;
    }

    private int getBalanceByUser() {
        Optional<UserBalance> optional = userBalanceRepository.findByUser(user);
        return optional.isEmpty() ? 0 : optional.get().getAmount();
    }

    private void addPayment(CommandCache command, Runnable parentRun, String title, String explanation) {
        switch (command.getSubCommand()) {
            case "getFormOfPayment" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getFormOfPayment(),
                        Message.errorWhenEditTask(),
                        parentRun,
                        "getPay",
                        null,
                        Buttons.getInlineByEnumFormOfPay(command.getCommand()));
                subCommandGetTextInfo(command, info);
            }
            case "getPay" -> {
                SendInvoice invoice = getSendInvoice(title, explanation);
                parent.sendMassage(invoice);
                completeSubCommand(command, "createPayDoc");
            }
            case "createPayDoc" -> subCommandCreatePayDoc();
            case "end" -> subCommandEnd();
            default -> sendDefault("");
        }
    }

    private void payTask() {
        CommandCache command = getCommandCache("", "pay", "getSum");
        if (command.getSubCommand().equalsIgnoreCase("getSum")) {
            Optional<Task> optional = taskRepository.findById(Long.valueOf(text));
            if (optional.isEmpty()) {
                exit("Task not found");
                return;
            }
            addNewSubCommand("pay", "taskCode", text);
            completeSubCommand(command, "getFormOfPayment", String.valueOf(optional.get().getTotalAmount()));
            command = getCommandCache();
        }
        addPayment(command, this::payTask, "Оплата здачи", getResultSubCommandFromCache("taskCode"));
    }

    private void addBalance() {
        CommandCache command = getCommandCache("", "/add_balance", "getSum");
        if (command.getSubCommand().equalsIgnoreCase("getSum")) {
            SubCommandInfo info = new SubCommandInfo(Message.getInputSum(),
                    Message.errorWhenEditTask(),
                    this::addBalance,
                    "getFormOfPayment",
                    "[0-9]{3,}",
                    this::convertSumTo);
            subCommandGetTextInfo(command, info);
            return;
        }
        addPayment(command, this::addBalance, "Внесение денег на баланс", "id: " + user.getId());
    }

    private SendInvoice getSendInvoice(String title, String explanation) {
        SendInvoice invoice = new SendInvoice();
        invoice.setChatId(chatId);
        invoice.setTitle(title);
        invoice.setCurrency(Currency.RUB.name());
        invoice.setPayload("U:" + user.getGuidEntity());
        invoice.setProviderToken(bot.getPayment());
        invoice.setDescription("Для продолжения следуйте подсказкам системы");
        int sum = Integer.parseInt(getResultSubCommandFromCache("getSum"));
        LabeledPrice labeledPrice = new LabeledPrice(title + " " + explanation, sum);
        invoice.setPrices(List.of(labeledPrice));
        return invoice;
    }

    private void subCommandCreatePayDoc() {
        if (commandCacheList.size() == 0 || text == null) {
            exit(Message.getDefaultMessageError(user.getUserName()));
            return;
        }

        String[] strings = text.split(";");
        String ref = strings.length > 1 ? strings[1] : null;

        List<UserStatus> statuses = statusRepository.findByUserBDAndLegalNotNull(user);
        Partner partner = statuses.size() == 1 ? (Partner) statuses.get(0).getLegal() : null;

        CardDoc cardDoc = new CardDoc();
        cardDoc.setTotalAmount(Integer.valueOf(getResultSubCommandFromCache("getSum")));
        cardDoc.setPartnerData(partner);
        cardDoc.setReferenceNumber(ref);
        cardDoc.setAuthor(user.getGuidEntity());
        cardDoc.setCreator(user);
        cardDoc.setPaymentType(PaymentType.INCOMING);
        cardDocRepository.save(cardDoc);
        eventPublisher.publishEvent(new EntitySavedEvent(cardDoc));

//        api1C.createCardDoc() //TODO написать обмен с 1С

        sendMessage(Message.getSuccessfullyCreatingCardDoc(cardDoc));
        completeSubCommand(getCommandCache(), "end");
        addBalance();
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
                .collect(Collectors.groupingBy(task -> getStringSorting(taskListToSend, task)));
    }

    private String getStringSorting(TaskListToSend taskListToSend, Task task) {
        return switch (taskListToSend.getSorting()) {
            case STATUS -> getNameEntity(task.getStatus());
            case PARTNER -> getNameEntity(task.getPartnerData().getPartner());
            default -> getNameEntity(task.getPartnerData().getDepartment());
        };
    }

    private TaskListToSend getTaskListToSend(UserStatus status) {
        List<Task> tasks = new ArrayList<>();
        SortingTaskType sortingType = SortingTaskType.DEPARTMENT;
        switch (status.getUserType()) {
            case UNAUTHORIZED, USER -> {
                if (TaskStatus.getDefaultClosedStatus() != null) {
                    tasks = getTaskListByApiByUser();
                }
            }
            case ADMINISTRATOR -> tasks = status.getDepartment() == null ?
                    getTaskListByApiByCompany(status.getLegal()) :
                    getTaskListByApiByDepartment(status.getDepartment());
            case DIRECTOR -> tasks = getTaskListByApiByCompany(status.getLegal());
            default -> {
                if (user.getIsMaster()) {
                    //TODO Написать связь Manager и UserBD
//                    tasks = getTaskListByApiByManager();
//                    sortingType = SortingTaskType.PARTNER;
                }
            }
        }
        return new TaskListToSend(sortingType, tasks);
    }

    private String getNameEntity(Entity entity) {
        if (entity == null) {
            return "-";
        }
        if (entity instanceof Reference ref) {
            return ref.toString();
        }
        return entity.getClass().getSimpleName();
    }

    private List<Task> getTaskListByApiByManager(Manager manager) {
        TaskDataListResponse response = api1C.getTaskListDataByManager(user.getGuidEntity());
        if (response == null || !response.isResult()) {
            return taskRepository.findByManagerAndStatusNotOrderByDateAsc(manager, TaskStatus.getDefaultClosedStatus());
        }
        return createTasks(response);
    }

    private List<Task> getTaskListByApiByCompany(LegalEntity legal) {
        TaskDataListResponse response = api1C.getTaskListDataByCompany(Converter1C.convertToGuid(legal));
        if (response == null || !response.isResult()) {
            return taskRepository.findByPartnerDataNotNullAndPartnerData_PartnerAndStatusNotOrderByDateAsc((Partner) legal,
                    TaskStatus.getDefaultClosedStatus());
        }
        return createTasks(response);
    }

    private List<Task> getTaskListByApiByDepartment(Department department) {
        TaskDataListResponse response = api1C.getTaskListDataByDepartment(Converter1C.convertToGuid(department));
        if (response == null || !response.isResult()) {
            return taskRepository.findByPartnerDataNotNullAndPartnerData_DepartmentAndStatusNotOrderByDateAsc(department,
                    TaskStatus.getDefaultClosedStatus());
        }
        return createTasks(response);
    }

    private List<Task> getTaskListByApiByUser() {
        TaskDataListResponse response = api1C.getTaskListDataByUser(user.getGuidEntity());
        if (response == null || !response.isResult()) {
            return taskRepository.findByCreatorAndStatusNotOrderByDateAsc(user, TaskStatus.getDefaultClosedStatus());
        }
        return createTasks(response);
    }

    private List<Task> createTasks(TaskDataListResponse response) {
        return response.isResult() ? createTasks(response.getTasks()) : new ArrayList<>();
    }

    private List<Task> createTasks(List<TaskResponse> list) {
        List<Task> tasks = new ArrayList<>();
        list.forEach(r -> tasks.add(taskConverter.convertToEntity(r)));
        return taskRepository.saveAll(tasks);
    }

    private void subCommandCreateTask() {
        if (commandCacheList.size() == 0) {
            exit(Message.getIncorrectTask());
            return;
        }
        Task task = new Task();
        task.setCreator(user);
        task.setAuthor(user.getGuidEntity());
        task.setStatus(TaskStatus.getDefaultInitialStatus());
        UserDataResponse userData = api1C.getUserData(getResultSubCommandFromCache("getPhone"));
        if (userData != null && userData.getStatusList() != null && userData.getStatusList().size() >= 1) {
            task.setPartnerData(getPartnerDateByAPI(userData.getStatusList().get(0).getGuid()));
        }
        task.setDescription(getDescriptionBySubCommand());
        task.setType(TaskType.getDefaultType());

        SyncDataResponse createResponse = api1C.createTask(taskConverter.convertToResponse(task));
        if (createResponse != null && createResponse.isResult()) {
            task.setSyncData(new SyncData(createResponse.getGuid(), createResponse.getCode()));
        }

        taskRepository.save(task);
        eventPublisher.publishEvent(new EntitySavedEvent(task));
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
            sendMessage(toStringServices.toStringNonNullFields(userDataResponse));
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
            sendMessage(toStringServices.toStringNonNullFields(daDataParty, true));
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
        addNewSubCommand("/registrationSurvey", "getInn");
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
        sendMessage(BotCommands.HELP_TEXT);
    }

    private void sendDefault(String receivedMessage) {
        if (receivedMessage.matches(REGEX_INN)) {
            PartnerDataResponse response = api1C.getPartnerData(receivedMessage);
            PartnerListData data = createDataByCompanyDataResponse(response);
            sendMessage(toStringServices.toStringNonNullFields(data));
            return;
        }
        if (receivedMessage.matches(REGEX_PHONE)) {
//            UserDataResponse userDataResponse = api1C.getUserData(receivedMessage);
//            userConverter.updateEntity(userDataResponse, user);
//            userRepository.save(user);
            Optional<UserBD> optional = userRepository.findByPhone(receivedMessage);
            if (optional.isPresent()) {
                List<UserStatus> statusList = statusRepository.findByUserBDOrderByLastUpdateDesc(optional.get());
                String string = toStringServices.toStringNonNullFields(optional.get()) +
                        SEPARATOR + SEPARATOR + "Statuses: " +
                        toStringServices.toStringIterableNonNull(statusList, false);
                sendMessage(string);
                return;
            }
        }
        if (receivedMessage.matches("000[0-9]{6}")) {
            TaskDataResponse taskResponse = api1C.getTaskByCode(receivedMessage);
            if (taskResponse != null && taskResponse.isResult()) {
                Task task = taskConverter.convertToEntity(taskResponse.getTask());
                taskRepository.save(task);
                sendMessage(task.toString(true), Buttons.getInlineMarkupEditTask(task));
                return;
            }
        }
        sendMessage(Message.getDefaultMessageError(user.getPerson().getFirstName()));
    }

    private void exit() {
        commandCacheList = commandCacheList.isEmpty() ?
                commandCacheRepository.findByUserBDOrderById(user) :
                commandCacheList;
        subCommandEnd(CommandStatus.INTERRUPTED_BY_USER);
    }

    private void exit(String message) {
        UserStatus status = statusRepository.findFirstByUserBDOrderByLastUpdateDesc(user);
        sendMessage(message, Buttons.inlineMarkupDefault(status == null ? UserType.UNAUTHORIZED : status.getUserType()));
        exit();
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

    private void convertSumTo() {
        text = text + "00";
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

            log.info(toStringServices.toStringNonNullFields(commandCacheList, true));
            commandCacheRepository.deleteByUserBD(user);
            commandCacheList.clear();
            text = null;
//            chatId = 0L;
//            user = null;
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
        PartnerDataResponse response = text.matches(REGEX_INN) ? api1C.getPartnerData(text) : api1C.getPartnerByGuid(text);
        PartnerListData data = createDataByCompanyDataResponse(response);
        Partner partner = data.getPartners() == null || data.getPartners().isEmpty() ?
                createLegalByInnFromDaData() :
                data.getPartners().get(0);
        partnerRepository.save(partner);
        return partner;
    }

    private PartnerData getPartnerDateByAPI(String text) {
        PartnerDataResponse response = text.matches(REGEX_INN) ? api1C.getPartnerData(text) : api1C.getPartnerByGuid(text);
        PartnerListData data = createDataByCompanyDataResponse(response);
        PartnerData partnerData = new PartnerData();

        if (data.getPartners().isEmpty()) {
            Partner partner = createLegalByInnFromDaData();
            partnerData.setPartner(partnerRepository.save(partner));
            partnerData.setContract(contractRepository.save(new Contract(partner)));
            return partnerData;
        }
        Partner partner = data.getPartners().get(0);
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
            partner.setCommencement(Converter1C.convertLongToLocalDateTime(data.getState().getRegistrationDate()));
            partner.setDateCertificate(Converter1C.convertLongToLocalDateTime(data.getOgrnDate()));
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
        return commandCacheList.size() == 0 ?
                new CommandCache() :
                commandCacheList.get(commandCacheList.size() - 1);
    }

    private CommandCache getCommandCache(String message, String nameCommand, String nameSubCommand) {
        if (commandCacheList.isEmpty()) {
            addNewSubCommand(nameCommand, nameSubCommand);
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
        CommandCache command = commandCacheList.isEmpty() ? addNewSubCommand(nameCommand, subCommand) : getCommandCache();
        return isStart(command, message);
    }

    private void completeSubCommand(CommandCache command, String nextSubCommand, String result) {

        CommandCache commandCache = new CommandCache();
        commandCache.setSubCommand(nextSubCommand);
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

    private List<UserStatus> updateUserAfterRequestAPI(UserDataResponse userData) {

        userConverter.updateEntity(userData, user);
        userRepository.save(user);

        if (userData.getTaskList() != null && !userData.getTaskList().isEmpty()) {
            createTasks(userData.getTaskList());
        }

        if (userData.getPartnerListData() != null) {
            createDataByCompanyDataResponse(userData.getPartnerListData());
        }

        if (userData.getStatusList() == null || userData.getStatusList().isEmpty()) {
            return statusRepository.findByUserBDAndLegalNotNull(user);
        }

        statusRepository.deleteByUserBD(user);
        return userData.getStatusList().stream()
                .map(statusResponse -> {
                    Optional<Partner> optional = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(statusResponse.getGuid());
                    Partner partner = optional.orElseGet(() -> getLegalEntityByGuid(statusResponse.getGuid()));
                    return createUserStatus(partner, statusResponse.getPost());
                })
                .collect(Collectors.toList());
    }

    private Partner getLegalEntityByGuid(String guid) {
        PartnerDataResponse dataResponse = api1C.getPartnerByGuid(guid);
        PartnerListData data = createDataByCompanyDataResponse(dataResponse);
        return data.getPartners().isEmpty() ? null : data.getPartners().get(0);
    }

    private PartnerListData createDataByCompanyDataResponse(PartnerDataResponse dataResponse) {
        PartnerListData data = new PartnerListData();
        if (dataResponse != null && dataResponse.isResult()) {
            data.setPartners(createLegalEntities(dataResponse.getPartners()));
            data.setDepartments(createDepartments(dataResponse.getDepartments()));
            data.setContracts(createContracts(dataResponse.getContracts()));
            data.setBalances(updateBalance(dataResponse.getBalance()));
        }
        return data;
    }

    private List<LegalBalance> updateBalance(List<BalanceResponse> list) {
        return list.stream()
                .map(balanceService::updateLegalBalance)
                .collect(Collectors.toList());
    }

    private List<Contract> createContracts(List<ContractResponse> list) {
        Map<String, List<ContractResponse>> groupedByGuidPartner = list.stream()
                .collect(Collectors.groupingBy(ContractResponse::getGuidPartner));
        List<Contract> saveContracts = new ArrayList<>();

        for (String key : groupedByGuidPartner.keySet()) {

            Optional<Partner> optional = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(key);
            if (optional.isEmpty()) {
                continue;
            }
            Partner partner = optional.get();
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

    private List<Partner> createLegalEntities(List<PartnerResponse> list) {
        return list.stream()
                .map(partnerConverter::convertToEntity)
                .map(partner -> partnerRepository.save((Partner) partner))
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

    private CommandCache addNewSubCommand(String name, String subCommand, String result) {
        CommandCache command = new CommandCache();
        command.setCommand(name);
        command.setSubCommand(subCommand);
        command.setUserBD(user);
        command.setCountStep(0L);
        command.setResult(result);
        commandCacheList.add(command);
        return command;
    }

    private CommandCache addNewSubCommand(String name, String subCommand) {
        return addNewSubCommand(name, subCommand, null);
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
}
