package ryulib.graphic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Boundary {
	
	public Boundary(int Left, int Top, int Right, int Bottom) {
		setBoundary(Left, Top, Right, Bottom);
	}
	
	public Boundary(Boundary boundary) {
		setBoundary(boundary);
	}

	protected int left;
	protected int top;
	protected int right;
	protected int bottom;
	protected Rect rect = new Rect();
	
	private boolean check_LineCollision(int S1, int E1, int S2, int E2) {
		boolean A = (S1 <= S2) && (S2 <= E1);
		boolean B = (S1 <= E2) && (E2 <= E1);
		boolean C = (S2 <= S1) && (S1 <= E2);
		boolean D = (S2 <= E1) && (E1 <= E2);
		
		return (A || B || C || D);			
	}
	
	public boolean CheckCollision(Boundary ADest) {
		return 
			check_LineCollision(left, right, ADest.getLeft(), ADest.getRight()) &&
			check_LineCollision(top, bottom, ADest.getTop(), ADest.getBottom());
	}
	
	public void setLeft(int ALeft) {
		int temp = getWidth();
		
		left = ALeft;
		right = left + temp;
	}
	
	public void incLeft(int value) {
		left += value;
		right = right + value;
	}
	
	public void setTop(int ATop) {
		int temp = getHeight();
		
		top = ATop;
		bottom = top + temp;
	}
	
	public void incTop(int value) {
		top += value;
		bottom = bottom + value;
	}
	
	public void setRight(int ARight) {
		int temp = getWidth();
		
		right = ARight;
		left = right - temp;
	}
	
	public void setBottom(int ABottom) {
		int temp = getHeight();
		
		bottom = ABottom;
		top = bottom - temp;
	}

	public void setWidth(int width) {
		right = left + width;
	}

	public void setHeight(int height) {
		bottom = top + height;
	}

	public void setBoundary(int ALeft, int ATop, int ARight, int ABottom) {
		left = ALeft;
		top = ATop;
		right = ARight;
		bottom = ABottom;
	}
	
	public void setBoundary(Boundary value) {
		left = value.left;
		top = value.top;
		right = value.right;
		bottom = value.bottom;
	}
	
	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public int getRight() {
		return right;
	}

	public int getBottom() {
		return bottom;
	}
	
	public int getWidth() {
		return right - left;
	}

	public int getHeight() {
		return bottom - top;
	}
	
	public Rect getRect() {
		rect.set(left, top, right, bottom);
		return rect;
	}
	
	public Rect getRect(int x, int y) {
		rect.set(left +x, top +y, right +x, bottom +y);
		return rect;
	}
	
	public Rect getRect(Point point) {
		return getRect(point.x, point.y);
	}
	
	public int getHorizontalCenter() {
		return left + (getWidth() / 2);
	}

	public int getVerticalCenter() {
		return top + (getHeight() / 2);
	}

	public void setHorizontalCenter(int value) {
		setLeft(value - (getWidth() / 2));
	}

	public void setVerticalCenter(int value) {
		setTop(value - (getHeight() / 2));
	}

	public void setCenter(int x, int y) {
		setHorizontalCenter(x);	
		setVerticalCenter(y);
	}
	
	public boolean isMyArea(int x, int y) {
		return (x >= left) && (x <= right) && (y >= top) && (y <= bottom);
	}
	
	public void drawTextCenter(Canvas canvas, Paint paint, String text) {
		paint.setTextAlign(Paint.Align.CENTER);

		Rect rect = new Rect();
		paint.getTextBounds(text, 0, text.length(), rect);
		
		canvas.drawText(text, getHorizontalCenter(), getVerticalCenter() + ((rect.bottom-rect.top) / 2), paint);
	}

}
