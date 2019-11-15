package ryulib;

import android.os.Handler;
import android.os.Message;

import ryulib.listeners.OnMessageListener;

public class MessageControl extends Handler {

    public void send(int what, String text) {
        Message msg = this.obtainMessage();
        msg.what = what;
        msg.obj = text;
        this.sendMessage(msg);
    }

    public void send(String text) {
        send(0, text);
    }

    public void send(int what, Object object) {
        Message msg = this.obtainMessage();
        msg.what = what;
        msg.obj = object;
        this.sendMessage(msg);
    }

    public void send(Object object) {
        send(0, object);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (onMessageListener != null) onMessageListener.onMessage(msg);
    }

    public void setOnMessageListener(OnMessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
    }

    private OnMessageListener onMessageListener = null;
}
