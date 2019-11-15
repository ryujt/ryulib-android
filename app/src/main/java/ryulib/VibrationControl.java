package ryulib;

import android.content.Context;
import android.os.Vibrator;

public class VibrationControl {

    public static void setContext(Context context) {
        VibrationControl.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static void vibrate(int millis) {
        vibrator.vibrate(millis);
    }

    private static Context context;
    private static Vibrator vibrator;
}
