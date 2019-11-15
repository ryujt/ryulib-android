package ryulib;

import android.util.Log;

import java.util.concurrent.atomic.AtomicReference;

import ryulib.listeners.OnNotifyListener;

public class ThreadRepeater implements Runnable {
	
	public final void start() {
		active = true;
		Thread temp = new Thread(this);
		temp.start();
		thread.set(temp);
	}
	
	public final void pause() {
		pause = true;
	}
	
	public final void resume() {
		pause = false;
	}
	
	public final void sleep(long time) {
		try {
    		Thread.sleep(time);
		} catch (InterruptedException e) {
			Log.e("ThreadRepeater.sleep()", e.getMessage());
		}       		
	}
	
	public final void stop() {
        active = false;
		thread.set(null);
	}

	public final void termiate() {
        active = false;

		Thread temp = thread.getAndSet(null);
		if (temp == null) return;

        boolean retry = true;
        while (retry) {
            try {
            	temp.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}

	public final void run() {
		while (active) {
			if ((pause == false) && (onRepeat != null)) {
				onRepeat.onNotify(this);
        		if (interval > 0) sleep(interval);
			} else {
				sleep(5);
			}
		}
	}
	
	public final void setOnRepeat(OnNotifyListener value) {
		onRepeat = value;
	}

	public final void setInterval(long value) {
		interval = value;
	}

	public final long getInterval() {
		return interval;
	}

	public Boolean isActive() {
		return active;
	}

	private Boolean active = false;
	private Boolean pause = false;
	private AtomicReference<Thread> thread = new AtomicReference<>();
	private long interval = 0;
	private OnNotifyListener onRepeat = null;

}
