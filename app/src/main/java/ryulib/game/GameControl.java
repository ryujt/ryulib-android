package ryulib.game;

import ryulib.listeners.OnNotifyListener;

public class GameControl extends GameControlBase {
	
	public final GameControl checkCollision(GameControl target) {
		if (gameControlList != null) {
			return gameControlList.checkCollision(target);
		} else {
			return null;
		}		
	}
	
	public final void checkCollision(GameControl target, OnNotifyListener event) {
		if (gameControlList != null)
			gameControlList.checkCollision(target, event);
	}


}
