package com.supportbot.utils;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Class.forName;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassScanner {

    private final ApplicationContext applicationContext;

    public <T> List<Object> invokeMethodForChildrenClazz(Class<T> clazz, String methodName) {

        String[] beanNames = applicationContext.getBeanNamesForType(clazz);

        return Arrays.stream(beanNames)
                .map(beanName -> invokeMethod(clazz, methodName, beanName))
                .filter(Objects::nonNull)
                .toList();
    }

    public <T> Object invokeMethod(Class<T> clazz, String methodName, String beanName) {
        T bean = applicationContext.getBean(beanName, clazz);
        return invokeMethod(bean, methodName);
    }

    public <T> Object invokeMethod(Class<T> clazz, String methodName) {
        T bean = applicationContext.getBean(clazz.getName(), clazz);
        return invokeMethod(bean, methodName);
    }

    public <T> Object invokeMethod(T bean, String methodName) {

        Method method = getClassMethod(bean, methodName);

        if (method == null) {
            log.error("Method {} not found for bean {}", methodName, bean.getClass().getSimpleName());
            return null;
        }

        try {
            return method.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Error while invoking method {} for bean {}", methodName, bean.getClass().getSimpleName());
            return null;
        }
    }

    public <T> List<Class<?>> scan(@NotNull String basePackage, @NotNull Class<T> tClass) throws ClassNotFoundException {

        if (basePackage.isEmpty()) {
            throw new ClassNotFoundException("Package path is empty");
        }

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(tClass));

        List<Class<?>> classList = new ArrayList<>();

        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            Class<?> clazz = forName(bd.getBeanClassName());
            classList.add(clazz);
        }

        return classList;
    }

    private <T> boolean hasFindMethod(T bean, String methodName) {
        return getClassMethod(bean, methodName) != null;
    }

    private <T> Method getClassMethod(T bean, String methodName) {
        try {
            return bean.getClass().getMethod(methodName, String.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
