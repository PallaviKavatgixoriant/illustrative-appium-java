package com.illustrative.appium.support;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Writes one log line as: timestamp, level, thread name, message.
 */
public final class TimestampFormatter extends Formatter {

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .withZone(ZoneId.systemDefault());

    @Override
    public String format(LogRecord record) {
        String timestamp = TIMESTAMP.format(Instant.ofEpochMilli(record.getMillis()));
        return timestamp
                + " "
                + record.getLevel().getName()
                + " ["
                + Thread.currentThread().getName()
                + "] "
                + formatMessage(record)
                + System.lineSeparator();
    }
}
