package ryulib.graphic;

public class Scroll {
	
	public Scroll() {
		super();
	}

	public Scroll(int speed) {
		super();
		
		_Speed = speed;
	}

	private int _Distance = 0;
	private long _DX = 0;
	
	private int _Speed = 0;
	
	public void init() {
		_Distance = 0;
		_DX = 0;
	}
	
	public int move(long tick) {
		int result = 0;
		
		_DX = _DX + (_Speed * tick);
		
		if (_DX >= 1000) {
			 result = (int) _DX / 1000;
			_DX = _DX - (result * 1000);
			
			_Distance = _Distance + result;
		}
		
		return result;
	}
	
	public int getDistance() {
		return _Distance;
	}

	public void setSpeed(int value) {
		_Speed = value;
	}

	public int getSpeed() {
		return _Speed;
	}

}
