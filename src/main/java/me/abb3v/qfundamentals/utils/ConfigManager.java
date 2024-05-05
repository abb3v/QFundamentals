package me.abb3v.qfundamentals.utils;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigManager {
    private static final String CONFIG_PATH = "plugins/QFundamentals/config.toml";
    private static final String DEFAULT_CONFIG_PATH = "/default_config.toml";
    private static Locale defaultLocale = Locale.ENGLISH;  // Default locale, can be set via config
    private Toml config;
    private File configFile;
    private Logger logger;

    public ConfigManager(Logger logger) {
        this.logger = logger;
        configFile = new File(CONFIG_PATH);
        loadConfig();
    }

    public boolean loadConfig() {
        try {
            if (!configFile.exists()) {
                createWithDefaultConfig();
            } else {
                config = new Toml().read(configFile);
            }
            return true; // Successfully loaded
        } catch (Exception e) {
            logger.severe("Failed to load config: " + e.getMessage());
            return false; // Failed to load
        }
    }

    private void createWithDefaultConfig() {
        // Load and create the default config.toml file
        InputStream configStream = getClass().getResourceAsStream(DEFAULT_CONFIG_PATH);
        if (configStream == null) {
            logger.severe("Default configuration file not found in resources.");
            return;
        }
        try (Reader configReader = new InputStreamReader(configStream)) {
            Toml defaultConfig = new Toml().read(configReader);
            saveConfig(defaultConfig.toMap());
        } catch (Exception e) {
            logger.severe("Failed to create or save default configuration: " + e.getMessage());
        }

        // Check and create the default language file (e.g., en.toml)
        File enLangFile = new File("plugins/QFundamentals/languages/en.toml");
        if (!enLangFile.exists()) {
            InputStream langStream = getClass().getResourceAsStream("/languages/en.toml");
            if (langStream == null) {
                logger.severe("Default English language file not found in resources.");
                return;
            }
            try (Reader langReader = new InputStreamReader(langStream)) {
                Toml langConfig = new Toml().read(langReader);
                if (!enLangFile.getParentFile().exists()) {
                    enLangFile.getParentFile().mkdirs();
                }
                try (FileWriter langWriter = new FileWriter(enLangFile)) {
                    new TomlWriter().write(langConfig.toMap(), langWriter);
                }
            } catch (Exception e) {
                logger.severe("Failed to create or save default English language file: " + e.getMessage());
            }
        }
    }

    public void saveConfig(Map<String, Object> data) {
        try {
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            }
            try (FileWriter writer = new FileWriter(configFile)) {
                TomlWriter tomlWriter = new TomlWriter();
                tomlWriter.write(data, writer);
            }
        } catch (Exception e) {
            logger.severe("Failed to save configuration: " + e.getMessage());
        }
    }

    public Locale getLocale() {
        try {
            String languageTag = config.getString("locale");
            if (languageTag != null) {
                return Locale.forLanguageTag(languageTag);
            }
        } catch (Exception e) {
            logger.warning("Locale not found in config, defaulting to English.");
        }
        return Locale.ENGLISH; // Default to English if not specified
    }
}
