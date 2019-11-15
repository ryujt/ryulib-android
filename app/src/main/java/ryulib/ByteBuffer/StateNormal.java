package ryulib.ByteBuffer;

public class StateNormal extends State {

	public StateNormal(ByteBuffer owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void clear() {
		synchronized (_ByteBuffer._Buffer) {
			_ByteBuffer._Buffer.clear();
		}
		
		if (_ByteBuffer._Capacity > 0) _ByteBuffer.setState(_ByteBuffer._StateEmpty);
	}

	@Override
	public void dataIn(byte[] data) {
		synchronized (_ByteBuffer._Buffer) {
			_ByteBuffer._Buffer.add(data);
		}
	}

	@Override
	public byte[] read() {
		int count;
		byte[] data = null;
		
		synchronized (_ByteBuffer._Buffer) {
			count = _ByteBuffer._Buffer.size(); 
			if (count > 0) {
				data = _ByteBuffer._Buffer.get(0);
				_ByteBuffer._Buffer.remove(0);
			}			
		}
		
		if (((_ByteBuffer._Capacity > 0)) && (count <= 0)) {
			_ByteBuffer.setState(_ByteBuffer._StateEmpty);
		}
		
		return data;
	}

}
