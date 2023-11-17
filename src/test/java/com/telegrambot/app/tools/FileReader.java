package com.telegrambot.app.tools;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileReader {

    public static String readJsonFile(String filePath) {

        File file;

        try {
            file = new File(filePath);
            if (!file.exists()) {
                throw new IOException("File not found: " + filePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return new String(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }
}
