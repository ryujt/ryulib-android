package ryulib.game;

import ryulib.graphic.Scroll;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BackgroundImage {

	private int _X01 = 0;
	private int _X02 = 0;
	private Scroll _Scroll = new Scroll();

	private Bitmap _Bitmap = null;
	
	public void draw(Canvas canvas, Paint paint, long tick) {
		if ((_Bitmap.getWidth()+_X01) > 0) {
			canvas.drawBitmap(_Bitmap, _X01, 0, paint);
		}
		
		if ((_Bitmap.getWidth()+_X01) <= canvas.getWidth()) {
			canvas.drawBitmap(_Bitmap, _X02, 0, paint);
		}
		
		_X01 = _X01 - _Scroll.move(tick);
		_X02 = _X01 + _Bitmap.getWidth();
		
		if (_X02+(_Bitmap.getWidth()) <= canvas.getWidth()) {
			int temp = _X01;
			_X01 = _X02;
			_X02 = temp + _Bitmap.getWidth();
		}
	}
	
	public void setBitmap(Bitmap value) {
		_Bitmap = value;
	}

	public Bitmap getBitmap() {
		return _Bitmap;
	}

	public void setSpeed(int value) {
		_Scroll.setSpeed(value);
	}

	public int getSpeed() {
		return _Scroll.getSpeed();
	}

}
