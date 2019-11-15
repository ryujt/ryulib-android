package ryulib.socket;

import android.util.Log;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import ryulib.listeners.OnNotifyListener;
import ryulib.listeners.OnStringListener;
import ryulib.SimpleThread;

public class TcpClient {

    private void do_connect(Job job) {
        socket.connect(job.host, job.port);
        if (socket.isConnected()) {
            connected = true;
            if (onConnected != null) onConnected.onNotify(this);
        }
    }

    private void do_disconnect(Job job) {
        socket.disconnect();
        connected = false;
    }

    private void do_sendText(Job job) {
        if (socket.sendText(job.data) == false) {
            if (connected) {
                connected = false;
                if (onDisconnected != null) onDisconnected.onNotify(this);
            }
        }
    }

    private Job nullJob = new Job(JOB_NOTHING);

    private synchronized Job getNextJob() {
        try {
            return queue.remove();
        } catch (NoSuchElementException e) {
            return nullJob;
        }
    }

    public TcpClient() {
        worker = new SimpleThread(new Runnable() {
            @Override
            public void run() {
                while (worker.is_running()) {
                    if (connected) {
                        String text = socket.readText();
                        if (text.length() > 0) {
                            if (onReceived != null) onReceived.onString(this, text);
                        }

                        if (socket.isConnected() == false) {
                            connected = false;
                            if (onDisconnected != null) onDisconnected.onNotify(this);
                        }
                    }

                    Job job = getNextJob();
                    switch (job.kind) {
                        case JOB_CONNECT:
                            do_connect(job);
                            break;
                        case JOB_DISCONNECT:
                            do_disconnect(job);
                            break;
                        case JOB_SENDTEXT:
                            do_sendText(job);
                            break;
                    }
                }
            }
        });
        worker.start();
    }

    public synchronized void connect(String host, int port) {
        queue.clear();
        queue.offer(new Job(JOB_CONNECT, host, port));
    }

    public synchronized void disconnect() {
        queue.offer(new Job(JOB_DISCONNECT));
    }

    public synchronized void sendText(String text) {
        queue.offer(new Job(JOB_SENDTEXT, text));
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    private SocketTCP socket = new SocketTCP();
    private Queue<Job> queue = new LinkedList<>();
    private SimpleThread worker;
    private boolean connected = false;

    public void setOnConnected(OnNotifyListener onConnected) {
        this.onConnected = onConnected;
    }

    public void setOnDisconnected(OnNotifyListener onDisconnected) {
        this.onDisconnected = onDisconnected;
    }

    public void setOnReceived(OnStringListener onReceived) {
        this.onReceived = onReceived;
    }

    private OnNotifyListener onConnected = null;
    private OnNotifyListener onDisconnected = null;
    private OnStringListener onReceived = null;

    private static final int JOB_NOTHING = 0;
    private static final int JOB_CONNECT = 1;
    private static final int JOB_DISCONNECT = 2;
    private static final int JOB_SENDTEXT = 3;

    private class Job {
        int kind;
        String data;
        String host;
        int port;

        public Job(int kind, String host, int port) {
            this.kind = kind;
            this.host = host;
            this.port = port;
        }

        public Job(int kind, String data) {
            this.kind = kind;
            this.data = data;
        }

        public Job(int kind) {
            this.kind = kind;
        }
    };

}
