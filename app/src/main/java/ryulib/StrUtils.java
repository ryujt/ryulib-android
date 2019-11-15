package ryulib;

public class StrUtils {

    static public String getTextInLines(String text, String border, int index) {
        if (index < 0) return "";

        String lines[] = text.split(border);
        if (index >= lines.length) return "";

        return lines[index];
    }

    static public int strToIntDef(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
