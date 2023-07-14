package com.telegrambot.app.DTO.message;

import com.telegrambot.app.model.task.Task;
import com.telegrambot.app.model.user.UserStatus;
import lombok.Data;

import java.util.List;

@Data
public abstract class Message {

    private static final String SEPARATOR = System.lineSeparator();
    private static final String DUAL_SEPARATOR = System.lineSeparator() + System.lineSeparator();

    public static String getWelcomeMessage() {
        return "Добро пожаловать в наш телеграмм бот!" + DUAL_SEPARATOR +
                "Устранение проблем с ПО и кассовым оборудованием - наша специализация. " +
                "Мы готовы предложить вам быстрые и профессиональные решения. " + SEPARATOR +
                "Оставьте заявку, и наша команда опытных специалистов поможет вам без лишних хлопот. " + SEPARATOR +
                "Доверьтесь нам, и ваш бизнес станет еще успешнее!";
    }

    public static String getBeforeSendingPhone() {
        return "Для получения персонализированной поддержки и доступа к информации, пожалуйста, нажмите на кнопку ниже. " + DUAL_SEPARATOR +
                "Мы гарантируем конфиденциальность и безопасность ваших данных. " + DUAL_SEPARATOR +
                "После отправки номера телефона, наша система проведет поиск информации и произведет регистрацию. " +
                "Затем мы присвоим вам соответствующий статус и предоставим вам точную и полезную информацию по вашему предприятию." +
                DUAL_SEPARATOR + "Мы ценим ваше доверие и стремимся обеспечить наивысший уровень сервиса.";
    }

    public static String getBeforeSurvey() {
        return "У нас не удалось найти информацию, соответствующую вашему номеру телефона в базе данных. " + DUAL_SEPARATOR +
                "Пожалуйста, укажите актуальную информацию, чтобы зарегистрироваться и получить доступ к нашим услугам";
    }

    public static String getMessageCorrectINN() {
        return "ИНН введен корректно";
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

    public static String getStartUserName() {
        return "Укажите ваше полное ФИО";
    }

    public static String getStartPost() {
        return "Укажите вашу должность";
    }

    public static String getUnCorrectUserName() {
        return "Ваше ФИО указано некорректно";
    }

    public static String getAfterSendingPhone(String name, List<UserStatus> userStatus) {
        StringBuilder message = new StringBuilder();
        message.append(name).append(", мы нашли и обновили информацию для следующих организаций: ");
        for (UserStatus status : userStatus) {
            message.append(SEPARATOR)
                    .append(" * ")
                    .append(status.getLegal().getName())
                    .append(" - ")
                    .append(status.getUserType());
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
        return "Введите текст вашего обращения или подробное описание проблемы. " +
                "Укажите все важные детали, чтобы мы могли более точно понять ситуацию и предложить соответствующие решения.";
    }

    public static String getErrorDescription() {
        return "Сообщение не корректно";
    }

    public static String getStartName() {
        return "Как к вам можно обращаться?";
    }

    public static String getErrorName() {
        return "Ваше имя указано некорректно, пожалуйста укажите ваше имя на русском языке";
    }

    public static String getStartCreateAssistance(String name) {
        return name + ", спасибо что обратились к нам. Мы зададим вам несколько вопросов чтобы уточнить все детали." + DUAL_SEPARATOR +
                "Постарайтесь точно описать проблему, избегая общих формулировок типа: " + DUAL_SEPARATOR +
                "\t -\"Не включается\"" + SEPARATOR +
                "\t -\"показывает черный экран\"" + SEPARATOR +
                "\t -\"не работает касса\", " + DUAL_SEPARATOR +
                "Нам очень поможет если вы укажите модель оборудования и/или название установленного у вас програмного обеспечения " +
                "и что предшествовало проблеме";
    }

    public static String getExitCommand(String command) {
        return "Выполнение команды " + command + " успешно завершено";
    }

    public static String getIncorrectTask() {
        return "Произошла ошибка при создании обращения, попробуйте пожалуйста немного позже";
    }

    public static String getSuccessfullyCreatingTask(Task task) {
        return "Ваше обращение успешно зарегистрировано под номером " +
                (task.getCode() == null ? task.getId() : task.getCode()) + DUAL_SEPARATOR +
                "Скоро с вами свяжется наш мастер. Спасибо что обратились к нам";
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
        return "Введенный вами параметр и\\или текст некорректен, попробуйте еще раз. " +
                "Для выхода нажмите /exit";
    }

    public static String getEditTextTask(String nameFields) {
        return "Укажите что нужно добавить в " + nameFields;
    }

    public static String getWhenCancelTask() {
        return "Укажите причину отмены задачи";
    }
}
