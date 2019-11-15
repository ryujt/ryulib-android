package ryulib.usbserial;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import ryulib.usbserial.driver.UsbSerialDriver;
import ryulib.usbserial.driver.UsbSerialPort;
import ryulib.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

import ryulib.listeners.OnNotifyListener;
import ryulib.ThreadRepeater;

public class SerialPort {

    public SerialPort(Context context) {
        this.context = context;

        port.set(null);

        readRepeater = new ThreadRepeater();
        readRepeater.setOnRepeat(new OnNotifyListener() {
            @Override
            public void onNotify(Object sender) {
                repeat_read();
            }
        });
        readRepeater.start();

        sendRepeater = new ThreadRepeater();
        sendRepeater.setOnRepeat(new OnNotifyListener() {
            @Override
            public void onNotify(Object sender) {
                repeat_send();
            }
        });
        sendRepeater.start();

        prepare();
    }

    public void prepare() {
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if (availableDrivers.isEmpty()) {
            Log.e("prepare", "availableDrivers.isEmpty()");
            return;
        }

        driver = availableDrivers.get(0);
        connection = usbManager.openDevice(driver.getDevice());
        if (connection == null) {
            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            context.registerReceiver(broadcastReceiver, filter);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
            usbManager.requestPermission(driver.getDevice(), pendingIntent);
        } else {
            connect();
        }
    }

    public void connect() {
        if (driver == null) {
            if (onErrorListener != null) onErrorListener.onError(-1, "driver를 가져오지 못하였습니다.");
            Log.e("connect", "(driver == null).");
            return;
        }

        UsbSerialPort port_temp;

        try {
            port_temp = port.get();
            if (port_temp != null) port_temp.close();
        } catch (IOException e) {
            Log.e("connect", "port_temp.close() - " + e.getMessage());
        }

        try {
            port_temp = driver.getPorts().get(0);
        } catch (Exception e) {
            if (onErrorListener != null) onErrorListener.onError(-2, "USB 포트를 가져오지 못하였습니다.");
            Log.e("connect", "(port_temp == null).");
            return;
        }

        if (port_temp == null) {
            port.set(null);
            if (onErrorListener != null) onErrorListener.onError(-2, "USB 포트를 가져오지 못하였습니다.");
            Log.e("connect", "(port_temp == null).");
            return;
        }

        try {
            port_temp.open(connection);
            port_temp.setDTR(true);
            port_temp.setRTS(true);
            port_temp.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            port_temp.purgeHwBuffers(true, true);

            port.set(port_temp);
        } catch (Exception e) {
            Log.e("connect", "Can not connect to USB port.");
            port.set(null);
            if (onErrorListener != null) onErrorListener.onError(-3, "USB 포트를 연결 중 에러가 발생하였습니다.");
            return;
        }

        Log.i("connect", "Connected.");

        if (onConnectedListener != null) onConnectedListener.onConnected();
    }

    public void clear() {
        synchronized (sendBuffer) {
            sendBuffer.clear();
        }
    }

    public void write(String text) {
        synchronized (sendBuffer) {
            if (port.get() != null) sendBuffer.offer(text);
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (null != usbDevice) {
                            Log.i("BroadcastReceiver", "USB port is ready to use.");
                            if (onPreparedListener != null) onPreparedListener.onPrepared();
                        }
                    } else {
                        Log.e("connect", String.valueOf("Permission denied for device" + usbDevice));
                        if (onErrorListener != null) onErrorListener.onError(-4, "USB 포트 사용권한을 취소하였습니다.");
                    }
                }
            }
        }
    };

    private void repeat_read() {
        UsbSerialPort port_copy = port.get();

        if (port_copy == null) {
            readRepeater.sleep(5);
            return;
        }

        String line = "";

        try {
            byte buffer[] = new byte[4096];
            int bytesRead = port_copy.read(buffer, 100);
            if (bytesRead > 0) {
                line = new String(buffer, 0, bytesRead);
                serialLock.readPacket(line);
            }
        } catch (Exception e) {
            Log.e("repeat_read", e.getMessage());
        }

        if (onReceivedListener != null) onReceivedListener.onReceived(line);
    }

    private void repeat_send() {
        UsbSerialPort port_copy = port.get();

        if (port_copy == null) {
            sendRepeater.sleep(5);
            return;
        }

        if (serialLock.isReleased() == false) {
            sendRepeater.sleep(5);
            return;
        }

        String line = null;

        synchronized (sendBuffer) {
            try {
                if (sendBuffer.isEmpty() == false) line = sendBuffer.poll();
            } catch (Exception e) {
                Log.e("repeat_send", e.getMessage());
                if (onDisconnectedListener != null) onDisconnectedListener.onDisconnected();
                return;
            }
        }

        if (line == null) {
            sendRepeater.sleep(5);
            return;
        }

        try {
            port_copy.write(line.getBytes(), 1000);
        } catch (Exception e) {
            Log.e("port.write", e.getMessage());
            if (onDisconnectedListener != null) onDisconnectedListener.onDisconnected();
        }
    }

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private Context context;

    private UsbManager usbManager;
    private AtomicReference<UsbSerialPort> port = new AtomicReference<>();
    private UsbSerialDriver driver = null;
    private UsbDeviceConnection connection;

    private ThreadRepeater readRepeater;
    private ThreadRepeater sendRepeater;
    private Queue<String> sendBuffer = new LinkedList<>();
    private SerialLock serialLock = new SerialLock();

    public interface OnPreparedListener {
        void onPrepared();
    }

    public interface OnConnectedListener {
        void onConnected();
    }

    public interface OnDisconnectedListener {
        void onDisconnected();
    }

    public interface OnReceivedListener {
        void onReceived(String text);
    }

    public interface OnErrorListener {
        void onError(int errorcode, String msg);
    }

    private OnPreparedListener onPreparedListener = null;
    private OnConnectedListener onConnectedListener = null;
    private OnDisconnectedListener onDisconnectedListener = null;
    private OnReceivedListener onReceivedListener = null;
    private OnErrorListener onErrorListener = null;

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    public OnConnectedListener getOnConnectedListener() {
        return onConnectedListener;
    }

    public void setOnConnectedListener(OnConnectedListener onConnectedListener) {
        this.onConnectedListener = onConnectedListener;
    }

    public OnDisconnectedListener getOnDisconnectedListener() {
        return onDisconnectedListener;
    }

    public void setOnDisconnectedListener(OnDisconnectedListener onDisconnectedListener) {
        this.onDisconnectedListener = onDisconnectedListener;
    }

    public OnReceivedListener getOnReceivedListener() {
        return onReceivedListener;
    }

    public void setOnReceivedListener(OnReceivedListener onReceivedListener) {
        this.onReceivedListener = onReceivedListener;
    }

    public OnErrorListener getOnErrorListener() {
        return onErrorListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }
}
