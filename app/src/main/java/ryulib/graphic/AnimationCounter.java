package ryulib.graphic;

public class AnimationCounter {

	public AnimationCounter() {
		super();
	}

	public AnimationCounter(int interval, int size) {
		super();
		
		_Interval = interval;
		_Size = size;
		
		clear();
	}

	private long _TickCount = 0;
	
	// Property 
	private int _Interval = 0;
	private int _Size = 0;
	private int _Index =0;
	private boolean _AutoRewind = true;
	
	public void clear() {
		_Index = 0;
		_TickCount = 0;
	}

	public void tick(long tick) {
		_TickCount = _TickCount + tick;
		
		if (_TickCount >= _Interval) {
			_TickCount = _TickCount - _Interval;
			_Index++;

			if ((_AutoRewind) && (_Index >= _Size)) {
				_Index = 0;
			}
		}
	}
	
	public void setInterval(int value) {
		_Interval = value;
	}
	
	public int getInterval() {
		return _Interval;
	}
	
	public void setSize(int value) {
		_Size = value;
	}

	public int getSize() {
		return _Size;
	}

	public int getIndex() {
		return _Index;
	}

	public void setAutoRewind(boolean value) {
		_AutoRewind = value;
	}

	public boolean isAutoRewind() {
		return _AutoRewind;
	}

}
