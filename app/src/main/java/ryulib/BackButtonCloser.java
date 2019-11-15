package ryulib;

import android.app.Activity;
import android.widget.Toast;

public class BackButtonCloser {

    public BackButtonCloser(Activity context) {
        this.activity = context;
    }

    public void pressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 1000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(activity, "Click the back button again to close this app.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 1000) {
            activity.finish();
            toast.cancel();
        }
    }

    private long backKeyPressedTime = 0;
    private Activity activity;
    private Toast toast;

}
