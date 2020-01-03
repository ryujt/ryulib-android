package ryulib;

public class StrUtils {

    /**
     * text를 border로 잘라서 idex번째 문자열을 리턴한다.
     * @param text 원본 문자열
     * @param border 복사를 할 경계선(문자열)
     * @param index 잘라진 문자열 중 가져와야 할 순서
     * @return text를 border로 자른 idex번째 문자열
     */
    static public String getTextInLines(String text, String border, int index)
    {
        if (index < 0) return "";

        String lines[] = text.split(border);
        if (index >= lines.length) return "";

        return lines[index];
    }

    static public int strToIntDef(String value, int defaultValue)
    {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * text에서 border를 찾아서 그 전까지 복사한다.
     * @param text 원본 문자열
     * @param border 복사를 할 경계선(문자열)
     * @param ignore_case border를 찾을 때 대소 문자를 구별할 것인가
     * @return border가 없으면 null 문자열을 리턴. 있으면 그 이전까지 복사
    */
    static public String copyLeft(String text, String border, boolean ignore_case)
    {
        int index;
        if (ignore_case) {
            index = text.toLowerCase().indexOf(border.toLowerCase());
        } else {
            index = text.indexOf(border);
        }

        if (index < 0) return "";

        return text.substring(0, index);
    }

    /**
     * text에서 border를 찾아서 그 전까지 복사한다.
     * @param text 원본 문자열
     * @param border 복사를 할 경계선(문자열)
     * @return border가 없으면 null 문자열을 리턴. 있으면 그 이전까지 복사
     */
    static public String copyLeft(String text, String border)
    {
        return copyLeft(text, border, true);
    }

    /**
     * text를 border까지 잘라서 리턴한다.
     * @param text 원본 문자열
     * @param border 복사를 할 경계선(문자열)
     * @param ignore_case border를 찾을 때 대소 문자를 구별할 것인가
     * @return 자르고 남은 문자열
     */
    static public String deleteLeftPlus(String text, String border, boolean ignore_case)
    {
        int index;
        if (ignore_case) {
            index = text.toLowerCase().indexOf(border.toLowerCase());
        } else {
            index = text.indexOf(border);
        }

        if (index < 0) return text;

        return text.substring(index+1, text.length());
    }

    /**
     * text를 border까지 잘라서 리턴한다.
     * @param text 원본 문자열
     * @param border 복사를 할 경계선(문자열)
     * @return 자르고 남은 문자열
     */
    static public String deleteLeftPlus(String text, String border)
    {
        return deleteLeftPlus(text, border, true);
    }
}
