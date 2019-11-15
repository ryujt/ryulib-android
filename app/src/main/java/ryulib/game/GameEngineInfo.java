package ryulib.game;

import ryulib.listeners.OnHandlerMessageListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

public class GameEngineInfo {

	public GameEngineInfo(GameEngine gameEngine) {
		super();
		
		this.gameEngine = gameEngine;
		
		paint.setARGB(255, 0, 0, 0);
		paint.setAntiAlias(true);
	}

	private GameEngine gameEngine = null;
	private Bitmap bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
	private Canvas canvas = new Canvas();
	private Paint paint = new Paint();
	private long tick = 0;
	
	private GameMessageList gameMessageList = new GameMessageList();
	private GameMessage gameMessage = null;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (onHandlerMessage != null) onHandlerMessage.onReceived(msg);
		}
	};
	
	final void setTick(long value) {
		if (value > TICK_LIMIT) value = TICK_LIMIT;
		tick = value;
	}

	final void getNextMessage() {
		gameMessage = gameMessageList.get();
	}
	
	final synchronized void setSize(int width, int height) {
		  bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		  canvas.setBitmap(bitmap);
	}
	
	final synchronized void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, paint);
	}
	
	private static final int TICK_LIMIT = 500;

	private OnHandlerMessageListener onHandlerMessage = null;
	
	void setOnHandlerMessage(OnHandlerMessageListener value) {
		onHandlerMessage = value;
	}

	public final void addMessage(GameMessage gameMessage) {
		gameMessageList.add(gameMessage);
	}
	
	public final void addMessage(Object sender, int what) {
		GameMessage gameMessage = new GameMessage();
		gameMessage.sender = sender;
		gameMessage.what = what;
		
		gameMessageList.add(gameMessage);
	}
	
	public final void addMessage(Object sender, String str) {
		GameMessage gameMessage = new GameMessage();
		gameMessage.sender = sender;
		gameMessage.str = str;
		
		gameMessageList.add(gameMessage);
	}
	
	public final Bitmap getBitmap() {
		return bitmap;
	}

	public final Canvas getCanvas() {
		return canvas;
	}
	
	public final Paint getPaint() {
		return paint;
	}
	
	public final GameMessage getGameMessage() {
		return gameMessage;
	}
	
	public final Handler getHandler() {
		return handler;
	}
	
	public final long getTick() {
		return tick;
	}
	
	private Rect rect = new Rect();
	
	public final Rect getRect() {
		rect.left = 0;
		rect.top = 0;
		rect.right = canvas.getWidth();
		rect.bottom = canvas.getHeight();
		
		return rect;
	}

	public final GameEngine getGamePlatform() {
		return gameEngine;
	}
	
}
