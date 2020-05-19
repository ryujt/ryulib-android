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
            toast = Toast.makeText(activity, msg_, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 1000) {
            activity.finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }

    public void setMsg(String msg) { msg_ = msg; }

    private String msg_ = "Click the back button again to close this app.";
    private long backKeyPressedTime = 0;
    private Activity activity;
    private Toast toast;
}
