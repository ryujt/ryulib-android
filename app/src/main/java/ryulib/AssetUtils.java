package ryulib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetUtils {
    public static void setContext(Context context) {
        AssetUtils.context = context;
    }

    public static String loadString(String filename) {
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "utf-8");
        } catch (Exception e) {
            Log.e("AssetUtils", e.getMessage());
            return "";
        }
    }

    public static Bitmap loadBitmap(String filename) {
        Bitmap bitmap;
        try {
            InputStream is = context.getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception e) {
            Log.e("AssetUtils", e.getMessage());
            return null;
        }
    }

    public static void saveText(String filename, String text) {
        String path = context.getFilesDir().getAbsolutePath() + "/";

        File folder = new File(path);
        if(folder.exists() == false) {
            folder.mkdirs();
        }

        File file = new File(path + filename);
        try {
            FileOutputStream fs = new FileOutputStream(file);
            fs.write(text.getBytes());
            fs.close();
        } catch (Exception e) {
            Log.e("saveString", e.getMessage());
        }
    }

    public static String readText(String filename) {
        String result = "";
        String path = context.getFilesDir().getAbsolutePath() + "/";

        try {
            FileInputStream fs = new FileInputStream(path + filename);
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fs));

            String temp;
            while( (temp = bufferReader.readLine()) != null ) {
                result = result + temp;
            }
        } catch (Exception e) {
            Log.e("readText", e.getMessage());
        }

        return result;
    }

    private static Context context = null;
}
