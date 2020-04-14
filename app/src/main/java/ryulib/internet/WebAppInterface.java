package ryulib.internet;

import android.content.Context;
import android.webkit.JavascriptInterface;

import ryulib.listeners.OnNotifyListener;
import ryulib.listeners.OnStringListener;

public class WebAppInterface {
    Context context;

    WebAppInterface(Context acontext) {
        context = acontext;
    }

    @JavascriptInterface
    public void showKeyboard(String html) { if (onShowKeyboard != null) onShowKeyboard.onString(this, html); }

    @JavascriptInterface
    public void go_url(String msg) {
        if (onURL != null) onURL.onString(this, msg);
    }

    @JavascriptInterface
    public void run_cmd(String msg) {
        if (onCommand != null) onCommand.onString(this, msg);
    }

    public void setOnShowKeyboard(OnStringListener onShowKeyboard) { this.onShowKeyboard = onShowKeyboard; }
    public void setOnURL(OnStringListener onURL) {
        this.onURL = onURL;
    }
    public void setOnCommand(OnStringListener onCommand) {
        this.onCommand = onCommand;
    }

    private OnStringListener onShowKeyboard = null;
    private OnStringListener onURL = null;
    private OnStringListener onCommand = null;
}
