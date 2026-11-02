package com.illustrative.appium.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads a flat key: value file. Comments start with #.
 * This keeps environment data and test data out of the Java steps.
 */
public final class FlatConfig {

    private final Map<String, String> values;

    private FlatConfig(Map<String, String> values) {
        this.values = values;
    }

    public static FlatConfig load(String relativePath) {
        Path path = Path.of(relativePath);
        if (!Files.exists(path)) {
            throw new IllustrationNotConfiguredException(
                    "PLACEHOLDER_CONFIG_FILE",
                    "Missing file " + path.toAbsolutePath() + ". Run Maven from the project folder.");
        }
        Map<String, String> parsed = new LinkedHashMap<>();
        try {
            for (String line : Files.readAllLines(path)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int colon = trimmed.indexOf(':');
                if (colon < 1) {
                    throw new IllustrationNotConfiguredException(
                            "PLACEHOLDER_CONFIG_FILE",
                            "Expected key: value in " + relativePath + " but found: " + trimmed);
                }
                String key = trimmed.substring(0, colon).trim();
                String value = trimmed.substring(colon + 1).trim();
                parsed.put(key, value);
            }
        } catch (IOException ex) {
            throw new IllustrationNotConfiguredException(
                    "PLACEHOLDER_CONFIG_FILE",
                    "Could not read " + relativePath + ". " + ex.getMessage());
        }
        return new FlatConfig(parsed);
    }

    public String required(String key) {
        String value = values.get(key);
        if (value == null || value.isBlank()) {
            throw new IllustrationNotConfiguredException(
                    "PLACEHOLDER_CONFIG_" + key,
                    "Add " + key + " to the configuration file.");
        }
        return value;
    }

    public List<String> list(String key) {
        return Arrays.stream(required(key).split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .toList();
    }

    public boolean isPlaceholder(String key) {
        return required(key).contains("PLACEHOLDER_");
    }
}
