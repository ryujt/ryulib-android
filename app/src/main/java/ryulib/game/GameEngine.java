package ryulib.game;

import ryulib.listeners.OnHandlerMessageListener;
import ryulib.listeners.OnNotifyListener;
import ryulib.ThreadRepeater;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameEngine extends SurfaceView implements SurfaceHolder.Callback {

	public GameEngine(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init();
	}

	public GameEngine(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public GameEngine(Context context) {
		super(context);

		init();
	}

	private void init() {
	    surfaceHolder.addCallback(this);
		repeater.setOnRepeat(onRepeat);

	    setFocusable(true);
	}	

	private ArrayList<GameControlList> layers = new ArrayList<>();
	private GameEngineInfo gameEngineInfo = new GameEngineInfo(this);
    private SurfaceHolder surfaceHolder = getHolder();
	private ThreadRepeater repeater = new ThreadRepeater();

	private boolean useKeyEvent = false;
	private boolean useMotionEvent = false;
	private boolean autoInvalidate = false;

	private void start() {
		_OldTick = System.currentTimeMillis();
		repeater.start();
	}

	public void stop() {
		repeater.stop();
	}
	
	public final void clearControls() {
		for (GameControlList layer : layers) {
			layer.clear();
		}
	}
	
	public final void surfaceCreated(SurfaceHolder holder) {
		start();
	}

	public final void surfaceDestroyed(SurfaceHolder holder) {
		stop();
	}
	
    public final void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	gameEngineInfo.setSize(width, height);
	}

	@Override
    public final boolean onKeyDown(int keyCode, KeyEvent msg) {
    	if (useKeyEvent) {
			for (GameControlList layer : layers) {
				layer.onKeyDown(gameEngineInfo, keyCode, msg);
			}
    		if (autoInvalidate) onRepeat.onNotify(repeater);
    	}
        return true;
    }

	@Override
    public final boolean onKeyUp(int keyCode, KeyEvent msg) {
    	if (useKeyEvent) {
			for (GameControlList layer : layers) {
				layer.onKeyUp(gameEngineInfo, keyCode, msg);
			}
    		if (autoInvalidate) onRepeat.onNotify(repeater);
    	}
        return true;
    }

	@Override
	public final boolean onTouchEvent(MotionEvent event) {
		if (useMotionEvent) {
			for (GameControlList layer : layers) {
				layer.onTouchEvent(gameEngineInfo, event);
			}
    		if (autoInvalidate) onRepeat.onNotify(repeater);
		}
		return true;
	}
	
	private long _OldTick = System.currentTimeMillis();

    private OnNotifyListener onRepeat = new OnNotifyListener() {
    	public synchronized void onNotify(Object sender) {
			Canvas canvas = surfaceHolder.lockCanvas(null);
            try {
                if (canvas == null) {
                	return;
				}
	                
				long tick = System.currentTimeMillis();
				gameEngineInfo.setTick(tick - _OldTick);
				gameEngineInfo.getNextMessage();

				for (GameControlList layer : layers) {
					layer.onRepeate(gameEngineInfo);
				}

				_OldTick = tick;

        		gameEngineInfo.draw(canvas);
            } finally {
                if (canvas != null) {
                	surfaceHolder.unlockCanvasAndPost(canvas);
                }
	    	}

			for (GameControlList layer : layers) {
				layer.arrangeControls();
			}
		}
	};
	
	public final GameControlList addLayer() throws Exception {
		if (repeater.isActive()) throw new Exception("can not add a layer to GameEngine after start().");

		GameControlList list = new GameControlList();
		layers.add(list);
		return list;
	}
	
	public final GameEngineInfo getGamePlatformInfo() {
		return gameEngineInfo;
	}

	public final void setOnHandlerMessage(OnHandlerMessageListener value) {
		gameEngineInfo.setOnHandlerMessage(value);
	}

	public final void setUseKeyEvent(boolean _UseKeyEvent) {
		this.useKeyEvent = _UseKeyEvent;
	}

	public final boolean getUseKeyEvent() {
		return useKeyEvent;
	}

	public final void setUseMotionEvent(boolean _UseMotionEvent) {
		this.useMotionEvent = _UseMotionEvent;
	}
	
	public final boolean getUseMotionEvent() {
		return useMotionEvent;
	}

	public final void setRepeatInterval(long value) {
		repeater.setInterval(value);
	}

	public final long getRepeatInterval() {
		return repeater.getInterval();
	}

	public final void setAutoInvalidate(boolean value) {
		autoInvalidate = value;
	}

	public final boolean isAutoInvalidate() {
		return autoInvalidate;
	}

}
