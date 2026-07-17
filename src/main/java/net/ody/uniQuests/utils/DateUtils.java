package net.ody.uniQuests.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yy");
    public static String addDays(String date, int days) {
        LocalDate parsed;
        try {
            parsed = LocalDate.parse(date, FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date \"" + date + "\", expected format dd/mm/yy", e);
        }
        return parsed.plusDays(days).format(FORMAT);
    }
}