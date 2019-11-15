package ryulib.graphic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.util.HashMap;

public class Bitmaps {
    private static Bitmaps obj;

    public static void initialize(Context context) {
        obj = new Bitmaps();
        obj.context = context;
    }

    public static Bitmaps getObj() {
        return obj;
    }

    public Bitmap getBitmap(final String filename) {
        Bitmap bitmap = items.get(filename);
        if ((bitmap == null) || bitmap.isRecycled()) {
            bitmap  = loadFromAssets(filename);
        }

        return bitmap;
    }

    private Bitmap loadFromAssets(final String filename) {
        Bitmap bitmap;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        } catch (Exception e) {
            Log.e("getBitmap", e.getMessage());
            return Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
        }

        return bitmap;
    }

    private Context context;
    private HashMap<String, Bitmap> items = new HashMap<>();
}
