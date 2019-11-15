package ryulib;

import android.util.Log;

public class SimpleThread {

    public SimpleThread(Runnable runnable) {
        thread = new Thread(runnable);
    }

    public void start() {
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        try {
            thread.join();
        } catch (Exception e) {
            Log.e("SimpleThread", e.getMessage());
        }
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            Log.e("SimpleThread", e.getMessage());
        }
    }

    public synchronized void sleepTight() {
        try {
            wait();
        } catch (Exception e) {
            Log.e("SimpleThread", e.getMessage());
        }
    }

    public synchronized void wakeUp() {
        notifyAll();
    }

    public boolean is_running() {
        return running;
    }

    private Thread thread;
    private boolean running = false;
}
