package ryulib.game;

import java.util.ArrayList;

public class GameMessageList {
	
	private ArrayList<GameMessage> list = new ArrayList<GameMessage>();
	
	public synchronized void clear() {
		list.clear();
	}

	public synchronized void add(GameMessage gameMessage) {
		list.add(gameMessage);
	}

	public synchronized void remove(GameMessage gameMessage) {
		list.remove(gameMessage);
	}
	
	private GameMessage nullMessage = new GameMessage();
	
	public synchronized GameMessage get() {
		if (list.size() == 0) return nullMessage;
		
		GameMessage gameMessage = list.get(0);
		list.remove(0);
		
		return gameMessage;
	}
	
	public synchronized int size() {
		return list.size();
	}

}
