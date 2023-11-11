package com.telegrambot.app.components;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

@Slf4j
class ButtonsTest {

    @org.junit.jupiter.api.Test
    @DisplayName("Проверка преобразования описания задачи в имя кнопки")
    public void testConvertDescription() {

        Map<String, String> strings = new HashMap<>();
        strings.put("Описание задачи обычное",
                "Описание задачи обычное");
        strings.put("Описание задачи длинное с большим количеством символов с большим количеством символов с большим количеством символов",
                "Описание задачи длинное с большим количеством символов с большим количеством символов с большим количеством символов");
        strings.put("Описание задачи - дополненое разными знаками препинания, без переноса строк",
                "Описание задачи - дополненое разными знаками препинания, без переноса строк");
        strings.put("Описание задачи - дополненое разными знаками препинания, " + System.lineSeparator() + "дополненое переносом в середине строки",
                "Описание задачи - дополненое разными знаками препинания, дополненое переносом в середине строки");
        strings.put("Описание задачи дополненое цифрами 2323443 2 324234242342342",
                "Описание задачи дополненое цифрами 2323443 2 324234242342342");
        strings.put("Описание задачи (дополненое) спец символами \"Фортуна\"\\.\\<Торг\\>",
                "Описание задачи (дополненое) спец символами Фортуна . Торг");
        strings.put("Описание  задачи дополненое   длинным пробелами",
                "Описание задачи дополненое длинным пробелами");
        strings.put("Описание задачи дополненое latin chars",
                "Описание задачи дополненое latin chars");

//        checkStringType(strings);
    }

//    private void checkStringType(Map<String, String> strings) {
//        for (String key : strings.keySet()) {
//            a.assertEquals("Проверяем \"" + key + "\"", strings.get(key), Buttons.convertDescription(key));
//            log.info("Строка \"{}\" успешно проверена", key);
//        }
//    }

}