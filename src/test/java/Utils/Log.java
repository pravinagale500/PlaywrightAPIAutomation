package Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Log {
    //Initialize Log4j instance
    private static final Logger Log = LogManager.getLogger(Log.class);

    //Info Level Logs
    public static void info(String message) {
        Log.info(message);
    }

    public static void info(int message) {
        Log.info(message);
    }

    public static void info(List<String> message) {
        message.forEach(e -> Log.info(e));
    }

    //Warn Level Logs
    public static void warn(String message) {
        Log.warn(message);
    }

    //Error Level Logs
    public static void error(String message) {
        Log.error(message);
    }

    //Fatal Level Logs
    public static void fatal(String message) {
        Log.fatal(message);
    }

    //Debug Level Logs
    public static void debug(String message) {
        Log.debug(message);
    }
}