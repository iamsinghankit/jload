package com.iamsinghankit.jload.logger;

/**
 * @author Ankit Singh
 */
public class Log {

    private static Logger LOG;

    public static void setupLog(boolean debug) {
        LOG = new SimpleConsoleLogger(debug);
    }

    public static void info(String... message){
        LOG.info(message);
    }

    public static void debug(String... message){
        LOG.debug(message);
    }

    public static void error(String... message){
        LOG.error(message);
    }

    public static void error(Throwable exception,String... message){
        LOG.error(exception,message);
    }
}
