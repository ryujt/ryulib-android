package ryulib.ByteBuffer;

import java.util.ArrayList;

public class ByteBuffer {
	
	public ByteBuffer() {
		_State = _StateEmpty;
	}

	ArrayList<byte[]> _Buffer = new ArrayList<byte[]>();
	
	public void clear() {
		_State.clear();
	}
	
	public void dataIn(byte[] data) {
		_State.dataIn(data);
	}
	
	public byte[] read() { 
		return _State.read(); 
	}
	
	private State _State;
	State _StateEmpty = new StateEmpty(this);
	State _StateNormal = new StateNormal(this);	
	
	public void setState(State value) {
		// TODO :
		android.util.Log.i("FFClient", value.toString() + " Count: " + Integer.toString(_Buffer.size()));

		synchronized (_State) {
			State oldState = _State;
			State newState = value;
			
			oldState.actionOut(newState);
			_State = newState;
			newState.actionIn(oldState);			
		}
	}
	
	int _Capacity = 0;
	
	public int getCapacity() {
		return _Capacity;
	}
	
	public void setCapacity(int value) {
		_Capacity = value;
	}
	
}
