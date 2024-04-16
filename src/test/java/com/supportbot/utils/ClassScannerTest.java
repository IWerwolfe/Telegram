package com.supportbot.utils;

import com.supportbot.model.types.Document;
import com.supportbot.model.types.Reference;
import com.supportbot.repositories.reference.LegalEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class ClassScannerTest {

    @Autowired
    private ClassScanner classScanner;

    String pathRef;


    @Test
    void testInvokeMethod() {

        Object object = classScanner.invokeMethod(LegalEntityRepository.class, "findBySyncDataNotNullAndSyncData_Guid");

        assertNotNull(object);
        assertTrue(object instanceof Optional<?>);
    }

    @Test
    void testScan() throws ClassNotFoundException {

        pathRef = "com.supportbot.model.reference";
        List<Class<?>> classList = classScanner.scan(pathRef, Reference.class);

        assertNotNull(classList);
        assertFalse(classList.isEmpty());
    }

    @Test
    void testScan_IsDifferentPackage() throws ClassNotFoundException {

        pathRef = "com.supportbot.model";
        List<Class<?>> refList = classScanner.scan(pathRef, Reference.class);
        List<Class<?>> docList = classScanner.scan(pathRef, Document.class);

        assertNotNull(refList);
        assertFalse(refList.isEmpty());
        assertNotNull(docList);
        assertFalse(docList.isEmpty());

        assertNotEquals(refList.size(), docList.size());
    }

    @Test
    void testScan_PackageIsEmpty() {

        pathRef = "";

        assertThrows(ClassNotFoundException.class, () -> {
            List<Class<?>> classList = classScanner.scan(pathRef, Reference.class);
        }, "Package path is empty");
    }

    @Test
    void testScan_InvalidPackage() throws ClassNotFoundException {

        pathRef = "nonexistent.package";
        List<Class<?>> classList = classScanner.scan(pathRef, Reference.class);

        assertNotNull(classList);
        assertTrue(classList.isEmpty());
    }

}