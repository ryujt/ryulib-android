package ryulib;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import ryulib.listeners.OnIntegerListener;
import ryulib.listeners.OnNotifyListener;

public class GestureControl {

    public GestureControl(Context context, final View targetView) {
        this.context = context;
        this.targetView = targetView;

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            // TODO:

            @Override
            public boolean onDown(MotionEvent motionEvent) {
                if (GestureControl.this.onDown != null)
                    return GestureControl.this.onDown.onTouch(targetView, motionEvent);
                return true;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {
                Log.i("GestureControl", "onShowPress()");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                Log.i("GestureControl", "onSingleTapUp()");
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.i("GestureControl", "onScroll()");
                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                longPressed = true;
                if (GestureControl.this.onLongPress != null) GestureControl.this.onLongPress.onNotify(targetView);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i("GestureControl", "onFling()");
                return false;
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        int termX = x - downX;
        int termY = y - downY;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                longPressed = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if ((termX < 0) && (onSlideLeft != null)) onSlideLeft.onInteger(targetView, termX);
                if ((termX > 0) && (onSlideRight != null)) onSlideRight.onInteger(targetView, termX);
                break;

            case MotionEvent.ACTION_UP:
                boolean insideX = Math.abs(termX) < thresholdX;
                boolean insideY = Math.abs(termY) < thresholdY;
                if ((longPressed == false) && insideX && insideY && (onClick != null)) onClick.onNotify(targetView);
                break;
        }

        return gestureDetector.onTouchEvent(event);
    }

    public void setOnDown(View.OnTouchListener onDown) {
        this.onDown = onDown;
    }

    public void setOnClick(OnNotifyListener onClick) {
        this.onClick = onClick;
    }

    public void setOnLongPress(OnNotifyListener onLongPress) {
        this.onLongPress = onLongPress;
    }

    public void setOnSlideLeft(OnIntegerListener onSlideLeft) {
        this.onSlideLeft = onSlideLeft;
    }

    public void setOnSlideRight(OnIntegerListener onSlideRight) {
        this.onSlideRight = onSlideRight;
    }

    private Context context;
    private View targetView;
    private GestureDetector gestureDetector;

    private int downX = -1;
    private int downY = -1;
    private boolean longPressed = false;

    private int thresholdX = 16;
    private int thresholdY = 16;

    private View.OnTouchListener onDown = null;
    private OnNotifyListener onClick = null;
    private OnNotifyListener onLongPress = null;
    private OnIntegerListener onSlideLeft = null;
    private OnIntegerListener onSlideRight = null;

}
