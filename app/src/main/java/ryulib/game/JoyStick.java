package ryulib.game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class JoyStick extends JoyStickInterface implements SensorEventListener {

	public JoyStick() {
		super();
		
	}

	public JoyStick(int speed) {
		super(speed);
		
	}
	
	public void PrepareOrientationSensor(Context context) {
	    SensorManager _SensorManager;
	    Sensor _Sensor;    
	    
		_SensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		_Sensor = _SensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
        if (_Sensor != null) {
        	_SensorManager.registerListener(this, _Sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

	private int targetY = 0;
	private boolean isMoving = false;
	private int defaultAngle = 0;
	private int angleLimit = 5;
	private boolean enabled = true;
	
	@Override
	public void tick(long tick) {
		if (enabled == false) return;
		
		super.tick(tick);
		
		if (isMoving) {
			if ((dy < 0) && (y <= targetY)) {
				isMoving = false;
				dy = 0;
				y = targetY;
			}
			
			if ((dy > 0) && (y >= targetY)) {
				isMoving = false;
				dy = 0;
				y = targetY;
			}
		}
	}
	
	public void onKeyDown(GameEngineInfo platformInfo, int keyCode, KeyEvent msg) {
		if (enabled == false) return;
		
		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT: _DX = -1; break;
	
			case KeyEvent.KEYCODE_DPAD_RIGHT: _DX = +1; break;
	
			case KeyEvent.KEYCODE_DPAD_UP: 
			case KeyEvent.KEYCODE_Q: dy = -1; break;
	
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_W: dy = +1; break;
		}
    }

	public void onKeyUp(GameEngineInfo platformInfo, int keyCode, KeyEvent msg) {
		if (enabled == false) return;
		
		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT: if (_DX == -1) _DX =0; break;
		
			case KeyEvent.KEYCODE_DPAD_RIGHT: if (_DX == +1) _DX =0; break;
	
			case KeyEvent.KEYCODE_DPAD_UP: 
			case KeyEvent.KEYCODE_Q: if (dy == -1) dy =0; break;
			
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_W: if (dy == +1) dy =0; break;
		}
    }

	public void onTouchEvent(GameEngineInfo platformInfo, MotionEvent event) {
		if (enabled == false) return;
		
		targetY = (int) event.getY();
		isMoving = true;
		
		if (targetY > y) dy = 1;
		else if (targetY < y) dy = -1;
		else isMoving = false;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	public void onSensorChanged(SensorEvent event) {
		if (enabled == false) return;
		
		int x = (int) event.values[SensorManager.DATA_Y];
		int y = (int) event.values[SensorManager.DATA_Z];
		
		if (x < (-angleLimit)) _DX = 1;
		else if (x > (angleLimit)) _DX = -1;
		else _DX = 0;
		
		if (y < (defaultAngle - angleLimit)) dy = -1;
		else if (y > (defaultAngle + angleLimit)) dy = 1;
		else dy = 0;
	}

	public void setDefaultAngle(int _DefaultAngle) {
		this.defaultAngle = _DefaultAngle;
	}

	public int getDefaultAngle() {
		return defaultAngle;
	}

	public void setAngleLimit(int value) {
		angleLimit = value;
	}

	public int getAngleLimit() {
		return angleLimit;
	}

	public void setEnabled(boolean value) {
		enabled = value;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
}
