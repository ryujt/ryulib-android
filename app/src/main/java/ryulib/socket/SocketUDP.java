package ryulib.socket;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocketUDP{

    public void open() {
        try {
            socket_ = new DatagramSocket();
        } catch (SocketException e) {
            Log.e("SocketUDP", e.getMessage());
        }
    }

    public void close() {
        socket_.close();
    }

    @SuppressLint("StaticFieldLeak")
    public void send(String host, int port, String text) {
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            Log.e("SocketUDP", e.getMessage());
        }

        byte[] buffer = text.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, addr, port);

        new AsyncTask<DatagramPacket, Void, Void>() {
            @Override
            protected Void doInBackground(DatagramPacket... packets) {

                try {
                    socket_.send(packets[0]);
                } catch (IOException e) {
                    Log.e("SocketUDP", e.getMessage());
                }

                return null;
            }
        }.execute(packet);
    }

    @SuppressLint("StaticFieldLeak")
    public void broadcast(int port, String text) {
        byte[] broadcast_ip = new byte[] { -1, -1, -1, -1 };
        InetAddress addr = null;
        try {
            addr = InetAddress.getByAddress(broadcast_ip);
        } catch (UnknownHostException e) {
            Log.e("SocketUDP", e.getMessage());
        }

        byte[] buffer = text.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, addr, port);

        new AsyncTask<DatagramPacket, Void, Void>() {
            @Override
            protected Void doInBackground(DatagramPacket... packets) {

                try {
                    socket_.send(packets[0]);
                } catch (IOException e) {
                    Log.e("SocketUDP", e.getMessage());
                }

                return null;
            }
        }.execute(packet);
    }

    DatagramSocket socket_;

}
