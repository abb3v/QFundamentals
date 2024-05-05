package me.abb3v.qfundamentals.utils;

import com.moandjiezana.toml.Toml;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class LanguageManager {
    private static final ConcurrentHashMap<Locale, Toml> messages = new ConcurrentHashMap<>();
    private static final String LANG_DIR = "plugins/QFundamentals/languages/";
    private static Locale defaultLocale; // This will hold the default locale throughout the plugin lifecycle

    // Method to initialize the default locale and load its messages
    public static void initializeDefaultLocale(Locale locale) {
        defaultLocale = locale;
        loadLocale(locale); // Load the locale when setting it
    }

    // Load locale-specific messages into memory
    public static void loadLocale(Locale locale) {
        Scheduler.run(() -> {
            File langFile = new File(LANG_DIR + locale.toLanguageTag() + ".toml");
            if (langFile.exists()) {
                try {
                    Toml result = new Toml().read(langFile);
                    messages.put(locale, result);
                } catch (Exception e) {
                    Bukkit.getLogger().severe("Failed to load language file: " + e.getMessage());
                }
            } else {
                Bukkit.getLogger().severe("Language file not found: " + langFile.getPath());
            }
        });
    }

    // Retrieve a localized message using the default locale
    public static String getMessage(String key, Object... args) {
        return getMessage(defaultLocale, key, args);
    }

    // Overloaded method to allow fetching messages with a specific locale
    private static String getMessage(Locale locale, String key, Object... args) {
        Toml toml = messages.get(locale);
        if (toml == null) {
            return "Locale not loaded";
        }
        String message = parseNestedKey(toml, key);
        if (message == null) return "Message key not found";
        return ChatColor.translateAlternateColorCodes('&', String.format(message, args));
    }

    private static String parseNestedKey(Toml toml, String nestedKey) {
        String[] keys = nestedKey.split("\\.");
        Toml current = toml;
        for (int i = 0; i < keys.length - 1; i++) {
            current = current.getTable(keys[i]);
            if (current == null) return null;  // Fallback for missing tables
        }
        return current.getString(keys[keys.length - 1]);
    }
}
