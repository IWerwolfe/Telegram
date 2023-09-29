package com.telegrambot.app.services;

import com.telegrambot.app.DTO.SubCommandInfo;
import com.telegrambot.app.DTO.TaskListToSend;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataListResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocResponse;
import com.telegrambot.app.DTO.api.other.SyncDataResponse;
import com.telegrambot.app.DTO.api.other.UserResponse;
import com.telegrambot.app.DTO.api.reference.legal.contract.ContractResponse;
import com.telegrambot.app.DTO.api.reference.legal.department.DepartmentResponse;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerDataResponse;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerListData;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerResponse;
import com.telegrambot.app.DTO.api.typeОbjects.DataResponse;
import com.telegrambot.app.DTO.dadata.DaDataParty;
import com.telegrambot.app.DTO.message.Message;
import com.telegrambot.app.DTO.types.Currency;
import com.telegrambot.app.DTO.types.PaymentType;
import com.telegrambot.app.DTO.types.SortingTaskType;
import com.telegrambot.app.DTO.types.TaskType;
import com.telegrambot.app.components.Buttons;
import com.telegrambot.app.config.BotConfig;
import com.telegrambot.app.model.EntitySavedEvent;
import com.telegrambot.app.model.balance.PartnerBalance;
import com.telegrambot.app.model.balance.UserBalance;
import com.telegrambot.app.model.command.Command;
import com.telegrambot.app.model.command.CommandCache;
import com.telegrambot.app.model.command.CommandStatus;
import com.telegrambot.app.model.documents.doc.payment.CardDoc;
import com.telegrambot.app.model.documents.doc.service.TaskDoc;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.reference.legalentity.Contract;
import com.telegrambot.app.model.reference.legalentity.Department;
import com.telegrambot.app.model.reference.legalentity.LegalEntity;
import com.telegrambot.app.model.reference.legalentity.Partner;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.model.types.Reference;
import com.telegrambot.app.model.user.UserStatus;
import com.telegrambot.app.model.user.UserType;
import com.telegrambot.app.repositories.balance.PartnerBalanceRepository;
import com.telegrambot.app.repositories.balance.UserBalanceRepository;
import com.telegrambot.app.repositories.command.CommandCacheRepository;
import com.telegrambot.app.repositories.command.CommandRepository;
import com.telegrambot.app.repositories.doc.CardDocRepository;
import com.telegrambot.app.repositories.doc.TaskDocRepository;
import com.telegrambot.app.repositories.reference.ContractRepository;
import com.telegrambot.app.repositories.reference.DepartmentRepository;
import com.telegrambot.app.repositories.reference.PartnerRepository;
import com.telegrambot.app.repositories.user.UserRepository;
import com.telegrambot.app.repositories.user.UserStatusRepository;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotCommandsImpl implements BotCommands {
    private final PartnerBalanceRepository partnerBalanceRepository;
    private final UserBalanceRepository userBalanceRepository;

    private final CardDocRepository cardDocRepository;
    private final TaskDocRepository taskDocRepository;
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

    private final CardDocConverter cardDocConverter;
    private final PartnerConverter partnerConverter;
    private final DepartmentConverter departmentConverter;
    private final ContractConverter contractConverter;
    private final UserBDConverter userConverter;
    private final TaskDocConverter taskDocConverter;
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
    private com.telegrambot.app.model.user.UserBD user;

    public void botAnswerUtils(String receivedMessage, long chatId, com.telegrambot.app.model.user.UserBD userBD) {
        this.chatId = chatId;
        this.user = userBD;
        botAnswerUtils(receivedMessage);
    }

    public void botAnswerUtils(List<CommandCache> commandCacheList, String text, long chatId, com.telegrambot.app.model.user.UserBD userBD) {
        this.commandCacheList = commandCacheList;
        this.text = text;
        this.user = userBD;
        this.chatId = chatId;

        if (text != null && text.equals("/exit")) {
            comExit(Message.getExitCommand(getCommandCache().getCommand()));
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
            case "/start" -> comStartBot();
            case "/help" -> comSendHelpText();
            case "/send_contact" -> comGetContact();
            case "/afterRegistered" -> comAfterRegistered();
            case "/registrationSurvey" -> comRegistrationSurvey();
            case "/getUserByPhone" -> comGetUserByPhone();
            case "/getByInn" -> comGetByInn();
            case "/get_task" -> comGetTask();
            case "/need_help" -> comCreateAssistance();
            case "/create_task" -> comCreateTask();
            case "getTask" -> comTaskPresentation();
            case "descriptor" -> editDescriptionTask();
            case "comment" -> comEditCommentTask();
            case "cancel" -> comEditCancelTask();
            case "/add_balance" -> comAddBalance();
            case "/get_balance" -> comGetBalance();
            case "pay" -> comPayTask();
//            case "/need_help" -> createAssistance();
            case "/exit" -> comExit();
            default -> comSendDefault(receivedMessage);
        }
        if (!commandCacheList.isEmpty()) {
            commandCacheRepository.saveAll(commandCacheList);
        }
        text = null;
    }

    //Command
    private void comStartBot() {

        UserStatus status = statusRepository.findFirstByUserBDOrderByLastUpdateDesc(user);
        boolean nonRegistered = (status == null || status.getUserType() == UserType.UNAUTHORIZED);
        boolean phoneFilled = (user.getPhone() != null && !user.getPhone().isEmpty());

        if (nonRegistered && phoneFilled) {
            List<UserStatus> statuses = subUpdateUserByAPI();
            status = statuses.isEmpty() ? null : statuses.get(0);
        }
        UserType type = status == null ? UserType.UNAUTHORIZED : status.getUserType();
        sendMessage(Message.getWelcomeMessage(), Buttons.inlineMarkupDefault(type));
    }

    private void comSendHelpText() {
        sendMessage(BotCommands.HELP_TEXT);
    }

    private void comGetContact() {
        sendMessage(Message.getBeforeSendingPhone(), Buttons.getContact());
    }

    private void comAfterRegistered() {
        List<UserStatus> statusList = subUpdateUserByAPI();
        UserType type = statusList.isEmpty() ? UserType.UNAUTHORIZED : statusList.get(0).getUserType();

        if (type == UserType.UNAUTHORIZED) {
            comRegistrationSurvey();
            return;
        }

        String message = Message.getAfterSendingPhone(user.getPerson().getFirstName(), statusList);
        sendMessage(message, Buttons.inlineMarkupDefault(type));
    }

    private void comRegistrationSurvey() {
        CommandCache command = getCommandCache(Message.getBeforeSurvey(), "/registrationSurvey", "getInn");
        SubCommandInfo info = new SubCommandInfo(this::comRegistrationSurvey);
        switch (command.getSubCommand()) {
            case "getInn" -> subGetInn(command, info, "getUserName");
            case "getUserName" -> subGetFio(command, info, "getPost");
            case "getPost" -> subGetPost(command, info, "update");
            case "update" -> subUpdateUserInfo();
            case "end" -> subEnd();
            default -> comSendDefault("");
        }
    }

    private void comGetUserByPhone() {
        CommandCache command = getCommandCache("/getUserByPhone", "getPhone");
        SubCommandInfo info = new SubCommandInfo(this::comGetUserByPhone);
        switch (command.getSubCommand()) {
            case "getPhone" -> subGetPhone(command, info, "getUserData");
            case "getUserData" -> {
                UserResponse response = api1C.getUserData(text);
                String message = isCompleted(response) ?
                        toStringServices.toStringNonNullFields(response, false) :
                        Message.getNonFindPhone();
                sendMessage(message);
                completeSubCommand(command, "end");
                comGetUserByPhone();
            }
            case "end" -> subEnd();
            default -> comSendDefault("");
        }
    }

    private void comGetByInn() {
        CommandCache command = getCommandCache("/getByInn", "getInn");
        SubCommandInfo info = new SubCommandInfo(this::comGetByInn);
        switch (command.getSubCommand()) {
            case "getInn" -> subGetInn(command, info, "getPartnerData");
            case "getPartnerData" -> {
                DaDataParty daDataParty = daDataService.getCompanyDataByINN(text);
                String message = daDataParty != null ?
                        toStringServices.toStringNonNullFields(daDataParty, true) :
                        Message.getNonFindPhone();
                sendMessage(message);
                completeSubCommand(command, "end");
                comGetByInn();
            }
            case "end" -> subEnd();
            default -> comSendDefault("");
        }
    }

    private void comGetTask() {
        List<UserStatus> statuses = statusRepository.findByUserBDOrderByLastUpdateDesc(user);
        switch (statuses.size()) {
            case 0 -> sendMessage(Message.getSearchErrors());
            case 1 -> subSendTaskList(getSortedTask(getTaskListToSend(statuses.get(0))));
            default -> statuses.forEach(s -> {
                TaskListToSend taskListToSend = getTaskListToSend(s);
                sendMessage(Message.getSearch(getNameEntity(s.getLegal()), taskListToSend.getTaskDocs().size()));
                subSendTaskList(getSortedTask(taskListToSend));
            });
        }
    }

    private void comCreateAssistance() {
        String message = Message.getStartCreateAssistance(user.getPerson().getFirstName());
        CommandCache command = getCommandCache(message, "/need_help", "getTopic");
        SubCommandInfo info = new SubCommandInfo(this::comCreateAssistance);
        switch (command.getSubCommand()) {
            case "getTopic" -> {
                info.setStartMessage(Message.getStartTopic());
                info.setNextSumCommand("getDescription");
                subGetTextInfo(command, info);
            }
            case "getDescription" -> subGetDescription(command, info, "getPhone");
            case "getPhone" -> subGetPhone(command, info, "getUserName");
            case "getUserName" -> subGetName(command, info, "createTask");
            case "createTask" -> subCreateTask();
            case "end" -> subEnd();
            default -> comSendDefault("");
        }
    }

    private void comCreateTask() {
        String message = Message.getStartCreateAssistance(user.getPerson().getFirstName());
        CommandCache command = getCommandCache(message, "/create_task", "getPartner");
        switch (command.getSubCommand()) {
            case "getPartner" -> subGetPartner(command, this::comCreateTask);
            case "getDepartment" -> subGetDepartment(command, this::comCreateTask);
            case "getDescription" -> subGetDescription(command, "createTask", this::comCreateTask);
            case "createTask" -> subCreateTask();
            case "end" -> subEnd();
            default -> comSendDefault("");
        }
    }

    private void comTaskPresentation() {
        TaskDoc taskDoc = subGetTaskDoc(getTaskCode());
        String message = taskDoc == null ? "TaskDoc not found" : taskDoc.toString(true);
        InlineKeyboardMarkup keyboard = taskDoc == null ? null : Buttons.getInlineMarkupEditTask(taskDoc);
        sendMessage(message, keyboard);
    }

    private void comEditCommentTask() {
        editTask(Message.getEditTextTask("комментарий"), "comment", this::comEditCommentTask);
    }

    private void comEditCancelTask() {
        editTask(Message.getWhenCancelTask(), "cancel", this::comEditCancelTask);
    }

    private void comAddBalance() {
        CommandCache command = getCommandCache("", "/add_balance", "getSum");
        if (command.getSubCommand().equalsIgnoreCase("getSum")) {
            SubCommandInfo info = new SubCommandInfo(Message.getInputSum(),
                    Message.errorWhenEditTask(),
                    this::comAddBalance,
                    "getFormOfPayment",
                    "[0-9]{3,}",
                    this::convertSumTo);
            subGetTextInfo(command, info);
            return;
        }
        addPayment(command, this::comAddBalance, "Внесение денег на баланс", "id: " + user.getId());
    }

    private void comGetBalance() {
        List<PartnerBalance> balances = getBalances();
        if (balances == null || balances.isEmpty()) {
            sendMessage(Message.getBalanceUser(getBalanceByUser()));
            return;
        }
        StringBuilder text = new StringBuilder();
        for (PartnerBalance balance : balances) {
            text.append(Message.getBalanceLegal(balance.getPartner(), balance.getAmount()))
                    .append(SEPARATOR);
        }
        sendMessage(text.toString());
    }

    private void comPayTask() {
        CommandCache command = getCommandCache("", "pay", "getSum");
        if (command.getSubCommand().equalsIgnoreCase("getSum")) {

            TaskDoc taskDoc = subGetTaskDoc(text);
            if (taskDoc == null) return;

            addNewSubCommand("pay", "taskCode", text);
            completeSubCommand(command, "getFormOfPayment", String.valueOf(taskDoc.getTotalAmount()));
            command = getCommandCache();
        }
        addPayment(command, this::comPayTask, "Оплата здачи", getResultSubCommandFromCache("taskCode"));
    }

    private void comExit() {
        commandCacheList = commandCacheList.isEmpty() ?
                commandCacheRepository.findByUserBDOrderById(user) :
                commandCacheList;
        subEnd(CommandStatus.INTERRUPTED_BY_USER);
    }

    private void comExit(String message) {
        UserStatus status = statusRepository.findFirstByUserBDOrderByLastUpdateDesc(user);
        sendMessage(message, Buttons.inlineMarkupDefault(status == null ? UserType.UNAUTHORIZED : status.getUserType()));
        comExit();
    }

    private void comSendDefault(String receivedMessage) {
        if (receivedMessage.matches(REGEX_INN)) {
            PartnerDataResponse response = api1C.getPartnerData(receivedMessage);
            PartnerListData data = createDataByPartnerDataResponse(response);
            sendMessage(toStringServices.toStringNonNullFields(data));
            return;
        }
        if (receivedMessage.matches(REGEX_PHONE)) {
//            UserResponse userDataResponse = api1C.getUserData(receivedMessage);
//            userConverter.updateEntity(userDataResponse, user);
//            userRepository.save(user);
            Optional<com.telegrambot.app.model.user.UserBD> optional = userRepository.findByPhone(receivedMessage);
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
            TaskDocDataResponse response = api1C.getTaskByCode(receivedMessage);
            if (isCompleted(response)) {
                TaskDoc taskDoc = taskDocConverter.convertToEntity(response.getEntity());
                taskDocRepository.save(taskDoc);
                sendMessage(taskDoc.toString(true), Buttons.getInlineMarkupEditTask(taskDoc));
                return;
            }
        }
        sendMessage(Message.getDefaultMessageError(user.getPerson().getFirstName()));
    }

    private void editTask(String message, String nameCommand, Runnable parent) {
        CommandCache command = getCommandCache("", nameCommand + ":", "code");
        String subCommand = "edit" + nameCommand;
        switch (command.getSubCommand()) {
            case "code" -> {
                completeSubCommand(command, subCommand, getTaskCode());
                runHandler(parent);
            }
            case "editTask" -> subEditTask(parent);
            case "end" -> subEnd();
            default -> {
                if (command.getSubCommand().equals(subCommand)) {
                    SubCommandInfo info = new SubCommandInfo(message,
                            Message.errorWhenEditTask(),
                            parent,
                            "editTask");
                    subGetTextInfo(command, info);
                }
            }
        }
    }

    private void addPayment(CommandCache command, Runnable parentRun, String title, String explanation) {
        switch (command.getSubCommand()) {
            case "getFormOfPayment" -> {
                SubCommandInfo info = new SubCommandInfo(parentRun);
                info.setNextSumCommand("getPay");
                info.setStartMessage(Message.getFormOfPayment());
                info.setErrorMessage(Message.errorWhenEditTask());
                info.setKeyboard(Buttons.getInlineByEnumFormOfPay(command.getCommand()));
                subGetTextInfo(command, info);
            }
            case "getPay" -> {
                SendInvoice invoice = subGetSendInvoice(title, explanation);
                parent.sendMessage(invoice);
                completeSubCommand(command, "createPayDoc");
            }
            case "createPayDoc" -> subCreatePayDoc();
            case "end" -> subEnd();
            default -> comSendDefault("");
        }
    }

    private void editDescriptionTask() {
        editTask(Message.getEditTextTask("описание"), "descriptor", this::editDescriptionTask);
    }

    //SubCommand

    private void subUpdateUserInfo() {
        if (commandCacheList.isEmpty()) {
            comExit(Message.getErrorToEditUserInfo());
            return;
        }
        String inn = getResultSubCommandFromCache("getInn");
        String fio = getResultSubCommandFromCache("getUserName");
        String post = getResultSubCommandFromCache("getPost");

        statusRepository.deleteByUserBD(user);
        PartnerData partnerData = getPartnerDateByAPI(inn);
        createUserStatus(UserType.USER, partnerData.getPartner(), post);

        UserBDConverter.updateUserFIO(fio, user.getPerson());
        userRepository.save(user);

        String message = Message.getSuccessfullyRegister(user.getPerson().getFirstName());
        sendMessage(message, Buttons.inlineMarkupDefault(UserType.USER));

        completeSubCommand(getCommandCache(), "end");
        comRegistrationSurvey();
    }

    private void subCreateTask() {
        if (commandCacheList.isEmpty()) {
            comExit(Message.getIncorrectTask());
            return;
        }
        TaskDoc doc = new TaskDoc();
        doc.setCreator(user);
        doc.setAuthor(user.getGuidEntity());
        doc.setStatus(TaskStatus.getDefaultInitialStatus());
        doc.setDescription(getDescriptionBySubCommand());
        doc.setType(TaskType.getDefaultType());
        fillPartnerData(doc);

        SyncDataResponse createResponse = api1C.createTask(taskDocConverter.convertToResponse(doc));
        if (isCompleted(createResponse)) {
            doc.setSyncData(new SyncData(createResponse.getGuid(), createResponse.getCode()));
        }
        taskDocRepository.save(doc);
        eventPublisher.publishEvent(new EntitySavedEvent(doc));
        sendMessage(Message.getSuccessfullyCreatingTask(doc));
//        sendToWorkGroup(doc.toString());

        completeSubCommand(getCommandCache(), "end");
        comCreateAssistance();
    }

    private void subGetPhone(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(Message.getStartGetPhone());
        info.setErrorMessage(Message.getUnCorrectGetPhone());
        info.setNextSumCommand(nextCommand);
        info.setRegex(REGEX_PHONE);
        subGetTextInfo(command, info);
    }

    private void subGetInn(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(Message.getStartINN());
        info.setErrorMessage(Message.getUnCorrectINN());
        info.setNextSumCommand(nextCommand);
        info.setRegex(REGEX_INN);
        subGetTextInfo(command, info);
    }

    private void subGetFio(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(Message.getStarFIO());
        info.setErrorMessage(Message.getUnCorrectFIO());
        info.setRegex(REGEX_FIO);
        info.setNextSumCommand(nextCommand);
        subGetTextInfo(command, info);
    }

    private void subGetName(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(Message.getStartName());
        info.setErrorMessage(Message.getErrorName());
        info.setNextSumCommand(nextCommand);
        subGetTextInfo(command, info);
    }

    private void subGetPost(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(Message.getStartPost());
        info.setNextSumCommand(nextCommand);
        subGetTextInfo(command, info);
    }

    private List<UserStatus> subUpdateUserByAPI() {

        UserResponse userData = api1C.getUserData(user.getPhone());
        if (!isCompleted(userData)) {
            return statusRepository.findByUserBDAndLegalNotNull(user);
        }

        userConverter.updateEntity(userData, user);
        userRepository.save(user);

        createTasks(userData.getTaskList());
        createDataByPartnerDataResponse(userData.getPartnerListData());

        if (userData.getStatusList() == null || userData.getStatusList().isEmpty()) {
            return statusRepository.findByUserBDAndLegalNotNull(user);
        }

        statusRepository.deleteByUserBD(user);
        return userData.getStatusList().stream()
                .map(statusResponse -> {
                    Optional<Partner> optional = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(statusResponse.getGuid());
                    Partner partner = optional.orElseGet(() -> getLegalEntityByGuid(statusResponse.getGuid()));
                    String post = statusResponse.getPost();
                    return createUserStatus(getUserTypeByPost(post), partner, post);
                })
                .collect(Collectors.toList());
    }

    private void subEnd(CommandStatus status) {
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

    private void subEnd() {
        subEnd(CommandStatus.COMPLETE);
        sendMessage("Команда упешно обработана");
    }

    private void subSendTaskList(Map<String, List<TaskDoc>> sortedTask) {
        if (sortedTask.isEmpty()) {
            sendMessage(Message.getSearchErrors());
            return;
        }
        sortedTask.keySet().forEach(sortName -> {
            List<TaskDoc> taskDocList = sortedTask.get(sortName);
            String message = Message.getSearchGrouping(sortName, taskDocList.size());
            ReplyKeyboard keyboard = Buttons.getInlineMarkupByTasks(taskDocList);
            sendMessage(message, keyboard);
        });
    }

    private void subGetTextInfo(CommandCache command, SubCommandInfo info) {

        boolean start = command.getCountStep() == 0;
        command.increment();

        if (start) {
            sendMessage(info.getStartMessage(), info.getKeyboard());
            return;
        }

        if (info.getRegex() != null && !text.matches(info.getRegex())) {
            sendMessage(info.getErrorMessage());
            return;
        }
        runHandler(info.getHandler());
        completeSubCommand(command, info.getNextSumCommand());
        runHandler(info.getParent());
    }

    private void subGetDescription(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(Message.getStartDescription());
        info.setErrorMessage(Message.getErrorDescription());
        info.setNextSumCommand(nextCommand);
        subGetTextInfo(command, info);
    }

    private void subGetDescription(CommandCache command, String nextCommand, Runnable parent) {
        SubCommandInfo info = new SubCommandInfo(parent);
        subGetDescription(command, info, nextCommand);
    }

    private void subGetPartner(CommandCache command, Runnable parent) {

        List<Partner> partners = getPartnerByUserStatus();

        if (partners.size() > 1) {
            SubCommandInfo info = new SubCommandInfo(parent);
            info.setNextSumCommand("getDepartment");
            info.setStartMessage(Message.getPartnersByList());
            info.setErrorMessage(Message.errorWhenEditTask());
            info.setKeyboard(Buttons.getInlineByRef("getPartner", partners));
            subGetTextInfo(command, info);
            return;
        }

        String value = partners.size() == 1 ? String.valueOf(partners.get(0).getId()) : null;
        completeSubCommand(command, "getDepartment", value);
        runHandler(parent);
    }

    private void subGetDepartment(CommandCache command, Runnable parent) {

        String result = getResultSubCommandFromCache("getPartner");

        if (result == null) {
            return;
        }

        Partner partner = partnerRepository.getReferenceById(Long.parseLong(result));
        List<Department> departments = getDepartmentsByUser(partner);

        if (departments.size() > 1) {
            SubCommandInfo info = new SubCommandInfo(parent);
            info.setNextSumCommand("getDescription");
            info.setStartMessage(Message.getDepartmentsByList());
            info.setErrorMessage(Message.errorWhenEditTask());
            info.setKeyboard(Buttons.getInlineByRef("getDepartment", departments));
            subGetTextInfo(command, info);
            return;
        }

        String value = departments.size() == 1 ? String.valueOf(departments.get(0).getId()) : null;
        completeSubCommand(command, "getDescription", value);
        runHandler(parent);
    }

    private SendInvoice subGetSendInvoice(String title, String explanation) {
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

    private void subCreatePayDoc() {
        if (commandCacheList.size() == 0 || text == null) {
            comExit(Message.getDefaultMessageError(user.getUserName()));
            return;
        }

        String[] strings = text.split(";");
        String ref = strings.length > 1 ? strings[1] : null;

        List<Partner> partners = getPartnerByUserStatus();
        Partner partner = partners.isEmpty() ? null : partners.get(0);

        CardDoc cardDoc = new CardDoc();
        cardDoc.setTotalAmount(Integer.valueOf(getResultSubCommandFromCache("getSum")));
        cardDoc.setPartnerData(partner);
        cardDoc.setReferenceNumber(ref);
        cardDoc.setAuthor(user.getGuidEntity());
        cardDoc.setCreator(user);
        cardDoc.setPaymentType(PaymentType.INCOMING);
        cardDocRepository.save(cardDoc);
        eventPublisher.publishEvent(new EntitySavedEvent(cardDoc));

        SyncDataResponse createResponse = api1C.updateTask(cardDocConverter.convertToResponse(cardDoc));
        if (isCompleted(createResponse)) {
            cardDoc.setSyncData(new SyncData(createResponse.getGuid(), createResponse.getCode()));
        }

        sendMessage(Message.getSuccessfullyCreatingCardDoc(cardDoc));
        completeSubCommand(getCommandCache(), "end");
        comAddBalance();
    }

    private void subEditTask(Runnable parent) {
        if (commandCacheList.isEmpty()) {
            comExit(Message.getIncorrectTask());
            return;
        }

        TaskDoc taskDoc = subGetTaskDoc(getResultSubCommandFromCache("code"));
        if (taskDoc == null) return;
        String prefix = "Дополнено " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + " -> ";
        taskDoc.setDescription(getResultFromSubCommand("editDescriptor", taskDoc.getDescription(), prefix));
        taskDoc.setComment(getResultFromSubCommand("editComment", taskDoc.getComment(), prefix));
        taskDoc.setDecision(getResultFromSubCommand("editDecision", taskDoc.getDecision(), prefix));
        String message = "";
        message = getResultSubCommandFromCache("editCancel");
        if (!message.isEmpty()) {
            closingTask(taskDoc, message, false, "Закрыто пользователем:");
        }
        message = getResultSubCommandFromCache("closed");
        if (!message.isEmpty()) {
            closingTask(taskDoc, message, true);
        }
        taskDocRepository.save(taskDoc);

        SyncDataResponse createResponse = api1C.updateTask(taskDocConverter.convertToResponse(taskDoc));
        if (isCompleted(createResponse)) {
            taskDoc.setSyncData(new SyncData(createResponse.getGuid(), createResponse.getCode()));
        }

        sendMessage(Message.getSuccessfullyEditTask(taskDoc));
        completeSubCommand(getCommandCache(), "end");
        runHandler(parent);
    }

    private TaskDoc subGetTaskDoc(String code) {
        Optional<TaskDoc> optional = taskDocRepository.findById(Long.valueOf(code));
        if (optional.isPresent()) {
            return optional.get();
        }
        comExit(Message.getIncorrectTask());
        return null;
    }

    private List<TaskDoc> createTasks(List<TaskDocResponse> list) {
        return list == null || list.isEmpty() ?
                new ArrayList<>() :
                taskDocRepository.saveAll(
                        list.stream()
                                .map(response -> (TaskDoc) taskDocConverter.convertToEntity(response)).toList());
    }

    private List<PartnerBalance> getBalances() {
        List<Partner> partners = getPartnerByUserStatus();
        return partnerBalanceRepository.findByPartnerInOrderByPartner_NameAsc(partners);
    }

    private int getBalanceByUser() {
        Optional<UserBalance> optional = userBalanceRepository.findByUser(user);
        return optional.isEmpty() ? 0 : optional.get().getAmount();
    }

    private List<Partner> getPartnerByUserStatus() {
        List<UserStatus> userStatuses = statusRepository.findByUserBD(user);
        return userStatuses.stream().map(status -> (Partner) status.getLegal()).toList();
    }

    private List<Department> getDepartmentsByUser(Partner partner) {
        List<UserStatus> userStatuses = statusRepository.findByUserBDAndLegal(user, partner);
        List<Department> departments = userStatuses.stream()
                .map(UserStatus::getDepartment)
                .filter(Objects::nonNull)
                .toList();
        return departments.isEmpty() ? departmentRepository.findByPartner(partner) : departments;
    }


    private String getResultFromSubCommand(String nameSubCommand, String field, String prefix) {
        String message = getResultSubCommandFromCache(nameSubCommand);
        if (message.isEmpty()) {
            return field;
        }
        return field == null || field.isEmpty() ? message : prefix + SEPARATOR + message + SEPARATOR + field;
    }

    private void closingTask(TaskDoc taskDoc, String message, boolean successfully) {
        taskDoc.setStatus(TaskStatus.getDefaultClosedStatus());
        taskDoc.setClosingDate(LocalDateTime.now());
        taskDoc.setSuccessfully(successfully);
        String decision = taskDoc.getDecision();
        taskDoc.setDecision(decision == null || decision.isEmpty() ? message : message + SEPARATOR + decision);
    }

    private void closingTask(TaskDoc taskDoc, String message, boolean successfully, String prefix) {
        message = prefix.isEmpty() ? message : prefix + SEPARATOR + message;
        closingTask(taskDoc, message, successfully);
    }

    private String getTaskCode() {
        return "0".repeat(9 - text.length()) + text;
    }


    private Map<String, List<TaskDoc>> getSortedTask(TaskListToSend taskListToSend) {
        return taskListToSend.getTaskDocs().stream()
                .collect(Collectors.groupingBy(task -> getStringSorting(taskListToSend, task)));
    }

    private String getStringSorting(TaskListToSend taskListToSend, TaskDoc taskDoc) {
        return switch (taskListToSend.getSorting()) {
            case STATUS -> getNameEntity(taskDoc.getStatus());
            case PARTNER -> getNameEntity(taskDoc.getPartnerData().getPartner());
            default -> getNameEntity(taskDoc.getPartnerData().getDepartment());
        };
    }

    private TaskListToSend getTaskListToSend(UserStatus status) {
        List<TaskDoc> taskDocs = new ArrayList<>();
        SortingTaskType sortingType = SortingTaskType.DEPARTMENT;
        switch (status.getUserType()) {
            case UNAUTHORIZED, USER -> {
                if (TaskStatus.getDefaultClosedStatus() != null) {
                    taskDocs = getTaskListByApiByUser();
                }
            }
            case ADMINISTRATOR -> taskDocs = status.getDepartment() == null ?
                    getTaskListByApiByCompany(status.getLegal()) :
                    getTaskListByApiByDepartment(status.getDepartment());
            case DIRECTOR -> taskDocs = getTaskListByApiByCompany(status.getLegal());
            default -> {
                if (user.getIsMaster()) {
                    //TODO Написать связь Manager и UserResponse
//                    taskDocs = getTaskListByApiByManager();
//                    sortingType = SortingTaskType.PARTNER;
                }
            }
        }
        return new TaskListToSend(sortingType, taskDocs);
    }

    private List<TaskDoc> getTaskListByApiByManager(Manager manager) {
        TaskDocDataListResponse response = api1C.getTaskListDataByManager(user.getGuidEntity());
        return isCompleted(response) ?
                createTasks(response.getList()) :
                taskDocRepository.findByManagerAndStatusNotOrderByDateAsc(manager, TaskStatus.getDefaultClosedStatus());
    }

    private List<TaskDoc> getTaskListByApiByCompany(LegalEntity legal) {
        TaskDocDataListResponse response = api1C.getTaskListDataByCompany(Converter.convertToGuid(legal));
        return isCompleted(response) ?
                createTasks(response.getList()) :
                taskDocRepository.findByPartnerDataNotNullAndPartnerData_PartnerAndStatusNotOrderByDateAsc((Partner) legal,
                        TaskStatus.getDefaultClosedStatus());
    }

    private List<TaskDoc> getTaskListByApiByDepartment(Department department) {
        TaskDocDataListResponse response = api1C.getTaskListDataByDepartment(Converter.convertToGuid(department));
        return isCompleted(response) ?
                createTasks(response.getList()) :
                taskDocRepository.findByPartnerDataNotNullAndPartnerData_DepartmentAndStatusNotOrderByDateAsc(department,
                        TaskStatus.getDefaultClosedStatus());
    }

    private List<TaskDoc> getTaskListByApiByUser() {
        if (user.getGuidEntity() == null) {
            return taskDocRepository.
                    findByCreatorAndStatusNotOrderByDateAsc(user, TaskStatus.getDefaultClosedStatus());
        }
        TaskDocDataListResponse response = api1C.getTaskListDataByUser(user.getGuidEntity());
        return isCompleted(response) ?
                createTasks(response.getList()) :
                taskDocRepository.
                        findByCreatorAndStatusNotOrderByDateAsc(user, TaskStatus.getDefaultClosedStatus());
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

    private void fillPartnerData(TaskDoc doc) {

        String idPartner = getResultSubCommandFromCache("getPartner");

        if (idPartner.isEmpty()) {
            UserResponse userData = api1C.getUserData(getResultSubCommandFromCache("getPhone"));
            if (userData != null && userData.getStatusList() != null && userData.getStatusList().size() >= 1) {
                PartnerData partnerData = getPartnerDateByAPI(userData.getStatusList().get(0).getGuid());
                doc.setPartnerData(partnerData);
                return;
            }
        }

        Partner partner = partnerRepository.findById(Long.valueOf(idPartner)).orElse(null);
        String idDepartment = getResultSubCommandFromCache("getDepartment");
        Department department = idDepartment.isEmpty() ?
                null :
                departmentRepository.findById(Long.valueOf(idDepartment)).orElse(null);
        doc.setPartnerData(partner, department);
    }

    private String getResultSubCommandFromCache(String subCommand) {
        List<String> result = commandCacheList.stream()
                .filter(command -> command.getSubCommand().equalsIgnoreCase(subCommand) && command.getResult() != null)
                .map(CommandCache::getResult).toList();
        return result.isEmpty() ? "" : result.get(0).replaceAll(".*:", "");
    }

    private void convertSumTo() {
        text = text + "00";
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

    private PartnerData getPartnerDateByAPI(String text) {

        PartnerDataResponse response = api1C.getPartnerData(text);
        PartnerData partnerData = new PartnerData();

        if (isCompleted(response)) {
            PartnerListData data = createDataByPartnerDataResponse(response);
            Partner partner = data.getPartners().get(0);
            partnerData.setPartner(partner);
            partnerData.setContract(data.getContracts().isEmpty() ? contractRepository.save(new Contract(partner)) : data.getContracts().get(0));
            partnerData.setDepartment(data.getDepartments().size() == 1 ? data.getDepartments().get(0) : null);
            return partnerData;
        }

        Partner partner = createLegalByInnFromDaData();
        partnerData.setPartner(partnerRepository.save(partner));
        partnerData.setContract(contractRepository.save(new Contract(partner)));
        return partnerData;
    }

    private Partner getLegalEntityByGuid(String guid) {
        PartnerDataResponse dataResponse = api1C.getPartnerByGuid(guid);
        PartnerListData data = createDataByPartnerDataResponse(dataResponse);
        return data.getPartners().isEmpty() ? null : data.getPartners().get(0);
    }

    private PartnerListData createDataByPartnerDataResponse(PartnerDataResponse dataResponse) {
        PartnerListData data = new PartnerListData();
        if (isCompleted(dataResponse)) {
            data.setPartners(createLegalEntities(dataResponse.getPartners()));
            data.setDepartments(createDepartments(dataResponse.getDepartments()));
            data.setContracts(createContracts(dataResponse.getContracts()));
            data.setBalances(updateBalance(dataResponse.getBalance()));
        }
        return data;
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


    private Partner createLegalByInnFromDaData() {
        DaDataParty data = daDataService.getCompanyDataByINN(text);
        Partner partner = new Partner();
        partner.setInn(text);
        partner.setComment("created automatically on " + LocalDateTime.now().format(formatter));
        if (data != null) {
            partner.setName(data.getName().getShortWithOpf());
            partner.setKpp(data.getKpp());
            partner.setOGRN(data.getOgrn());
            partner.setCommencement(Converter.convertLongToLocalDateTime(data.getState().getRegistrationDate()));
            partner.setDateCertificate(Converter.convertLongToLocalDateTime(data.getOgrnDate()));
            partner.setOKPO(data.getOkpo());
        }
        return partner;
    }

    private String getDescriptionBySubCommand() {
        String topic = getResultSubCommandFromCache("getTopic");
        String desc = getResultSubCommandFromCache("getDescription");
        String phone = getResultSubCommandFromCache("getPhone");
        String name = getResultSubCommandFromCache("getUserName");

        StringBuilder description = new StringBuilder();
        if (!topic.isEmpty()) {
            description.append(topic).append(SEPARATOR);
        }
        if (!desc.isEmpty()) {
            description.append(desc);
        }
        if (!phone.isEmpty()) {
            description.append(SEPARATOR).append(phone);
        }
        if (!name.isEmpty()) {
            description.append(" (").append(getResultSubCommandFromCache("getUserName")).append(")");
        }
        return description.toString();
    }

    private void sendMessage(String message) {
        sendMessage(message, null);
    }

    private void sendMessage(String message, ReplyKeyboard keyboard) {
        try {
            SendMessage sendMessage = createMessage(message, keyboard);
            parent.sendMessage(sendMessage);
        } catch (Exception e) {
            log.error("an error occurred while sending a message to the user {} \r\nError: {}",
                    user.getId(),
                    e.getMessage());
        }
    }

    private CommandCache getCommandCache() {
        return commandCacheList.isEmpty() ?
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

    private CommandCache getCommandCache(String nameCommand, String nameSubCommand) {
        return commandCacheList.isEmpty() ?
                addNewSubCommand(nameCommand, nameSubCommand) :
                commandCacheList.get(commandCacheList.size() - 1);
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
        parent.sendMessage(message);
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

    private List<PartnerBalance> updateBalance(List<com.telegrambot.app.DTO.api.balance.BalanceResponse> list) {
        return list.stream()
                .map(balanceService::updateLegalBalance)
                .collect(Collectors.toList());
    }


    private static <R extends DataResponse> boolean isCompleted(R response) {
        return response != null && response.isResult();
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
