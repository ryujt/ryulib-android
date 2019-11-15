package ryulib;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class Screen {

    private static Screen obj = null;

    public static void setContext(Context context) {
        obj = new Screen();
        obj.context = context;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) obj.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width  = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
    }

    public static void refresh() {
        if ((obj != null) && (obj.context != null)) setContext(obj.context);
    }

    public static float getdensity() {
        return Screen.obj.context.getResources().getDisplayMetrics().density;
    }

    public static int getDP(int pixel) {
        if (obj == null) return -1;
        float scale = Screen.obj.context.getResources().getDisplayMetrics().density;
        return (int) ((pixel / 1.5f) * scale);
    }

    public static int getPixel(int dp) {
        if (obj == null) return -1;

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, obj.context.getResources().getDisplayMetrics()
        );
    }

    private Context context = null;
    public static int width = -1;
    public static int height = -1;
}
