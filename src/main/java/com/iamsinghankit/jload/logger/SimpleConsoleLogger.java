package com.iamsinghankit.jload.logger;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * @author Ankit Singh
 */
class SimpleConsoleLogger implements Logger {


    private final boolean debug;
    private final DateTimeFormatter formatter;

    SimpleConsoleLogger(boolean debug) {
        this.debug = debug;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void info(String... message) {
        print(out, "INFO", message);
    }

    @Override
    public void debug(String... message) {
        if (!debug) return;
        print(out, "DEBUG", message);
    }

    @Override
    public void error(String... message) {
        print(err, "ERROR", message);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void error(Throwable exception, String... message) {
        error(message);
        if (debug) exception.printStackTrace();
    }

    @SuppressWarnings("all")
    private void print(PrintStream stream, String level, String... message) {
        String text = message[0];
        message[0]=level;
        var finalText = LocalDateTime.now().format(formatter) + " [%s] " + text;
        stream.println( finalText.formatted(message));
    }

}
