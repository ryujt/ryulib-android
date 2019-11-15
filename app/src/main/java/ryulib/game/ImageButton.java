package ryulib.game;

import ryulib.listeners.OnNotifyListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class ImageButton extends GameControl {

	private boolean downed = false;

	private Resources resources = null;
	private Canvas canvas = null;
	private Bitmap bitmapUp = null;
	private Bitmap bitmapDown = null;
	
	// Property
	private Point position = new Point(0, 0);
	private int imageUp = -1;
	private int imageDown = -1;
	private Paint paint = new Paint();
	private String caption = "";
	private OnNotifyListener onClick = null;
	
	@Override
	protected void onStart(GameEngineInfo gameEngineInfo) {
		resources = gameEngineInfo.getGamePlatform().getContext().getResources();
		canvas = gameEngineInfo.getCanvas();
	}

	@Override
	protected void onDraw(GameEngineInfo gameEngineInfo) {
		Bitmap bitmap = null;
		
		if (downed) {
			if (bitmapDown == null) bitmapDown = loadBitmap(resources, imageDown);
			bitmap = bitmapDown;
		} else {
			if (bitmapUp == null) bitmapUp = loadBitmap(resources, imageUp);
			bitmap = bitmapUp;
		}
		
		if (bitmap != null) {
			canvas.drawBitmap(bitmap, position.x, position.y, paint);
		}
		
		getBoundary().drawTextCenter(canvas, paint, caption);
	}
	
	@Override
    protected final boolean onTouchEvent(GameEngineInfo gameEngineInfo, MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		boolean isMyArea = getBoundary().isMyArea(x, y); 
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: doActionDown(isMyArea); break;
			case MotionEvent.ACTION_UP: doActionUp(isMyArea); break;
		}

		return isMyArea;
    }
	
	private Bitmap loadBitmap(Resources resources, int id) {
		Bitmap bitmap = null;
		
		if (id > -1) {
			bitmap = BitmapFactory.decodeResource(resources, id);
			getBoundary().setBoundary(
					position.x,
					position.y,
					position.x+bitmap.getWidth(),
					position.y+bitmap.getHeight()
			);
		}
		
		return bitmap;
	}

	private void doActionDown(boolean isMyArea) {
		downed = isMyArea;
	}
    
	private void doActionUp(boolean isMyArea) {
		if (isMyArea && downed) {
			if (onClick != null)
				onClick.onNotify(this);
		}
		
		downed = false;
	}
    
	public final void setImageUp(int value) {
		imageUp = value;
		bitmapUp = null;
	}
	
	public final int getImageUp() {
		return imageUp;
	}

	public final void setImageDown(int value) {
		imageDown = value;
		bitmapDown = null;
	}

	public final int getImageDown() {
		return imageDown;
	}

	public void setOnClick(OnNotifyListener value) {
		onClick = value;
	}

	public OnNotifyListener getOnClick() {
		return onClick;
	}
	
	private void changeBoundary() {
		getBoundary().setLeft(position.x);
		getBoundary().setTop(position.y);
	}
	
	public void setPosition(int x, int y) {
		position.x = x;
		position.y = y;
		changeBoundary();
	}

	public void setX(int value) {
		position.x = value;
		changeBoundary();
	}

	public int getX() {
		return position.x;
	}

	public void setY(int value) {
		position.y = value;
		changeBoundary();
	}

	public int getY() {
		return position.y;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setCaption(String value) {
		caption = value;
	}

	public String getCaption() {
		return caption;
	}

}
