package com.supportbot.config;

import com.supportbot.model.types.Reference;
import com.supportbot.repositories.EntityRepository;
import com.supportbot.utils.ClassScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class TelegramBotConfigurationTest {

    @Mock
    private DefaultBotSession botSession;
    @InjectMocks
    private ClassScanner classScanner;
    @InjectMocks
    private TelegramBotConfiguration botConfiguration;

    @BeforeEach
    public void setup() {
        Mockito.lenient().when(botSession.isRunning()).thenReturn(true);
    }

    @Test
    public void testReferenceListScanning() {
        List<Class<?>> referenceList = null;
        try {
            referenceList = classScanner.scan("com.supportbot.model.reference", Reference.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertFalse(referenceList == null || referenceList.isEmpty());
    }

    @Test
    public void testCheckRefWithEmptyList() {
        List<Class<?>> emptyList = new ArrayList<>();
        botConfiguration.setReferenceList(emptyList);
        botConfiguration.checkRef();
    }

    @Test
    public void testCheckRefWithNonEmptyList() {
        try {
            List<Class<?>> referenceList = classScanner.scan("com.supportbot.model.reference", Reference.class);
            botConfiguration.setReferenceList(referenceList);
            botConfiguration.checkRef();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckRef() {

        List<Class<?>> mockList = new ArrayList<>();
        mockList.add(String.class);

        ReflectionTestUtils.setField(botConfiguration, "referenceList", mockList);

        Method method = null;
        try {
            method = botConfiguration.getClass().getDeclaredMethod("checkRef");
            method.setAccessible(true);
            method.invoke(botConfiguration);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCheckRefRepository() throws ClassNotFoundException {

        List<Class<?>> mockList = classScanner.scan("com.supportbot.repositories.reference", EntityRepository.class);
        ReflectionTestUtils.setField(botConfiguration, "referenceList", mockList);

        Method method = null;
        try {
            method = botConfiguration.getClass().getDeclaredMethod("checkRef");
            method.setAccessible(true);
            method.invoke(botConfiguration);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}