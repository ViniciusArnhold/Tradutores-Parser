package br.unisinos.util;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    public static void lineBreak() {
        LOGGER.lineBreak();
    }

    public static void info(String message, Object... args) {
        LOGGER.info(message, args);
    }

    public static void warn(String message, Object... args) {
        LOGGER.warn(message, args);
    }

    public static void error(String message, Object... args) {
        LOGGER.error(message, args);
    }

    private static class LoggerImpl {

        private static final String LOG_FORMAT = "[%s] - %s : ";

        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

        private static String getFormatedTime() {
            return TIME_FORMATTER.format(LocalDateTime.now());
        }

        private void doLog(PrintStream out, String message, String logName) {
            out.println(Arrays.stream(message.split(System.lineSeparator())).collect(
                    Collectors.joining(System.lineSeparator() + String.format(LOG_FORMAT, logName, getFormatedTime()),
                            String.format(LOG_FORMAT, logName, getFormatedTime()),
                            "")));
            out.flush();
        }

        private void log(String logName, String message) {
            doLog(System.out, message, logName);
        }

        private void logErr(String logName, String message) {
            doLog(System.err, message, logName);
        }

        void info(String message) {
            log("INFO", message);
        }

        void warn(String message) {
            log("WARN", message);
        }

        void error(String message) {
            logErr("ERROR", message);
        }

        public void info(String message, Object... args) {
            info(String.format(message, args));
        }

        public void error(String message, Object... args) {
            error(String.format(message, args));
        }

        public void warn(String message, Object... args) {
            warn(String.format(message, args));
        }

        public void lineBreak() {
            System.out.println();
        }
    }
}
