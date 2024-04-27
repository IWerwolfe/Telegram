package com.supportbot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class ToStringServices {

    private static final String SEPARATOR = System.lineSeparator();

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
            boolean isContains = field.getType().getName().startsWith("com.supportbot");
            boolean isDesiredType = !field.getType().isPrimitive() && !field.getType().isEnum();
            if (isDesiredType && isContains && isNested) {
                toStringObject(tab, stringBuilder, value, isNested);
                continue;
            }
            stringBuilder.append(value).append(SEPARATOR);
        }
        return stringBuilder.toString();
    }

    List<Field> getFieldObject(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> allFields = new ArrayList<Field>();
        while (clazz != null && clazz.getTypeName().startsWith("com.supportbot")) {
            Field[] declaredFields = clazz.getDeclaredFields();
            allFields.addAll(Arrays.asList(declaredFields));
            clazz = clazz.getSuperclass();
        }
        return allFields;
    }

    void toStringObject(String tab, StringBuilder stringBuilder, Object value, boolean isNested) {
        stringBuilder.append(SEPARATOR)
                .append(toStringNonNullFields(value, tab + "\t", isNested))
                .append(SEPARATOR);
    }

    void toStringArray(String tab, StringBuilder stringBuilder, Object value, boolean isNested) {
        stringBuilder.append(SEPARATOR);
        for (int i = 0; i < Array.getLength(value); i++) {
            Object objectValue = Array.get(value, i);
            if (objectValue != null) {
                addStringInCycle(tab, stringBuilder, i, objectValue, isNested);
            }
        }
    }

    public String toStringIterableNonNull(Iterable<?> value, boolean isNested) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        toStringIterable("", stringBuilder, value, isNested);
        return stringBuilder.append("]").toString();
    }

    void toStringIterable(String tab, StringBuilder stringBuilder, Iterable<?> value, boolean isNested) {
        int index = 0;
        stringBuilder.append(SEPARATOR);
        for (Object element : value) {
            if (element != null) {
                addStringInCycle(tab, stringBuilder, index, element, isNested);
                index++;
            }
        }
    }

    void addStringInCycle(String tab, StringBuilder stringBuilder, int index, Object element, boolean isNested) {
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