package ryulib;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class SuspensionQueue<T> {

    public synchronized void clear() {
        queue.clear();
    }

    public synchronized void push(T item) {
        queue.offer(item);
        notifyAll();
    }

    public synchronized T pop() {
        while (queue.peek() == null) {
            try {
                wait();
            } catch (Exception e) {
                Log.e("SuspensionQueue", e.getMessage());
            }
        }
        return queue.remove();
    }

    public synchronized T peek() {
        return queue.peek();
    }

    public synchronized boolean is_empty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }

    private Queue<T> queue = new LinkedList<>();
}
