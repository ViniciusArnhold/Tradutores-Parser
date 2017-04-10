package br.unisinos.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class Logger {

    private static final LoggerImpl LOGGER = new LoggerImpl();

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void info(String message, String... args) {
        LOGGER.info(message, args);
    }

    public static void warn(String message, String... args) {
        LOGGER.warn(message, args);
    }

    public static void error(String message, String... args) {
        LOGGER.error(message, args);
    }

    private static class LoggerImpl {

        private static final String LOG_FORMAT = "[%s] - %s : %s";

        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

        private static String getFormatedTime() {
            return TIME_FORMATTER.format(LocalDateTime.now());
        }

        private void log(String logName, String message) {
            System.out.println(String.format(LOG_FORMAT, logName, getFormatedTime(), message));
        }

        void info(String message) {
            log("INFO", message);
        }

        void warn(String message) {
            log("WARN", message);
        }

        void error(String message) {
            log("ERROR", message);
        }

        public void info(String message, String[] args) {
            info(String.format(message, args));
        }

        public void error(String message, String[] args) {
            error(String.format(message, args));
        }

        public void warn(String message, String[] args) {
            warn(String.format(message, args));
        }
    }
}
