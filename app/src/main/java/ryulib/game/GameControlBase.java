package ryulib.game;

import ryulib.graphic.Boundary;

import android.view.KeyEvent;
import android.view.MotionEvent;

public class GameControlBase {
	
	GameControlList gameControlList = null;

	boolean deleted = false;

	boolean focused = false;
	
	boolean started = false;
	
	void onRepeate(GameEngineInfo gameEngineInfo) {
		if (getDeleted()) return; 

		if (started == false) {
			started = true;
			onStart(gameEngineInfo);
		}
		
		if ((deleted == false) && enabled && visible) onTick(gameEngineInfo);
		if ((deleted == false) && visible) onDraw(gameEngineInfo);
	}
	
	protected HitArea getHitArea() {
		return null;
	}
	
	protected void onStart(GameEngineInfo gameEngineInfo) {
	}
	
	protected void onTick(GameEngineInfo gameEngineInfo) {
	}

	protected void onDraw(GameEngineInfo gameEngineInfo) {
	}
	
	protected boolean onKeyDown(GameEngineInfo gameEngineInfo, int keyCode, KeyEvent msg) {
		return false;
    }

    protected boolean onKeyUp(GameEngineInfo gameEngineInfo, int keyCode, KeyEvent msg) {
		return false;
    }

    protected boolean onTouchEvent(GameEngineInfo gameEngineInfo, MotionEvent event) {
		return false;
    }
    
	public final void bringToFront() {
		if (gameControlList != null)
			gameControlList.bringToFront(this);
	}
	
	public final void sendToBack() {
		if (gameControlList != null)
			gameControlList.sendToBack(this);
	}
	
	public final GameControlList getGameControlList() {
		return gameControlList;
	}

	public final Boundary getBoundary() {
		return boundary;
	}

    public final boolean getVisible() {
    	return visible;
    }
    
    public final void setVisible(boolean value) {
    	visible = value;
    }
    
    public final boolean getEnabled() {
    	return enabled;
    }
    
    public final void setEnabled(boolean value) {
    	enabled = value;
    }

	public final boolean isStarted() {
		return started;
	}

	public final boolean getFocused() {
		return focused;
	}
	
	public final boolean getDeleted() {
		return deleted;
	}
	
	public final void delete() {
		deleted = true;
	}

	protected Boundary boundary = new Boundary(0, 0, 0, 0);
	protected boolean visible = true;
	protected boolean enabled = true;

}
