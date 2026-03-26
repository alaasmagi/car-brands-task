package com.alaasmagi.car_brands_api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class ApplicationTest {

    private static final String DOTENV_KEY = "APP_TEST_DOTENV_VALUE";

    @AfterEach
    void clearSystemProperty() {
        System.clearProperty(DOTENV_KEY);
    }

    @Test
    void mainLoadsDotenvAndStartsSpringApplication() throws Exception {
        withDotenvFile(DOTENV_KEY + "=loaded-from-dotenv\n", () -> {
            try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
                Application.main(new String[]{"--test"});

                assertEquals("loaded-from-dotenv", System.getProperty(DOTENV_KEY));
                springApplication.verify(() -> SpringApplication.run(Application.class, new String[]{"--test"}), times(1));
            }
        });
    }

    @Test
    void loadDotenvDoesNotOverrideExistingSystemProperty() throws Exception {
        System.setProperty(DOTENV_KEY, "existing-value");

        withDotenvFile(DOTENV_KEY + "=from-dotenv\n", () -> {
            Method loadDotenv = Application.class.getDeclaredMethod("loadDotenv");
            loadDotenv.setAccessible(true);
            loadDotenv.invoke(null);

            assertEquals("existing-value", System.getProperty(DOTENV_KEY));
        });
    }

    private void withDotenvFile(String content, ThrowingRunnable action) throws Exception {
        Path dotenvPath = Path.of(".env");
        Optional<String> originalContent = Files.exists(dotenvPath)
                ? Optional.of(Files.readString(dotenvPath))
                : Optional.empty();

        Files.writeString(dotenvPath, content);

        try {
            action.run();
        } finally {
            if (originalContent.isPresent()) {
                Files.writeString(dotenvPath, originalContent.get());
            } else {
                Files.deleteIfExists(dotenvPath);
            }
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
