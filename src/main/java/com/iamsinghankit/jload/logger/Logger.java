package com.iamsinghankit.jload.logger;

import java.time.LocalDateTime;

/**
 * @author Ankit Singh
 */
public interface Logger {

    void info(String... message);

    void debug(String... message);

    void error(String... message);

    void error(Throwable exception,String... message);


}
