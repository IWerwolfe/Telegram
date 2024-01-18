package com.supportbot.DTO.message;

import com.supportbot.model.documents.doc.service.TaskDoc;
import com.supportbot.model.reference.legalentity.Department;
import com.supportbot.model.reference.legalentity.LegalEntity;
import com.supportbot.model.types.doctype.PayDoc;
import com.supportbot.model.user.UserBD;
import com.supportbot.model.user.UserStatus;
import com.supportbot.utils.TextUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Data
public abstract class MessageText {

    private static final String SEPARATOR = System.lineSeparator();
    private static final String DUAL_SEPARATOR = System.lineSeparator() + System.lineSeparator();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault());

    public static String getWelcomeMessage() {
        return """
                Добро пожаловать в  СЦ «КассаКом»!
                                
                Принимаем и выполняем заявки по устранению проблем с:
                  • кассовым и торговым оборудованием,
                  • программным обеспечением,
                  • настройкой 1С.
                                
                Для начала работы нажмите «Зарегистрироваться».""";
    }

    public static String getShotWelcomeMessage() {
        return """
                Добро пожаловать в  СЦ «КассаКом»!
                                
                Принимаем и выполняем заявки по устранению проблем с:
                  • кассовым и торговым оборудованием,
                  • программным обеспечением,
                  • настройкой 1С.""";
    }

    public static String getBeforeSurvey() {
        return """
                У нас не удалось найти информацию, соответствующую вашему номеру телефона в базе данных.
                Пожалуйста, укажите актуальную информацию, чтобы зарегистрироваться и получить доступ к нашим услугам""";
    }

    public static String getUnCorrectINN() {
        return "ИНН некорректен. Ваш ИНН можно узнать с любого чека с кассы. " +
                "Если нет возможности посмотреть отложите регистрацию, для этого нажмите /exit";
    }

    public static String getStartINN() {
        return "Укажите ИНН вашей организации";
    }

    public static String getStartGetPhone() {
        return "Укажите номер телефона в формате 79998887766";
    }

    public static String getUnCorrectGetPhone() {
        return "Телефон указан некорректно, требуется указать в следующем формате: 79998887766";
    }

    public static String getNonFindPhone() {
        return "Ваш телефон не найден";
    }

    public static String getStarFIO() {
        return "Укажите ваше полное ФИО";
    }

    public static String getStartPost() {
        return "Укажите вашу должность";
    }

    public static String getUnCorrectFIO() {
        return "Ваше ФИО указано некорректно";
    }

    public static String getAfterSendingPhone(String name, List<UserStatus> userStatus) {

        StringBuilder message = new StringBuilder();
        message.append(name)
                .append(", вы закреплены за следующими организациями: ")
                .append(SEPARATOR);

        for (UserStatus status : userStatus) {
            message.append(SEPARATOR)
                    .append(TextUtils.getNameRef(status.getLegal()))
                    .append(", ваш статус: ")
                    .append(status.getUserType().getLabel());
        }
        return message.toString();
    }

    public static String getDefaultMessageError(String name) {
        return name + ", мы не смогли обработать вашу команду, приносим свои извинения";
    }

    public static String getStartTopic() {
        return "Введите тему вашего обращения.";
    }

    public static String getStartDescription() {
        return """
                Введите текст вашего обращения или подробное описание проблемы. 
                Укажите все важные детали, чтобы мы могли более точно понять ситуацию и предложить соответствующие решения.
                """;
    }

    public static String getErrorDescription() {
        return "Сообщение не корректно, опишите подробно что у вас случилось. Я не могу обрабатывать видео, фото и голосовые сообщения ";
    }

    public static String getStartName() {
        return "Как к вам можно обращаться?";
    }

    public static String getErrorName() {
        return "Ваше имя указано некорректно, пожалуйста укажите ваше имя на русском языке";
    }

    public static String getStartCreateAssistance(String name) {
        return "Рады помочь, " + name + """
                 :
                                
                1. Опишите в сообщении (как можно подробнее!) вашу проблему, что именно не работает или не включается.
                2. Постарайтесь указать модель кассы или оборудования (как правило, написано на наклейке).
                                
                Пожалуйста, не дублируйте заявки, мы обязательно с вами свяжемся!
                """;
    }

    public static String getExitCommand(String command) {
        return "Выполнение команды " + command + " успешно завершено";
    }

    public static String getExitToErrors() {
        return """
                Произошла ошибка при выполнении команды и мы уже разбираемся как ее устранить, попробуйте пожалуйста немного позже
                """;
    }

    public static String getIncorrectTask() {
        return "Произошла ошибка при создании обращения, попробуйте пожалуйста немного позже";
    }

    public static String getSuccessfullyCreatingTask(TaskDoc taskDoc) {
        return "Ваше обращение успешно зарегистрировано под номером " +
                taskDoc.toString(false) + DUAL_SEPARATOR +
                "Мы обязательно свяжемся с вами в самое ближайшее время!";
    }

    public static String getSearchErrors() {
        return "Активных задач по вашему запросу не найдено";
    }

    public static String getSearchGrouping(String sortName, int count) {
        return count + " задач: " + (sortName == null ? "" : sortName);
    }

    public static String getSearch(String nameCompany, int count) {
        return "На " + nameCompany + " зарегистрировано " + count + " задач: ";
    }

    public static String errorWhenEditTask() {
        return """
                Введенный вами параметр и\\или текст некорректен, попробуйте еще раз. 
                Для выхода нажмите /exit""";
    }

    public static String getEditTextTask(String nameFields) {
        return "Укажите что нужно добавить в " + nameFields;
    }

    public static String getWhenCancelTask() {
        return "Укажите причину отмены задачи";
    }

    public static String getFormOfPayment() {
        return "Выберите форму оплаты из списка";
    }

    public static String getInputSum() {
        return "Введите сумму для внесения на баланс";
    }

    public static String getSuccessfullyCreatingCardDoc(PayDoc doc) {
        return "От вас поступила оплата в размере " + doc.getPresentTotalAmount() + " руб." + SEPARATOR +
                "Номер платежного документа " + doc.getCodeEntity();
    }

    public static String getBalanceUser(int sum) {
        return "Ваш баланс на " + formatter.format(LocalDateTime.now()) +
                " составляет " + (sum / 100.00) + " руб.";
    }

    public static String getBalanceLegal(LegalEntity legal, int sum) {
        return "Баланс " + legal.getName() + " на " + formatter.format(LocalDateTime.now()) +
                " составляет " + (sum / 100.00) + " руб.";
    }

    public static String getPartnersByList() {
        return "Выберите организацию из списка";
    }

    public static String getDepartmentsByList() {
        return "Выберите торговую точку из списка";
    }

    public static String getSuccessfullyEditTask(TaskDoc taskDoc) {
        return "Задача № " + taskDoc.getCodeEntity() + " успешно отредактирована";
    }

    public static String getSuccessfullyRegister(String firstName) {
        return firstName + """
                Для повышения вашего статуса в системе вам необходимо обратиться к нам в офис либо по телефону.
                """;
    }

    public static String getErrorToEditUserInfo() {
        return "Произошла ошибка при редактировании информации, попробуйте повторить позже";
    }

    public static String getToPaySBP(String sbpStatic) {
        return """
                Для оплаты по СБП необходимо:
                 1. перейти по ссылке, указанной ниже;
                 2. выбрать банк, через который будете оплачивать;
                 3. указать сумму оплаты;
                 4. в приложении банка подтвердить оплату;
                 
                Деньги поступят на баланс в течение 3 рабочих дней, после этого обновится информация в телеграм боте
                Ссылка для оплаты через БСП: """ + sbpStatic;
    }

    public static String getErrorPayBlocked() {
        return """
                В данный момент возможность оплаты через телеграм бот заблокирована. Приносим свои извинения
                """;
    }

    public static String getNotifyNewTask(TaskDoc taskDoc, UserBD user) {
        return "От вашей организации поступило новое обращение №"
                + taskDoc.getCodeEntity() +
                " в техподдержку: " + DUAL_SEPARATOR +
                taskDoc.getDescription() + DUAL_SEPARATOR +
                getDepartmentPresentation(taskDoc.getDepartment()) +
                getUserPresentation(user);
    }

    public static String getNotifyClosed(TaskDoc taskDoc, UserBD user) {
        return "Обращение №"
                + taskDoc.getCodeEntity() +
                " было закрыто "
                + (user != null ? user.getNamePresentation() : "")
                + DUAL_SEPARATOR
                + taskDoc.getDescription();
    }

    public static String getSystemNotifyOfUserClosure(TaskDoc taskDoc, UserBD user) {
        return "Задача №"
                + taskDoc.getCodeEntity() +
                " была закрыта пользоватем "
                + (user != null ? user.getNamePresentation() : "")
                + DUAL_SEPARATOR
                + taskDoc.getDescription() + DUAL_SEPARATOR
                + "Причина: "
                + SEPARATOR
                + taskDoc.getDecision().replaceAll("Закрыто пользователем:", "").trim();
    }

    public static String getIncorrectTaskStatus() {
        return "Запрещено редактировать завершенные обращения";
    }

    public static String getSystemNotifyClosed(TaskDoc taskDoc) {
        return "Задача №"
                + taskDoc.getCodeEntity() +
                " была закрыта"
                + DUAL_SEPARATOR +
                taskDoc.getDescription()
                + "Решение: "
                + DUAL_SEPARATOR
                + taskDoc.getDecision();
    }

    public static String getSystemNotifyNewTask(TaskDoc taskDoc, UserBD user) {
        return "От организации "
                + (taskDoc.isBilling() ? "на обслуживании " : "") +
                taskDoc.getPartner() +
                " поступила новая задача №"
                + taskDoc.getCodeEntity() + DUAL_SEPARATOR +
                "Описание: " + SEPARATOR +
                taskDoc.getDescription() + DUAL_SEPARATOR +
                getDepartmentPresentation(taskDoc.getDepartment()) +
                getUserPresentation(user);
    }

    private static String getDepartmentPresentation(Department department) {
        return department != null ? "Торговая точка " + department.getName() + SEPARATOR : "";
    }

    private static String getUserPresentation(UserBD user) {
        return user != null ? "от " + user.getNamePresentation() : "";
    }

    public static String getSkippingMessageFromGroup() {
        return "Обработка сообщения из общей группы пропущено";
    }

    public static String getSkippingMessageFromUnknownUser(String messageText) {
        return "Обработка сообщения от неизвестного пользователя пропущена. Текст: " + messageText;
    }
}
