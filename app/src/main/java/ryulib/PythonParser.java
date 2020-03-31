package ryulib;

public class PythonParser {
    enum State {
        stNone, stStringSmall, stStringBig
    }

    static public String deleteComment(String text) {
        String result = "";

        State state = State.stNone;
        int i = 0;
        while (i < text.length()) {
            if (text.charAt(i) == '\\') {
                result = result + "\\";  i++;
                if (i < text.length()) result = result + text.charAt(i);  i++;
                continue;
            }

            switch (state) {
                case stNone: {
                    switch (text.charAt(i)) {
                        case '\'': state = State.stStringSmall; break;
                        case '"': state = State.stStringBig; break;
                        case '#': return result;
                    }
                } break;

                case stStringSmall:
                    if (text.charAt(i) == '\'') state = State.stNone;
                    break;

                case stStringBig:
                    if (text.charAt(i) == '"') state = State.stNone;
                    break;
            }

            result = result + text.charAt(i);
            i++;
        }

        return result;
    }
}
