package Config;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {
    public static final Logger LOGGER = Logger.getLogger(LoggerConfig.class.getName());

    public static void init() {
        Handler consoleHandler;
        try {
            consoleHandler = new ConsoleHandler();
            Formatter simpleFormatter = new SimpleFormatter();
            consoleHandler.setFormatter(simpleFormatter);
            LOGGER.addHandler(consoleHandler);
            consoleHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);
        } catch (SecurityException e) {
            LOGGER.severe("Error occur in Logger initialization." + e.getLocalizedMessage());
        }
    }
}
