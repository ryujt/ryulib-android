package ryulib;

import android.os.Handler;

import ryulib.listeners.OnTimerListener;

public class Timer {
	
	public void start() {
		_OldTick = System.currentTimeMillis();
		_Active = true;
		_Handler.postDelayed(_Runnable, _Interval);
	}
	
	public void stop() {
		_Active = false;
	}

	// Interval Property
	private int _Interval = 1000;
	public int getInterval() {
		return _Interval;
	}
	public void setInterval(int AValue) {
		_Interval = AValue;
	}
	
	// Active Property
	private boolean _Active = false;
	public boolean getActive() {
		return _Active;
	}

	// OnTimer Event
	private OnTimerListener _OnTimer = null;
	public void setOnTimer(OnTimerListener AValue) {
		_OnTimer = AValue;
	}
	
    private long _OldTick = System.currentTimeMillis();
    
    private Handler _Handler = new Handler();
    private Runnable _Runnable = new Runnable() {
    	public void run() {
    		if (getActive() == false) return;

    		long _Tick = System.currentTimeMillis();
  			if (_OnTimer != null) _OnTimer.onTime(_Tick-_OldTick);
    		_OldTick = _Tick;

  			if (_Interval > 0) _Handler.postDelayed(this, _Interval);
  			else _Handler.post(this);
    	}
    };
    
}
