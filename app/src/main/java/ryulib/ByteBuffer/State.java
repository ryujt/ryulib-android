package ryulib.ByteBuffer;

public class State {

	protected ByteBuffer _ByteBuffer;
	
	public State(ByteBuffer owner) {
		_ByteBuffer = owner;
	}
	
	public void actionIn(State oldState) {}
	public void actionOut(State newState) {}
	
	public void clear() {}
	public void dataIn(byte[] data) {}
	public byte[] read() { return null; }
}
