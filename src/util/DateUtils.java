package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter[] PARSERS = new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };
    private static final DateTimeFormatter OUTPUT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static LocalDate parseOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        for (DateTimeFormatter f : PARSERS) {
            try { return LocalDate.parse(t, f); }
            catch (DateTimeParseException ignored) {}
        }
        return null;
    }

    public static String formatOrBlank(LocalDate d) {
        return d == null ? "" : d.format(OUTPUT);
    }
}
