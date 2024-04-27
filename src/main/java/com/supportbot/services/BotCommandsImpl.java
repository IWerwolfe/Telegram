package com.supportbot.services;

import com.supportbot.DTO.SubCommandInfo;
import com.supportbot.DTO.api.other.SyncDataResponse;
import com.supportbot.DTO.api.other.UserResponse;
import com.supportbot.DTO.api.reference.legal.partner.PartnerDataResponse;
import com.supportbot.DTO.api.reference.legal.partner.PartnerListData;
import com.supportbot.DTO.api.typeObjects.DataResponse;
import com.supportbot.DTO.message.MessageText;
import com.supportbot.DTO.types.Currency;
import com.supportbot.DTO.types.EventSource;
import com.supportbot.DTO.types.OperationType;
import com.supportbot.DTO.types.PaymentType;
import com.supportbot.bot.SupportBot;
import com.supportbot.client.ApiClient;
import com.supportbot.components.Buttons;
import com.supportbot.config.BotConfig;
import com.supportbot.config.PaySetting;
import com.supportbot.model.EntityDefaults;
import com.supportbot.model.EntitySavedEvent;
import com.supportbot.model.command.Command;
import com.supportbot.model.command.CommandCache;
import com.supportbot.model.command.CommandStatus;
import com.supportbot.model.documents.doc.payment.CardDoc;
import com.supportbot.model.documents.doc.service.TaskDoc;
import com.supportbot.model.documents.docdata.CardData;
import com.supportbot.model.documents.docdata.PartnerData;
import com.supportbot.model.reference.TaskStatus;
import com.supportbot.model.reference.legalentity.Department;
import com.supportbot.model.reference.legalentity.Partner;
import com.supportbot.model.user.UserBD;
import com.supportbot.model.user.UserStatus;
import com.supportbot.model.user.UserType;
import com.supportbot.repositories.command.CommandCacheRepository;
import com.supportbot.repositories.command.CommandRepository;
import com.supportbot.repositories.doc.CardDocRepository;
import com.supportbot.repositories.doc.TaskDocRepository;
import com.supportbot.repositories.reference.PartnerRepository;
import com.supportbot.repositories.user.UserRepository;
import com.supportbot.services.converter.CardDocConverter;
import com.supportbot.services.converter.TaskDocConverter;
import com.supportbot.services.converter.UserBDConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotCommandsImpl implements BotCommands {

    private final PartnerService partnerService;
    private final UserBotService userBotService;

    private static final String SEPARATOR = System.lineSeparator();

    private final CardDocRepository cardDocRepository;
    private final TaskDocRepository taskDocRepository;
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final CommandCacheRepository commandCacheRepository;
    private final CommandRepository commandRepository;
    private final ApiClient api1C;
    private final ApplicationEventPublisher eventPublisher;
    private final CardDocConverter cardDocConverter;
    private final TaskDocConverter taskDocConverter;
    private final ToStringServices toStringServices;
    private final EntityDefaults entityDefaults;
    private final Buttons button;
    private final SenderService senderService;
    private final BotConfig bot;
    private final PaySetting paySetting;
    private final String REGEX_INN = "^[0-9]{10}|[0-9]{12}$";
    private final String REGEX_FIO = "^(?:[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?\\s+){1,2}[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?$";
    private final String REGEX_PHONE = "^(7|8|\\+7)9[0-9]{9,10}$";
    private SupportBot parent;
    private String text;
    private String nameCommand;
    private long chatId;
    private UserBD user;

    private static <R extends DataResponse> boolean isCompleted(R response) {
        return response != null && response.isResult();
    }

    public void botAnswerUtils(String receivedMessage, long chatId, UserBD userBD) {

        this.chatId = chatId;
        this.user = userBD;

        if (user.getCommandsCache() != null && !user.getCommandsCache().isEmpty()) {
            this.text = receivedMessage.trim();
            this.nameCommand = getCommandCache().getCommand();
        } else {
            this.nameCommand = receivedMessage.trim();
            user.setCommandsCache(new ArrayList<>());
        }

        if (receivedMessage.equals("/exit") || receivedMessage.equals("Отмена")) {
            comExit(MessageText.getExitCommand(nameCommand));
            return;
        }

        try {
            botAnswerUtils();
        } catch (Exception e) {
            parent.handleAnException("Ошибка при выполнении команды \"%s\":\r\n%s \r\n user ID: %s",
                    nameCommand,
                    e.getMessage(),
                    String.valueOf(userBD.getId()));
            comExit(MessageText.getExitToErrors());
        }
    }

    //Command

    public void botAnswerUtils() throws Exception {

        if (nameCommand == null) {
            return;
        }

        String[] param = nameCommand.split(":");
        nameCommand = param.length > 0 ? param[0] : nameCommand;
        text = param.length > 1 && text == null ? param[1] : text;

        switch (nameCommand) {
//            case "/start" -> comStartBot();
//            case "/help" -> comSendHelpText();
            case "/afterRegistered" -> comAfterRegistered();
            case "/registrationSurvey" -> comRegistrationSurvey();
//            case "/getUserByPhone" -> comGetUserByPhone();
//            case "/getByInn" -> comGetByInn();
//            case "/get_task", "Активные" -> comGetTask();
            case "/need_help", "Новое обращение", "/create_task", "Написать в техподдержку" -> comCreateAssistance();
            case "createAssistance" -> comCreateTask();
//            case "getTask" -> comTaskPresentation();
            case "descriptor" -> editDescriptionTask();
            case "comment" -> comEditCommentTask();
            case "cancel" -> comEditCancelTask();
            case "/add_balance", "Пополнить баланс" -> comAddBalance();
//            case "/get_balance", "Проверить баланс" -> comGetBalance();
            case "pay" -> comPayTask();
//            case "/error" -> throw new TelegramApiException();
//            case "/getAboutMe" -> comGetAboutMe();
            case "/exit", "Отмена" -> subEnd(CommandStatus.INTERRUPTED_BY_USER);
            default -> {
                if (nameCommand.matches(".{10,}")) {
                    text = nameCommand;
                    nameCommand = "createAssistance";
                    CommandCache command = getCommandCache("getDescription");
                    completeSubCommand(command, "question", text);
                    comCreateTask();
                } else {
                    comSendDefault();
                }
            }
        }
        if (!user.getCommandsCache().isEmpty()) {
            commandCacheRepository.saveAll(user.getCommandsCache());
        }
        text = null;
    }

    private void comAfterRegistered() {

        userBotService.subUpdateUserByAPI(user);

        if (user.getUserType() == UserType.UNAUTHORIZED) {
            comRegistrationSurvey();
            return;
        }

        String message = MessageText.getAfterSendingPhone(user.getPerson().getFirstName(), user.getStatuses());
        sendMessage(message, button.keyboardMarkupDefault(user.getUserType()));
    }

    private void comRegistrationSurvey() {
        CommandCache command = getCommandCache(MessageText.getBeforeSurvey(), "getInn");
        SubCommandInfo info = new SubCommandInfo(this::comRegistrationSurvey);
        switch (command.getSubCommand()) {
            case "getInn" -> subGetInn(command, info, "getUserName");
            case "getUserName" -> subGetFio(command, info, "getPost");
            case "getPost" -> subGetPost(command, info, "update");
            case "update" -> subUpdateUserInfo();
            default -> comSendDefault();
        }
    }

    private void comCreateAssistance() {

        String message = MessageText.getStartCreateAssistance(user.getPerson().getFirstName());
        CommandCache command = getCommandCache(message, "getDescription");
        SubCommandInfo info = new SubCommandInfo(this::comCreateAssistance);

        if (user.getUserType() == UserType.UNAUTHORIZED) {
            switch (command.getSubCommand()) {
                case "getDescription" -> subGetDescription(command, info, "getPhone");
                case "getPhone" -> {
                    if (user.getPhone() == null) {
                        subGetPhone(command, info, "createTask");
                    } else {
                        completeSubCommand(command, "createTask", user.getPhone());
                    }
                }
//                case "getUserName" -> subGetName(command, info, "createTask");
                case "createTask" -> subCreateTask();
                default -> comSendDefault();
            }
        } else {
            switch (command.getSubCommand()) {
                case "getDescription" -> subGetDescription(command, info, "getPartner");
                case "getPartner" -> subGetPartner(command, this::comCreateAssistance, "getDepartment");
                case "getDepartment" -> subGetDepartment(command, this::comCreateAssistance, "createTask");
                case "createTask" -> subCreateTask();
                default -> comSendDefault();
            }
        }
    }

    private void comCreateTask() {

        CommandCache command = getCommandCache("question");

        switch (command.getSubCommand()) {
            case "question" -> {
                SubCommandInfo info = new SubCommandInfo(this::comCreateTask);
                info.setNextSumCommand("confirmConsent");
                info.setStartMessage(MessageText.getConfirmConsent());
                info.setErrorMessage(MessageText.getDefaultMessageError(user.getUserName()));
                info.setKeyboard(button.getInlineConfirmConsent("createAssistance"));
                subGetTextInfo(command, info);
            }
            case "confirmConsent" -> {
                if (getResultSubCommandFromCache("question").equals("yes")) {
                    String next = user.getUserType() == UserType.UNAUTHORIZED ? "getPhone" : "getPartner";
                    completeSubCommand(command, next);
                    comCreateAssistance();
                } else {
                    subEnd(MessageText.getExitCommand("createAssistance"));
                }
            }
            default -> comCreateAssistance();
        }
    }

    private void comEditCommentTask() {
        editTask(MessageText.getEditTextTask("комментарий"), "comment", this::comEditCommentTask);
    }

    private void comEditCancelTask() {
        editTask(MessageText.getWhenCancelTask(), "cancel", this::comEditCancelTask);
    }

    private void comAddBalance() {
        if (!paySetting.isUse() || !paySetting.isAddBalance()) {
            comExit(MessageText.getErrorPayBlocked());
            return;
        }
        CommandCache command = getCommandCache("getSum");
        switch (command.getSubCommand()) {
            case "getSum" -> {
                SubCommandInfo info = new SubCommandInfo(MessageText.getInputSum(),
                        MessageText.errorWhenEditTask(),
                        this::comAddBalance,
                        "getFormOfPayment",
                        "[0-9]{3,}",
                        this::convertSumTo);
                subGetTextInfo(command, info);
            }
            default -> addPayment(command, this::comAddBalance, "Внесение денег на баланс", "id: " + user.getId());
        }
    }

    private void comPayTask() {
        if (!paySetting.isUse() || !paySetting.isAddBalance()) {
            comExit(MessageText.getErrorPayBlocked());
            return;
        }
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

    private void comExit(String message) {
        sendMessage(message, button.keyboardMarkupDefault(user.getUserType()));
        subEnd(CommandStatus.ERROR);
    }

    private void comSendDefault() {
        if (nameCommand.matches(REGEX_INN)) {
            PartnerDataResponse response = api1C.getPartnerData(nameCommand);
            if (isCompleted(response)) {
                PartnerListData data = partnerService.createDataByPartnerDataResponse(response);
                sendMessage(toStringServices.toStringNonNullFields(data));
                return;
            }
        }
        sendMessage(MessageText.getDefaultMessageError(user.getPerson().getFirstName()));
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
            default -> {
                if (command.getSubCommand().equals(subCommand)) {
                    SubCommandInfo info = new SubCommandInfo(message,
                            MessageText.errorWhenEditTask(),
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
                info.setStartMessage(MessageText.getFormOfPayment());
                info.setErrorMessage(MessageText.errorWhenEditTask());
                info.setKeyboard(button.getInlineByEnumFormOfPay(command.getCommand()));
                subGetTextInfo(command, info);
            }
            case "getPay" -> {
                subGetPay(command, title, explanation);
            }
            case "createPayDoc" -> subCreatePayDoc();
            default -> comSendDefault();
        }
    }

    //SubCommand

    private void editDescriptionTask() {
        editTask(MessageText.getEditTextTask("описание"), "descriptor", this::editDescriptionTask);
    }

    private void subUpdateUserInfo() {

        if (user.getCommandsCache().isEmpty()) {
            comExit(MessageText.getErrorToEditUserInfo());
            return;
        }
        String inn = getResultSubCommandFromCache("getInn");
        String fio = getResultSubCommandFromCache("getUserName");
        String post = getResultSubCommandFromCache("getPost");

        PartnerData partnerData = partnerService.getPartnerDateByAPI(inn);
        UserStatus status = new UserStatus(user, UserType.USER, partnerData.getPartner(), post);

        user.setStatuses(List.of(status));
        UserBDConverter.updateUserFIO(fio, user.getPerson());
        userRepository.save(user);

        String message = MessageText.getSuccessfullyRegister(user.getPerson().getFirstName());
        subEnd(message);
    }

    private void subGetPay(CommandCache command, String title, String explanation) {

        String formOfPay = getResultSubCommandFromCache("getFormOfPayment");
        switch (formOfPay) {
            case "SBP_STATIC" -> {
                sendMessage(MessageText.getToPaySBP(paySetting.getSbpStatic()));
                completeSubCommand(command, "end");
                comAddBalance();
            }
            case "CARD" -> {
                SendInvoice invoice = subGetSendInvoice(title, explanation);
                senderService.sendBotMessage(parent, invoice, user);
                completeSubCommand(command, "createPayDoc");
            }
            case "INVOICE" -> {
            }
            default -> comSendDefault();
        }
    }

    private void subCreateTask() {

        if (user.getCommandsCache().isEmpty()) {
            comExit(MessageText.getIncorrectTask());
            return;
        }
        TaskDoc doc = new TaskDoc();
        entityDefaults.fillDefaultData(doc);
        doc.setDescription(getDescriptionBySubCommand());
        doc.setPartnerData(fillPartnerData());
        doc.setCreator(user);

        SyncDataResponse createResponse = api1C.createTask(taskDocConverter.convertToResponse(doc));
        doc.setSyncData(createResponse);

        taskDocRepository.save(doc);
        eventPublisher.publishEvent(new EntitySavedEvent(doc, OperationType.CREATE, EventSource.USER, user));

        subEnd(MessageText.getSuccessfullyCreatingTask(doc));
    }

    private void subGetPhone(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(MessageText.getStartGetPhone());
        info.setErrorMessage(MessageText.getUnCorrectGetPhone());
        info.setNextSumCommand(nextCommand);
        info.setRegex(REGEX_PHONE);
        subGetTextInfo(command, info);
    }

    private void subGetInn(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(MessageText.getStartINN());
        info.setErrorMessage(MessageText.getUnCorrectINN());
        info.setNextSumCommand(nextCommand);
        info.setRegex(REGEX_INN);
        subGetTextInfo(command, info);
    }

    private void subGetFio(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(MessageText.getStarFIO());
        info.setErrorMessage(MessageText.getUnCorrectFIO());
        info.setRegex(REGEX_FIO);
        info.setNextSumCommand(nextCommand);
        subGetTextInfo(command, info);
    }

    private void subGetName(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(MessageText.getStartName());
        info.setErrorMessage(MessageText.getErrorName());
        info.setNextSumCommand(nextCommand);
        subGetTextInfo(command, info);
    }

    private void subGetPost(CommandCache command, SubCommandInfo info, String nextCommand) {
        info.setStartMessage(MessageText.getStartPost());
        info.setNextSumCommand(nextCommand);
        subGetTextInfo(command, info);
    }

    private void subEnd(CommandStatus status) {

        if (user.getCommandsCache().isEmpty()) {
            return;
        }

        Command command = new Command();
        command.setUserBD(user);
        command.setCommand(user.getCommandsCache().get(0).getCommand());
        command.setResult(getStringSubCommand());
        command.setStatus(status);
        command.setDateComplete(LocalDateTime.now());
        commandRepository.save(command);

        commandCacheRepository.deleteAll(user.getCommandsCache());
        user.getCommandsCache().clear();
        text = null;
    }

    private void subEnd(String message) {
        completeSubCommand(getCommandCache(), "end");
        subEnd(CommandStatus.COMPLETE);
        sendMessage(message, button.keyboardMarkupDefault(user.getUserType()));
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

        info.setStartMessage(MessageText.getStartDescription());
        info.setErrorMessage(MessageText.getErrorDescription());
        info.setNextSumCommand(nextCommand);
        info.setRegex(".{10,}");
        subGetTextInfo(command, info);
    }

    private void subGetPartner(CommandCache command, Runnable parent, String nextCommand) {

        List<Partner> partners = partnerService.getPartnerByUserStatus(user);

        if (partners.size() > 1) {
            SubCommandInfo info = new SubCommandInfo(parent);
            info.setNextSumCommand(nextCommand);
            info.setStartMessage(MessageText.getPartnersByList());
            info.setErrorMessage(MessageText.errorWhenEditTask());
            info.setKeyboard(button.getInlineByRef("getPartner", partners, false));
            subGetTextInfo(command, info);
            return;
        }

        String value = partners.size() == 1 ? String.valueOf(partners.get(0).getId()) : null;
        completeSubCommand(command, "getDepartment", value);
        runHandler(parent);
    }

    private void subGetDepartment(CommandCache command, Runnable parent, String nextCommand) {

        String result = getResultSubCommandFromCache("getPartner");
        Optional<Partner> optional = partnerRepository.findById(Long.parseLong(result));

        if (optional.isEmpty()) {
            comExit(MessageText.getDefaultMessageError(user.getUserName()));
            return;
        }

        List<Department> departments = optional.get().getDepartments();
        String value;

        if (departments == null || departments.isEmpty()) {
            value = "";
        } else if (departments.size() == 1) {
            value = String.valueOf(departments.get(0).getId());
        } else {
            SubCommandInfo info = new SubCommandInfo(parent);
            info.setNextSumCommand(nextCommand);
            info.setStartMessage(MessageText.getDepartmentsByList());
            info.setErrorMessage(MessageText.errorWhenEditTask());
            info.setKeyboard(button.getInlineByRef("getDepartment", departments, true));
            subGetTextInfo(command, info);
            return;
        }

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

        if (user.getCommandsCache().isEmpty() || text == null) {
            comExit(MessageText.getDefaultMessageError(user.getUserName()));
            return;
        }

        String[] strings = text.split(";");
        String ref = strings.length > 1 ? strings[2] : null;
        String comment = strings.length >= 1 ? strings[1] : null;
        String taskCode = getResultSubCommandFromCache("taskCode");
        comment = taskCode.isEmpty() ? comment : comment + SEPARATOR + "task:" + taskCode;

        List<Partner> partners = partnerService.getPartnerByUserStatus(user);
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
        eventPublisher.publishEvent(new EntitySavedEvent(cardDoc, OperationType.CREATE, EventSource.USER, user));

        subEnd(MessageText.getSuccessfullyCreatingCardDoc(cardDoc));
    }

    private void subEditTask(Runnable parent) {
        if (user.getCommandsCache().isEmpty()) {
            comExit(MessageText.getIncorrectTask());
            return;
        }

        TaskDoc taskDoc = subGetTaskDoc(getResultSubCommandFromCache("code"));
        if (taskDoc == null) {
            comExit(MessageText.getIncorrectTask());
            return;
        }

        if (Objects.equals(taskDoc.getStatus().getId(), TaskStatus.getClosedStatus().getId())) {
            comExit(MessageText.getIncorrectTaskStatus());
            return;
        }

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
        eventPublisher.publishEvent(new EntitySavedEvent(taskDoc, OperationType.EDIT, EventSource.USER, user));

        SyncDataResponse createResponse = api1C.updateTask(taskDocConverter.convertToResponse(taskDoc));
        taskDoc.setSyncData(createResponse);

        subEnd(MessageText.getSuccessfullyEditTask(taskDoc));
    }

    private TaskDoc subGetTaskDoc(String code) {
        Optional<TaskDoc> optional = taskDocRepository.findById(Long.valueOf(code));
        if (optional.isPresent()) {
            return optional.get();
        }
        comExit(MessageText.getIncorrectTask());
        return null;
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
    }

    private void closingTask(TaskDoc taskDoc, String message, boolean successfully, String prefix) {
        message = prefix.isEmpty() ? message : prefix + SEPARATOR + message;
        closingTask(taskDoc, message, successfully);
    }

    private String getTaskCode() {
        return "0".repeat(9 - text.length()) + text;
    }

    private PartnerData fillPartnerData() {

        String idPartner = getResultSubCommandFromCache("getPartner");

        if (idPartner.isEmpty()) {
            UserResponse userData = api1C.getUserData(getResultSubCommandFromCache("getPhone"));
            if (userData != null && userData.getStatusList() != null && userData.getStatusList().size() >= 1) {
                Partner partner = partnerService.getPartnerByGuid(userData.getStatusList().get(0).getGuid());
                return new PartnerData(partner);
            }
            return null;
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
        List<String> result = user.getCommandsCache().stream()
                .filter(command -> command.getSubCommand().equalsIgnoreCase(subCommand) && command.getResult() != null)
                .map(CommandCache::getResult).toList();
        return result.isEmpty() ? "" : result.get(0).replaceAll(".*:", "");
    }

    private void convertSumTo() {
        text = text + "00";
    }

    private String getStringSubCommand() {
        StringBuilder builder = new StringBuilder();
        for (CommandCache cache : user.getCommandsCache()) {
            builder.append(cache.getSubCommand())
                    .append(" -> ")
                    .append(cache.getResult())
                    .append("; count: ")
                    .append(cache.getCountStep())
                    .append(SEPARATOR);
        }
        return builder.toString();
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
        senderService.sendBotMessage(parent, message, keyboard, chatId, user);
    }

    private CommandCache getCommandCache() {
        return user.getCommandsCache().isEmpty() ?
                new CommandCache(nameCommand, user) :
                user.getCommandsCache().get(user.getCommandsCache().size() - 1);
    }

    private CommandCache getCommandCache(String message, String nameSubCommand) {
        if (user.getCommandsCache().isEmpty()) {
            sendMessage(message);
            return addNewSubCommand(nameSubCommand, null);
        }
        return user.getCommandsCache().get(user.getCommandsCache().size() - 1);
    }

    private CommandCache getCommandCache(String nameSubCommand) {
        return user.getCommandsCache().isEmpty() ?
                addNewSubCommand(nameSubCommand, null) :
                user.getCommandsCache().get(user.getCommandsCache().size() - 1);
    }

    private void completeSubCommand(CommandCache command, String nextSubCommand, String result) {
        addNewSubCommand(nextSubCommand, null);
        command.setComplete(true);
        command.setResult(result);
    }

    private CommandCache addNewSubCommand(String subCommand, String result) {
        CommandCache command = new CommandCache(nameCommand, subCommand, user, result);
        user.getCommandsCache().add(command);
        return command;
    }

    private void completeSubCommand(CommandCache command, String subCommand) {
        completeSubCommand(command, subCommand, text);
    }

    public void setParent(SupportBot parent) {
        this.parent = parent;
    }

    private void runHandler(Runnable handler) {
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
