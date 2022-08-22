package Config;

import java.io.IOException;
import java.util.logging.*;

public class LoggerConfig
{
    public static final Logger LOGGER = Logger.getLogger(LoggerConfig.class.getName());
    public static void init()
    {
        Handler fileHandler;
        try {
            fileHandler = new FileHandler(System.getProperty("user.dir") + "/application.log");
            Formatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);
        } catch (IOException  | SecurityException e) {
            LOGGER.severe("Error occur in Logger initialization." + e.getLocalizedMessage());
        }
    }
}
