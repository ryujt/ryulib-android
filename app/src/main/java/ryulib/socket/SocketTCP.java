package ryulib.socket;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class SocketTCP {

    public static final int PACKETSIZE_LIMIT = 4096;

    public void connect(String host, int port) {
        disconnect();

        socket = new Socket();
        try {
            socket.setSoTimeout(5);
        } catch (SocketException e) {
            Log.e("SocketTCP", e.getMessage());
            return;
        }

        InetSocketAddress address = new InetSocketAddress(host, port);
        try {
            socket.connect(address);
        } catch (IOException e) {
            Log.e("SocketTCP", e.getMessage());
        }
    }

    public void disconnect() {
        if (socket == null) return;

        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            Log.e("SocketTCP", e.getMessage());
        }
    }

    public boolean sendText(String text) {
        if (socket == null) return false;

        byte[] data = text.getBytes();
        try {
            socket.getOutputStream().write(data, 0, data.length);
            return true;
        } catch (IOException e) {
            Log.e("SocketTCP", e.getMessage());
            disconnect();
            return false;
        }
    }

    public String readText() {
        if (socket == null) return "";

        byte[] data = new byte[PACKETSIZE_LIMIT];
        try {
            int size = socket.getInputStream().read(data, 0, PACKETSIZE_LIMIT);
            if (size > 0) {
                return new String(data, 0, size);
            } else {
                return "";
            }
        } catch (IOException e) {
            return "";
        }
    }

    public boolean isConnected() {
        if (socket == null) return false;
        else return socket.isConnected();
    }

    private Socket socket = null;

}
