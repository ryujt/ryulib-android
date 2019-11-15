package ryulib.game;

import java.util.ArrayList;

import ryulib.listeners.OnNotifyListener;

import android.view.KeyEvent;
import android.view.MotionEvent;

public class GameControlList {
	
	private ArrayList<GameControlBase> list = new ArrayList<GameControlBase>();
	
	private ArrayList<GameControlBase> queAdd = new ArrayList<GameControlBase>();
	private ArrayList<GameControlBase> queRemove = new ArrayList<GameControlBase>();
	private ArrayList<GameControlBase> queSendToBack = new ArrayList<GameControlBase>();
	private ArrayList<GameControlBase> queBringToFront = new ArrayList<GameControlBase>();

	public final void iterate(GameControlCallBack callBack) {
		synchronized (list) {
			for (GameControlBase control : list) {
				if (callBack.execute(control)) break;
			}
		}
	}

	public final void clear() {
		synchronized (list) {
			for (GameControlBase control : list) {
				queRemove.add(control);
			}
		}
	}
	
	public final void add(GameControlBase control) {
		synchronized (queAdd) {
			queAdd.add(control);
		}
	}
	
	public final void remove(GameControlBase control) {
		synchronized (queRemove) {
			queRemove.add(control);
		}
	}
	
	public final void bringToFront(GameControlBase control) {
		synchronized (queBringToFront) {
			queBringToFront.add(control);
		}
	}
	
	public final void sendToBack(GameControlBase control) {
		synchronized (queSendToBack) {
			queSendToBack.add(control);
		}
	}

	public final GameControl checkCollision(GameControl target) {
		synchronized (list) {
			int size = list.size();
			for (int i=0; i<size; i++) {
				GameControlBase control = list.get(i);

				HitArea _HitArea = control.getHitArea();
				
				if ((isControlVisible(control) == false) || (_HitArea == null) || (control == target)) continue;
				
				if (target.getHitArea().checkCollision(_HitArea)) {
					return (GameControl) control;
				}
			}
		}

		return null;
	}
	
	public final void checkCollision(GameControl target, OnNotifyListener event) {
		synchronized (list) {
			int size = list.size();
			for (int i=0; i<size; i++) {
				GameControlBase control = list.get(i);
				HitArea _HitArea = control.getHitArea();
				
				if ((isControlVisible(control) == false) || (_HitArea == null) || (control == target)) continue;
				
				if (target.getHitArea().checkCollision(_HitArea)) {
					event.onNotify(control);
				}
			}
		}
	}
	
	public final void onRepeate(GameEngineInfo platformInfo) {
		gameControlIndex = 0;
		GameControlBase control = getGameControl();
		while (control != null) {

			control.onRepeate(platformInfo);
			control = getGameControl();
		}
	}
	
	public final boolean onKeyDown(GameEngineInfo platformInfo, int keyCode, KeyEvent msg) {
		synchronized (list) {
			int size = list.size();
			for (int i=size-1; i>=0; i--) {
				GameControlBase control = list.get(i);
				if (isControlAvailable(control) == false) continue;

				if (control.onKeyDown(platformInfo, keyCode, msg)) return true;
			}
			
			return false;
		}
    }

	public final boolean onKeyUp(GameEngineInfo platformInfo, int keyCode, KeyEvent msg) {
		synchronized (list) {
			int size = list.size();
			for (int i=size-1; i>=0; i--) {
				GameControlBase control = list.get(i);
				if (isControlAvailable(control) == false) continue;
				
				if (control.onKeyUp(platformInfo, keyCode, msg)) return true;
			}
		}
		
		return false;
    }
    
	public final boolean onTouchEvent(GameEngineInfo platformInfo, MotionEvent event) {
		boolean result = false;
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: 
				result = do_ActionDown(platformInfo, event);
				break;
				
			case MotionEvent.ACTION_UP: 
				result = do_ActionUp(platformInfo, event);
				break;
				
			default: 
				result = do_ActionOthers(platformInfo, event);
				break;
		}
		
