package ryulib;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class LocalValues {

    public static void setString(String name, String key, String value) {
        SharedPreferences sharedPreferences = context_.getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String name, String key) {
        SharedPreferences sharedPreferences = context_.getSharedPreferences(name, MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void setContext(Context context) {
        context_ = context;
    }

    private static Context context_ = null;

}
