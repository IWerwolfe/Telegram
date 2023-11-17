package com.telegrambot.app.services;

import com.telegrambot.app.DTO.SubCommandInfo;
import com.telegrambot.app.DTO.TaskListToSend;
import com.telegrambot.app.DTO.api.balance.BalanceResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataListResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataResponse;
import com.telegrambot.app.DTO.api.other.SyncDataResponse;
import com.telegrambot.app.DTO.api.other.UserResponse;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerDataResponse;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerListData;
import com.telegrambot.app.DTO.api.typeОbjects.DataResponse;
import com.telegrambot.app.DTO.dadata.DaDataParty;
import com.telegrambot.app.DTO.message.Message;
import com.telegrambot.app.DTO.types.Currency;
import com.telegrambot.app.DTO.types.PartnerType;
import com.telegrambot.app.DTO.types.PaymentType;
import com.telegrambot.app.DTO.types.SortingTaskType;
import com.telegrambot.app.components.Buttons;
import com.telegrambot.app.config.BotConfig;
import com.telegrambot.app.model.EntityDefaults;
import com.telegrambot.app.model.EntitySavedEvent;
import com.telegrambot.app.model.balance.PartnerBalance;
import com.telegrambot.app.model.command.Command;
import com.telegrambot.app.model.command.CommandCache;
import com.telegrambot.app.model.command.CommandStatus;
import com.telegrambot.app.model.documents.doc.payment.CardDoc;
import com.telegrambot.app.model.documents.doc.service.TaskDoc;
import com.telegrambot.app.model.documents.docdata.CardData;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.reference.legalentity.Contract;
import com.telegrambot.app.model.reference.legalentity.Department;
import com.telegrambot.app.model.reference.legalentity.LegalEntity;
import com.telegrambot.app.model.reference.legalentity.Partner;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.model.types.Reference;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import com.telegrambot.app.model.user.UserType;
import com.telegrambot.app.repositories.balance.PartnerBalanceRepository;
import com.telegrambot.app.repositories.command.CommandCacheRepository;
import com.telegrambot.app.repositories.command.CommandRepository;
import com.telegrambot.app.repositories.doc.CardDocRepository;
import com.telegrambot.app.repositories.doc.TaskDocRepository;
import com.telegrambot.app.repositories.reference.ContractRepository;
import com.telegrambot.app.repositories.reference.DepartmentRepository;
import com.telegrambot.app.repositories.reference.PartnerRepository;
import com.telegrambot.app.repositories.user.UserRepository;
import com.telegrambot.app.services.api.ApiDaDataService;
import com.telegrambot.app.services.api.ApiOutServiceImpl;
import com.telegrambot.app.services.converter.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final CardDocRepository cardDocRepository;
    private final TaskDocRepository taskDocRepository;
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final DepartmentRepository departmentRepository;
    private final ContractRepository contractRepository;
    private final CommandCacheRepository commandCacheRepository;
    private final CommandRepository commandRepository;
    private final ApiOutServiceImpl api1C;
    private final ApiDaDataService apiDaDataService;

    private final ApplicationEventPublisher eventPublisher;

    private final CardDocConverter cardDocConverter;
    private final PartnerConverter partnerConverter;
    private final DepartmentConverter departmentConverter;
    private final ContractConverter contractConverter;
    private final UserBDConverter userConverter;
    private final TaskDocConverter taskDocConverter;
    private final ToStringServices toStringServices;

    private final BalanceService balanceService;
    private final EntityDefaults entityDefaults;

    private final BotConfig bot;

    @Value("${pay.sbpStatic}")
    private String sbpStatic;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
    private final String REGEX_INN = "^[0-9]{10}|[0-9]{12}$";
    private final String REGEX_FIO = "^(?:[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?\\s+){1,2}[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?$";
    private final String REGEX_PHONE = "^79[0-9]{9}$";
    private static final String SEPARATOR = System.lineSeparator();
    private TelegramBotServices parent;
    private List<CommandCache> commandCacheList = new ArrayList<>();
    private String text;
    private String nameCommand;
    private long chatId;
    private UserBD user;

    public void botAnswerUtils(String receivedMessage, long chatId, UserBD userBD) {

        this.chatId = chatId;
        this.user = userBD;
        this.nameCommand = receivedMessage.trim();

        if (receivedMessage.equals("/exit") || receivedMessage.equals("Отмена") ||
                (text != null && (text.equals("/exit") || text.equals("Отмена")))) {
            comExit(Message.getExitCommand(nameCommand));
            return;
        }

        try {
            botAnswerUtils();
        } catch (Exception e) {
            log.error("Ошибка при выполнении команды: {}{}{}", nameCommand, SEPARATOR, e.getMessage());
            comExit(Message.getExitToErrors());
        }
    }

    public void botAnswerUtils(List<CommandCache> commandCacheList, String text, long chatId, UserBD userBD) {
        this.commandCacheList = commandCacheList;
        this.text = text;
        botAnswerUtils(getCommandCache().getCommand(), chatId, userBD);
    }

    public void botAnswerUtils() {

        if (nameCommand == null) {
            return;
        }

        String[] param = nameCommand.split(":");
        nameCommand = param.length > 0 ? param[0] : nameCommand;
        text = param.length > 1 && text == null ? param[1] : text;

        switch (nameCommand) {
            case "/start" -> comStartBot();
            case "/help" -> comSendHelpText();
            case "/send_contact", "Зарегистрироваться" -> comGetContact();
            case "/afterRegistered" -> comAfterRegistered();
            case "/registrationSurvey" -> comRegistrationSurvey();
            case "/getUserByPhone" -> comGetUserByPhone();
            case "/getByInn" -> comGetByInn();
            case "/get_task", "Посмотреть задачи" -> comGetTask();
            case "/need_help", "Создать обращение" -> comCreateAssistance();
            case "/create_task", "Создать задачу" -> comCreateTask();
            case "getTask" -> comTaskPresentation();
            case "descriptor" -> editDescriptionTask();
            case "comment" -> comEditCommentTask();
            case "cancel" -> comEditCancelTask();
            case "/add_balance", "Пополнить баланс" -> comAddBalance();
            case "/get_balance", "Проверить баланс" -> comGetBalance();
            case "pay" -> comPayTask();
//            case "/need_help" -> createAssistance();
            case "/exit", "Отмена" -> comExit();
            default -> comSendDefault();
        }
        if (!commandCacheList.isEmpty()) {
            commandCacheRepository.saveAll(commandCacheList);
        }
        text = null;
    }

    //Command
    private void comStartBot() {

        boolean nonRegistered = (user.getUserType() == UserType.UNAUTHORIZED);
        boolean phoneFilled = (user.getPhone() != null && !user.getPhone().isEmpty());

        if (nonRegistered && phoneFilled) {
            subUpdateUserByAPI();
        }
        sendMessage(Message.getWelcomeMessage(), Buttons.keyboardMarkupDefault(user.getUserType()));

        if (user.getUserType() != UserType.UNAUTHORIZED) {
            String message = Message.getAfterSendingPhone(user.getPerson().getFirstName(), user.getStatuses());
            sendMessage(message, Buttons.keyboardMarkupDefault(user.getUserType()));
        }
    }

    private void comSendHelpText() {
        sendMessage(BotCommands.HELP_TEXT);
    }

    private void comGetContact() {
        sendMessage(Message.getBeforeSendingPhone(), Buttons.getContact());
    }

    private void comAfterRegistered() {

        subUpdateUserByAPI();

        if (user.getUserType() == UserType.UNAUTHORIZED) {
            comRegistrationSurvey();
            return;
        }

        String message = Message.getAfterSendingPhone(user.getPerson().getFirstName(), user.getStatuses());
        sendMessage(message, Buttons.keyboardMarkupDefault(user.getUserType()));
    }

    private void comRegistrationSurvey() {
        CommandCache command = getCommandCache(Message.getBeforeSurvey(), "getInn");
        SubCommandInfo info = new SubCommandInfo(this::comRegistrationSurvey);
        switch (command.getSubCommand()) {
            case "getInn" -> subGetInn(command, info, "getUserName");
            case "getUserName" -> subGetFio(command, info, "getPost");
            case "getPost" -> subGetPost(command, info, "update");
            case "update" -> subUpdateUserInfo();
            case "end" -> subEnd();
            default -> comSendDefault();
        }
    }

    private void comGetUserByPhone() {
        CommandCache command = getCommandCache("getPhone");
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
            default -> comSendDefault();
        }
    }

    private void comGetByInn() {
        CommandCache command = getCommandCache("getInn");
        SubCommandInfo info = new SubCommandInfo(this::comGetByInn);
        switch (command.getSubCommand()) {
            case "getInn" -> subGetInn(command, info, "getPartnerData");
            case "getPartnerData" -> {
                Partner partner = createLegalByInnFromDaData();
//                DaDataParty daDataParty = apiDaDataService.getCompanyDataByINN(text);
//                String message = daDataParty != null ?
//                        toStringServices.toStringNonNullFields(daDataParty, true) :
//                        Message.getNonFindPhone();
                String message = toStringServices.toStringNonNullFields(partner, false);
                sendMessage(message);
                completeSubCommand(command, "end");
                comGetByInn();
            }
            case "end" -> subEnd();
            default -> comSendDefault();
        }
    }

    private void comGetTask() {
        List<UserStatus> statuses = user.getStatuses();
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
        CommandCache command = getCommandCache(message, "getTopic");
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
            default -> comSendDefault();
        }
    }

    private void comCreateTask() {
        String message = Message.getStartCreateAssistance(user.getPerson().getFirstName());
        CommandCache command = getCommandCache(message, "getPartner");
        SubCommandInfo info = new SubCommandInfo(this::comCreateTask);
        switch (command.getSubCommand()) {
            case "getPartner" -> subGetPartner(command, this::comCreateTask);
            case "getDepartment" -> subGetDepartment(command, this::comCreateTask);
            case "getDescription" -> subGetDescription(command, info, "createTask");
            case "createTask" -> subCreateTask();
            case "end" -> subEnd();
            default -> comSendDefault();
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
        CommandCache command = getCommandCache("getSum");
        switch (command.getSubCommand()) {
            case "getSum" -> {
                SubCommandInfo info = new SubCommandInfo(Message.getInputSum(),
                        Message.errorWhenEditTask(),
                        this::comAddBalance,
                        "getFormOfPayment",
                        "[0-9]{3,}",
                        this::convertSumTo);
                subGetTextInfo(command, info);
            }
            default -> addPayment(command, this::comAddBalance, "Внесение денег на баланс", "id: " + user.getId());
        }
    }

    private void comGetBalance() {
        List<PartnerBalance> balances = getBalances();
        if (balances == null || balances.isEmpty()) {
            sendMessage(Message.getBalanceUser(user.getBalance()));
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
        CommandCache command = getCommandCache("getSum");
        switch (command.getSubCommand()) {
            case "getSum" -> {
                TaskDoc taskDoc = subGetTaskDoc(text);
                if (taskDoc == null) return;

                addNewSubCommand("taskCode", text);
                completeSubCommand(command, "getFormOfPayment", String.valueOf(taskDoc.getTotalAmount()));
                comPayTask();
            }
            default -> addPayment(command, this::comPayTask, "Оплата здачи", getResultSubCommandFromCache("taskCode"));
        }
    }

    private void comExit() {
        commandCacheList = commandCacheList.isEmpty() ?
                commandCacheRepository.findByUserBDOrderById(user) :
                commandCacheList;
        subEnd(CommandStatus.INTERRUPTED_BY_USER);
    }

    private void comExit(String message) {
        sendMessage(message, Buttons.keyboardMarkupDefault(user.getUserType()));
        comExit();
    }

    private void comSendDefault() {
        if (nameCommand.matches(REGEX_INN)) {
            PartnerDataResponse response = api1C.getPartnerData(nameCommand);
            if (isCompleted(response)) {
                PartnerListData data = createDataByPartnerDataResponse(response);
                sendMessage(toStringServices.toStringNonNullFields(data));
                return;
            }
        }
        if (nameCommand.matches(REGEX_PHONE)) {
//            UserResponse userDataResponse = api1C.getUserData(receivedMessage);
//            userConverter.updateEntity(userDataResponse, user);
//            userRepository.save(user);
            Optional<UserBD> optional = userRepository.findByPhone(nameCommand);
            if (optional.isPresent()) {
                String string = toStringServices.toStringNonNullFields(optional.get()) +
                        SEPARATOR + SEPARATOR + "Statuses: " +
                        toStringServices.toStringIterableNonNull(optional.get().getStatuses(), false);
                sendMessage(string);
                return;
            }
        }
        if (nameCommand.matches("000[0-9]{6}")) {
            TaskDocDataResponse response = api1C.getTaskByCode(nameCommand);
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
        this.nameCommand = nameCommand + ":";
        CommandCache command = getCommandCache("code");
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
                subGetPay(command, title, explanation);
            }
            case "createPayDoc" -> subCreatePayDoc();
            case "end" -> subEnd();
            default -> comSendDefault();
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

        PartnerData partnerData = getPartnerDateByAPI(inn);
        UserStatus status = new UserStatus(user, UserType.USER, partnerData.getPartner(), post);

        user.setStatuses(List.of(status));
        UserBDConverter.updateUserFIO(fio, user.getPerson());
        userRepository.save(user);

        String message = Message.getSuccessfullyRegister(user.getPerson().getFirstName());
        sendMessage(message);

        completeSubCommand(getCommandCache(), "end");
        comRegistrationSurvey();
    }

    private void subGetPay(CommandCache command, String title, String explanation) {
        String formOfPay = getResultSubCommandFromCache("getFormOfPayment");
        switch (formOfPay) {
            case "SBP" -> {
                sendMessage(Message.getToPaySBP(sbpStatic));
                completeSubCommand(command, "end");
                comAddBalance();
            }
            case "CARD" -> {
                SendInvoice invoice = subGetSendInvoice(title, explanation);
                parent.sendMessage(invoice);
                completeSubCommand(command, "createPayDoc");
            }
            case "INVOICE" -> {
            }
            default -> comExit();
        }
    }

    private void subCreateTask() {
        if (commandCacheList.isEmpty()) {
            comExit(Message.getIncorrectTask());
            return;
        }
        TaskDoc doc = new TaskDoc();
        entityDefaults.fillDefaultData(doc);
        doc.setDescription(getDescriptionBySubCommand());
        doc.setPartnerData(fillPartnerData());

        SyncDataResponse createResponse = api1C.createTask(taskDocConverter.convertToResponse(doc));
        doc.setSyncData(createResponse);

        taskDocRepository.save(doc);
        eventPublisher.publishEvent(new EntitySavedEvent(doc));
        sendMessage(Message.getSuccessfullyCreatingTask(doc));
        sendToWorkGroup("Создана новая задача " + SEPARATOR + doc);

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

    private void subUpdateUserByAPI() {

        UserResponse userData = api1C.getUserData(user.getPhone());
        if (!isCompleted(userData)) {
            return;
        }

        if (!userData.getStatusList().isEmpty()) {
            updateUserStatus(userData);
        }

        userConverter.updateEntity(userData, user);
        userRepository.save(user);

        taskDocConverter.convertToEntityList(userData.getTaskList(), true);

        if (isCompleted(userData.getPartnerListData())) {
            createDataByPartnerDataResponse(userData.getPartnerListData());
        }
    }

    private void updateUserStatus(UserResponse userData) {
        List<UserStatus> statuses = userData.getStatusList()
                .stream()
                .map(statusResponse ->
                        new UserStatus(user, getPartnerByGuid(statusResponse.getGuid()), statusResponse.getPost()))
                .toList();
        user.setStatuses(statuses);
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

            commandCacheRepository.deleteByUserBD(user);
            commandCacheList.clear();
            text = null;
        }
    }

    private void subEnd() {
        subEnd(CommandStatus.COMPLETE);
        sendMessage("Команда упешно обработана", Buttons.keyboardMarkupDefault(user.getUserType()));
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

        Partner partner = partnerRepository.getReferenceById(Long.parseLong(result));
        List<Department> departments = partner.getDepartments();

        if (departments.size() > 1) {
            SubCommandInfo info = new SubCommandInfo(parent);
            info.setNextSumCommand("getDescription");
            info.setStartMessage(Message.getDepartmentsByList());
            info.setErrorMessage(Message.errorWhenEditTask());
            info.setKeyboard(Buttons.getInlineByRef("getDepartment", departments));
            subGetTextInfo(command, info);
            return;
        }

        String value = departments.size() == 1 ? String.valueOf(departments.get(0).getId()) : "";
        completeSubCommand(command, "getDescription", value);
        runHandler(parent);
    }

    private SendInvoice subGetSendInvoice(String title, String explanation) {
        SendInvoice invoice = new SendInvoice();
        invoice.setChatId(chatId);
        invoice.setTitle(title);
        invoice.setCurrency(Currency.RUB.name());
        invoice.setPayload("balance:" + user.getId());
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
        String ref = strings.length > 1 ? strings[2] : null;
        String comment = strings.length >= 1 ? strings[1] : null;
        String taskCode = getResultSubCommandFromCache("taskCode");
        comment = taskCode.isEmpty() ? comment : comment + SEPARATOR + "task:" + taskCode;

        List<Partner> partners = getPartnerByUserStatus();
        Partner partner = partners.isEmpty() ? null : partners.get(0);

        CardDoc cardDoc = new CardDoc();
        entityDefaults.fillDefaultData(cardDoc);
        cardDoc.setTotalAmount(Integer.valueOf(getResultSubCommandFromCache("getSum")));
        cardDoc.setPartnerData(partner);
        cardDoc.setReferenceNumber(ref);
        cardDoc.setPaymentType(PaymentType.INCOMING);
        cardDoc.setCardData(CardData.createToDefault());
        cardDoc.setComment(comment);

        SyncDataResponse createResponse = api1C.createCardDoc(cardDocConverter.convertToResponse(cardDoc));
        cardDoc.setSyncData(createResponse);

        cardDocRepository.save(cardDoc);
        eventPublisher.publishEvent(new EntitySavedEvent(cardDoc));

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
        taskDoc.setSyncData(createResponse);

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

    private List<PartnerBalance> getBalances() {
        List<Partner> partners = getPartnerByUserStatus();
        return partnerBalanceRepository.findByPartnerInOrderByPartner_NameAsc(partners);
    }

    private List<UserStatus> getUserStatusByPartner(Partner partner) {
        return user.getStatuses()
                .stream()
                .filter(p -> p.getLegal() == partner)
                .toList();
    }

    private List<Partner> getPartnerByUserStatus() {
        return user.getStatuses()
                .stream()
                .map(status -> (Partner) status.getLegal())
                .filter(Objects::nonNull)
                .toList();
    }

    private String getResultFromSubCommand(String nameSubCommand, String field, String prefix) {
        String message = getResultSubCommandFromCache(nameSubCommand);
        if (message.isEmpty()) {
            return field;
        }
        return field == null || field.isEmpty() ? message : prefix + SEPARATOR + message + SEPARATOR + field;
    }

    private void closingTask(TaskDoc taskDoc, String message, boolean successfully) {
        taskDoc.setStatus(TaskStatus.getClosedStatus());
        taskDoc.setClosingDate(LocalDateTime.now());
        taskDoc.setSuccessfully(successfully);
        String decision = taskDoc.getDecision();
        taskDoc.setDecision(decision == null || decision.isEmpty() ? message : message + SEPARATOR + decision);
        sendToWorkGroup("Задача №" + taskDoc.getCodeEntity() + " отменена пользователем. " + SEPARATOR + "Причина: " + SEPARATOR + message);
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
            case PARTNER -> getNamePartner(taskDoc);
            default -> getNameDepartment(taskDoc);
        };
    }

    private TaskListToSend getTaskListToSend(@NonNull UserStatus status) {
        List<TaskDoc> taskDocs = new ArrayList<>();
        SortingTaskType sortingType = SortingTaskType.DEPARTMENT;
        switch (status.getUserType()) {
            case UNAUTHORIZED, USER -> {
                if (TaskStatus.getClosedStatus() != null) {
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
                taskDocConverter.convertToEntityList(response.getList(), true) :
                taskDocRepository.findByManagerAndStatusNotOrderByDateAsc(manager, TaskStatus.getClosedStatus());
    }

    private List<TaskDoc> getTaskListByApiByCompany(LegalEntity legal) {
        TaskDocDataListResponse response = api1C.getTaskListDataByCompany(Converter.convertToGuid(legal));
        return isCompleted(response) ?
                taskDocConverter.convertToEntityList(response.getList(), true) :
                taskDocRepository.findByPartnerDataNotNullAndPartnerData_PartnerAndStatusNotOrderByDateAsc((Partner) legal,
                        TaskStatus.getClosedStatus());
    }

    private List<TaskDoc> getTaskListByApiByDepartment(Department department) {
        TaskDocDataListResponse response = api1C.getTaskListDataByDepartment(Converter.convertToGuid(department));
        return isCompleted(response) ?
                taskDocConverter.convertToEntityList(response.getList(), true) :
                taskDocRepository.findByPartnerDataNotNullAndPartnerData_DepartmentAndStatusNotOrderByDateAsc(department,
                        TaskStatus.getClosedStatus());
    }

    private List<TaskDoc> getTaskListByApiByUser() {
        if (user.getGuidEntity() == null) {
            return taskDocRepository.
                    findByCreatorAndStatusNotOrderByDateAsc(user, TaskStatus.getClosedStatus());
        }
        TaskDocDataListResponse response = api1C.getTaskListDataByUser(user.getGuidEntity());
        return isCompleted(response) ?
                Converter.convertToEntityListIsSave(response.getList(), taskDocConverter, taskDocRepository) :
                taskDocRepository.
                        findByCreatorAndStatusNotOrderByDateAsc(user, TaskStatus.getClosedStatus());
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

    private String getNamePartner(TaskDoc taskDoc) {
        return taskDoc.getPartnerData() == null ? "-" : getNameEntity(taskDoc.getPartnerData().getPartner());
    }

    private String getNameDepartment(TaskDoc taskDoc) {
        return taskDoc.getPartnerData() == null ? "-" : getNameEntity(taskDoc.getPartnerData().getDepartment());
    }

    private PartnerData fillPartnerData() {

        String idPartner = getResultSubCommandFromCache("getPartner");

        if (idPartner.isEmpty()) {
            UserResponse userData = api1C.getUserData(getResultSubCommandFromCache("getPhone"));
            if (userData != null && userData.getStatusList() != null && userData.getStatusList().size() >= 1) {
                Partner partner = getPartnerByGuid(userData.getStatusList().get(0).getGuid());
                return new PartnerData(partner);
            }
        }

        Partner partner = partnerRepository.findById(Long.valueOf(idPartner)).orElse(null);
        String idDepartment = getResultSubCommandFromCache("getDepartment");
        Department department = getDepartmentById(partner, idDepartment);
        return new PartnerData(partner, department);
    }

    private Department getDepartmentById(Partner partner, String idDep) {
        if (partner == null || partner.getDepartments() == null || partner.getDepartments().isEmpty() || idDep.isEmpty()) {
            return null;
        }
        long id = Long.parseLong(idDep);
        return partner.getDepartments().stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
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

    private @NonNull PartnerData getPartnerDateByAPI(String text) {
        if (!text.equals(REGEX_INN)) {
            return new PartnerData();
        }
        PartnerDataResponse response = api1C.getPartnerData(text);
        Partner partner = isCompleted(response) ?
                createDataByPartnerDataResponse(response).getPartners().get(0) :
                createLegalByInnFromDaData();
        return new PartnerData(partner);
    }

    private Partner getPartnerByGuid(String guid) {

        Optional<Partner> optional = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        if (optional.isPresent()) {
            return optional.get();
        }

        PartnerDataResponse dataResponse = api1C.getPartnerByGuid(guid);

        if (isCompleted(dataResponse)) {
            PartnerListData data = createDataByPartnerDataResponse(dataResponse);
            return data.getPartners().isEmpty() ? null : data.getPartners().get(0);
        }

        return null;
    }

    private PartnerListData createDataByPartnerDataResponse(PartnerDataResponse dataResponse) {
        List<Partner> partners = Converter.convertToEntityListIsSave(dataResponse.getPartners(), partnerConverter, partnerRepository);
        List<Department> departments = Converter.convertToEntityListIsSave(dataResponse.getDepartments(), departmentConverter, departmentRepository);
        List<Contract> contracts = Converter.convertToEntityListIsSave(dataResponse.getContracts(), contractConverter, contractRepository);
        List<PartnerBalance> balances = updateBalance(dataResponse.getBalance());

        return new PartnerListData(partners, departments, contracts, balances);
    }

    private Partner createLegalByInnFromDaData() {
        DaDataParty data = apiDaDataService.getCompanyDataByINN(text);
        Partner partner = new Partner();
        partner.setInn(text);
        partner.setComment("created automatically on " + LocalDateTime.now().format(formatter));
        partner.setPartnerType(PartnerType.BUYER);

        if (data != null) {
            partner.setName(data.getName().getShortWithOpf());
            partner.setKpp(data.getKpp());
            partner.setOGRN(data.getOgrn());
            partner.setCommencement(Converter.convertLongToLocalDateTime(data.getState().getRegistrationDate()));
            partner.setDateCertificate(Converter.convertLongToLocalDateTime(data.getOgrnDate()));
            partner.setOKPO(data.getOkpo());
        }

        Contract contract = new Contract(partner);
        partner.setDefaultContract(contract);

        SyncDataResponse createResponse = api1C.createPartner(partnerConverter.convertToResponse(partner));
        partner.setSyncData(createResponse);

        return partnerRepository.save(partner);
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

    private void sendMessage(@NonNull String message, ReplyKeyboard keyboard) {
        if (message.trim().isEmpty()) {
            return;
        }
        SendMessage sendMessage = createMessage(message, keyboard);
        parent.sendMessage(sendMessage);
    }

    private CommandCache getCommandCache() {
        return commandCacheList.isEmpty() ?
                new CommandCache(nameCommand, user) :
                commandCacheList.get(commandCacheList.size() - 1);
    }

    private CommandCache getCommandCache(String message, String nameSubCommand) {
        if (commandCacheList.isEmpty()) {
            sendMessage(message);
            return addNewSubCommand(nameSubCommand, null);
        }
        return commandCacheList.get(commandCacheList.size() - 1);
    }

    private CommandCache getCommandCache(String nameSubCommand) {
        return commandCacheList.isEmpty() ?
                addNewSubCommand(nameSubCommand, null) :
                commandCacheList.get(commandCacheList.size() - 1);
    }

    private void completeSubCommand(CommandCache command, String nextSubCommand, String result) {
        addNewSubCommand(nextSubCommand, null);
        command.setComplete(true);
        command.setResult(result);
    }

    private CommandCache addNewSubCommand(String subCommand, String result) {
        CommandCache command = new CommandCache(nameCommand, subCommand, user, result);
        commandCacheList.add(command);
        return command;
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
        message.setText(text);
//        parent.sendMessage(message);
    }

    private List<PartnerBalance> updateBalance(List<BalanceResponse> list) {
        return list.stream()
                .map(balanceService::updateLegalBalance)
                .collect(Collectors.toList());
    }

    private static <R extends DataResponse> boolean isCompleted(R response) {
        return response != null && response.isResult();
    }

    public void setParent(TelegramBotServices parent) {
        this.parent = parent;
    }

    private static void runHandler(Runnable handler) {
        if (handler == null) {
            return;
        }
        try {
            handler.run();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