		return result;
	}
	
	public final void arrangeControls() {
		synchronized (list) {
			int size = list.size();
			for (int i=size-1; i>=0; i--) {
				GameControlBase control = list.get(i);

				if (control.deleted) {
					control.gameControlList = null;
					list.remove(i);
					continue;
				} 
			}
		}
			
		synchronized (queRemove) {
			int size = queRemove.size();
			for (int i=0; i<size; i++) {
				GameControlBase control = queRemove.get(i);
				
				synchronized (list) {
					control.gameControlList = null;
					list.remove(control);
				}
			}
			
			queRemove.clear();
		}

		synchronized (queAdd) {
			int size = queAdd.size();
			for (int i=0; i<size; i++) {
				GameControlBase control = queAdd.get(i);
				
				synchronized (list) {
					control.gameControlList = this;
					list.add(control);
				}
			}
			
			queAdd.clear();
		}

		synchronized (queBringToFront) {
			int size = queBringToFront.size();
			for (int i=0; i<size; i++) {
				GameControlBase control = queBringToFront.get(i);
				
				synchronized (list) {
					list.remove(control);
					list.add(control);
				}
			}
			
			queBringToFront.clear();
		}

		// TODO: 락 효율 문제 있음
		synchronized (queSendToBack) {
			int size = queSendToBack.size();
			for (int i=0; i<size; i++) {
				GameControlBase control = queSendToBack.get(i);

				synchronized (list) {
					list.remove(control);
					list.add(0, control);
				}
			}
			
			queSendToBack.clear();
		}
	}
	
	private int gameControlIndex = 0;
	
	private GameControlBase getGameControl() {
		synchronized (list) {
			if (list.size() == 0) return null;

			if (gameControlIndex >= list.size()) {
				gameControlIndex = 0;
				return null;
			}
			
			GameControlBase control = list.get(gameControlIndex);
			gameControlIndex++;
			
			return control;
		}		
	}
	
	private GameControlBase focusedObject = null;
	
	private boolean canHaveFocus(MotionEvent event, GameControlBase control) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		return 
			((focusedObject == null) || ((focusedObject != null) && (focusedObject.deleted))) &&
			(control.getBoundary().isMyArea(x, y));
	}
	
	private boolean do_ActionDown(GameEngineInfo platformInfo, MotionEvent event) {
		synchronized (list) {
			int size = list.size();
			for (int i=size-1; i>=0; i--) {
				GameControlBase control = list.get(i);
				if (isControlVisible(control) == false) continue;
				
				if (canHaveFocus(event, control)) {
					focusedObject = control;
					control.focused = true;
				}
				
				if (control.onTouchEvent(platformInfo, event)) return true;
			}
			
			return false;
		}		
	}
	
	private boolean do_ActionUp(GameEngineInfo platformInfo, MotionEvent event) {
		boolean result = false;
		
		synchronized (list) {
			int size = list.size();
			for (int i=size-1; i>=0; i--) {
				GameControlBase control = list.get(i);
				if (isControlVisible(control) == false) continue;

				if (control.onTouchEvent(platformInfo, event)) {
					result = true;
					break;
				}
			}

			if (focusedObject != null) focusedObject.focused = false;
			focusedObject = null;
			
			return result;
		}		
	}
	
	private boolean do_ActionOthers(GameEngineInfo platformInfo, MotionEvent event) {
		synchronized (list) {
			int size = list.size();
			for (int i=size-1; i>=0; i--) {
				GameControlBase control = list.get(i);
				if (isControlVisible(control) == false) continue;

				if (control.onTouchEvent(platformInfo, event)) return true;
			}
			
			return false;
		}		
	}
	
	private boolean isControlAvailable(GameControlBase control) {
		return (control.deleted == false) && (control.getVisible()) && (control.getEnabled());
	}

	private boolean isControlVisible(GameControlBase control) {
		return (control.deleted == false) && (control.getVisible());
	}
	
}
