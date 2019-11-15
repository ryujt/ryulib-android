package ryulib.socket;

import ryulib.listeners.OnNotifyListener;
import ryulib.ThreadRepeater;

public class RyuSocketClient {

	static {
		System.loadLibrary("RyuSocketClient");	
	}
	
	public static native int GetDataPacketSize(byte[] dataPacket);

	private static native byte[] CreateObject();
	private static native void ReleaseObject(byte[] handle);
	private static native boolean Connect(byte[] handle, String host, int port);
	private static native void Disconnect(byte[] handle);
	private static native boolean IsConnected(byte[] handle);
	private static native boolean Send(byte[] handle, byte[] dataPacket);
	private static native byte[] Read(byte[] handle);
	
	private byte[] _Handle = CreateObject();
	
	private ThreadRepeater _Repeater = new ThreadRepeater();
	
	private boolean _IsDisconnectedEventFired = false;
	
	private OnNotifyListener _OnRepeat = new OnNotifyListener() {
		public void onNotify(Object sender) {
			read_Packet();
		}
	};
	
	public RyuSocketClient() {
		_Repeater.setOnRepeat(_OnRepeat);
	}
	
	protected void finalize() throws Throwable {
		disconnect();
		
		ReleaseObject(_Handle);
		
		super.finalize(); 
	}
	
	private void read_Packet() {
		byte[] dataPacket = null;
		
		if (IsConnected(_Handle) == false) {
			disconnect();
			
			if (_IsDisconnectedEventFired == false) {
				_IsDisconnectedEventFired = true;
				if (_OnDisconnected != null) _OnDisconnected.onNotify(this);
			}
			
			return;
		}
			
		dataPacket = Read(_Handle);
		
		if ((dataPacket != null) && (GetDataPacketSize(dataPacket) > 0) && (_OnReceived != null)) {
			_OnReceived.onReceived(this, dataPacket);
		}			
	}
	
	public synchronized boolean connect(String host, int port) {
		_Host = host;
		_Port = port;
		
		boolean isConnected = Connect(_Handle, host, port);
		if (isConnected) {
			_IsDisconnectedEventFired = false;
			if (_OnConnected != null) _OnConnected.onNotify(this);
			_Repeater.start();
		}
		return isConnected;
	}
	
	public synchronized void disconnect() {
		Disconnect(_Handle);
		_Repeater.stop();
	}
	
	public synchronized boolean send(byte[] dataPacket) {
		return Send(_Handle, dataPacket);
	}
	
	private String _Host;
	
	public String getHost() {
		return _Host;
	}
	
	private int _Port = 0;
	
	public int getPort() {
		return _Port;
	}
	
	public synchronized boolean isConnected() {
		return IsConnected(_Handle);
	}
	
	private OnNotifyListener _OnConnected = null;
	
	public void setOnConnectedListener(OnNotifyListener value) {
		_OnConnected = value;
	}
	
	private OnNotifyListener _OnDisconnected = null;
	
	public void setOnDisconnectedListener(OnNotifyListener value) {
		_OnDisconnected = value;
	}
	
	private OnReceivedListener _OnReceived = null;
	
	public void setOnReceivedListener(OnReceivedListener value) {
		_OnReceived = value;
	}

}
