package ryulib.ByteBuffer;

public class StateEmpty extends State {

	public StateEmpty(ByteBuffer owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void clear() {
		synchronized (_ByteBuffer._Buffer) {
			_ByteBuffer._Buffer.clear();
		}
	}

	@Override
	public void dataIn(byte[] data) {
		int count;
		
		synchronized (_ByteBuffer._Buffer) {
			_ByteBuffer._Buffer.add(data);
			count = _ByteBuffer._Buffer.size(); 
		}
		
		if (count >= _ByteBuffer._Capacity) {
//		if ((_ByteBuffer._Capacity == 0) || (count >= _ByteBuffer._Capacity)) {
			_ByteBuffer.setState(_ByteBuffer._StateNormal);
		}
	}

}
