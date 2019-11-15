package ryulib.game;

import ryulib.graphic.Scroll;

public abstract class JoyStickInterface {
	
	public JoyStickInterface() {
		super();
	}
	
	public JoyStickInterface(int speed) {
		super();
		
		_Scroll.setSpeed(speed);
	}

	protected int _X = 0;
	protected int y = 0;
	
	private int _LeftLimit = 0;  
	private int _TopLimit = 0;  
	private int _RightLimit = 0;  
	private int _BottomLimit = 0;
	
	private int _ObjectWidth = 0;
	private int _ObjectHeight = 0;
	
	private boolean _UseBoundaryLimit = false;
	
	protected int _DX = 0;
	protected int dy = 0;
	
	private Scroll _Scroll = new Scroll();
	
	public void setBoundaryLimit(boolean useBoundaryLimit, 
			int leftLimit, int topLimit, 
			int rightLimit, int bottomLimit, 
			int objectWidth, int objectHeight) {
		
		_UseBoundaryLimit = useBoundaryLimit;
		_LeftLimit = leftLimit;
		_TopLimit = topLimit;
		_RightLimit = rightLimit;
		_BottomLimit = bottomLimit;
		_ObjectWidth = objectWidth;
		_ObjectHeight = objectHeight;
	}
	
	public void tick(long tick) {
		int temp = _Scroll.move(tick);
		_X = _X + (_DX * temp);
		y = y + (dy * temp);
		
		if (_UseBoundaryLimit == false) return;
		
		if (_X < _LeftLimit) {
			_X = _LeftLimit;
		} else if (_X > (_RightLimit-_ObjectWidth)) {
			_X = _RightLimit-_ObjectWidth;
		}
		
		if (y < _TopLimit) {
			y = _TopLimit;
		} else if (y > (_BottomLimit-_ObjectHeight)) {
			y = _BottomLimit-_ObjectHeight;
		}
	}
	
	public int getX() {
		return _X;		
	}
	
	public void setX(int value) {
		_X = value;
	}
	
	public int getY() {
		return y;
	}

	public void setY(int value) {
		y = value;
	}
	
    public int getSpeed() {
    	return _Scroll.getSpeed();
    }
    
    public void setSpeed(int value) {
    	_Scroll.setSpeed(value);
    }

	public void setLeftLimit(int value) {
		this._LeftLimit = value;
	}

	public int getLeftLimit() {
		return _LeftLimit;
	}
    
	public void setTopLimit(int value) {
		this._TopLimit = value;
	}

	public int getTopLimit() {
		return _TopLimit;
	}

	public void setRightLimit(int value) {
		this._RightLimit = value;
	}

	public int getRightLimit() {
		return _RightLimit;
	}

	public void setBottomLimit(int value) {
		this._BottomLimit = value;
	}

	public int getBottomLimit() {
		return _BottomLimit;
	}

	public void setObjectWidth(int value) {
		this._ObjectWidth = value;
	}

	public int getObjectWidth() {
		return _ObjectWidth;
	}

	public void setObjectHeight(int value) {
		this._ObjectHeight = value;
	}

	public int getObjectHeight() {
		return _ObjectHeight;
	}

	public void setUseBoundaryLimit(boolean value) {
		this._UseBoundaryLimit = value;
	}

	public boolean isUseBoundaryLimit() {
		return _UseBoundaryLimit;
	}

}
