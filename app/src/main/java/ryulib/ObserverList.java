package ryulib;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ryulib.listeners.OnMessageListener;

public class ObserverList {

	public ObserverList() {
		messageControl.setOnMessageListener(new OnMessageListener() {
			@Override
			public void onMessage(Message message) {
//				Log.i("ObserverList", ((JSONObject) message.obj).toString());

				for (int i=0; i< list.size(); i++) {
					do_Notify(list.get(i), (JSONObject) message.obj);
				}
			}
		});
	}

	public void clear() {
		list.clear();
	}
	
	public void add(Object object) {
		list.remove(object);
		list.add(object);
	}
	
	public void remove(Object object) {
		list.remove(object);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void do_Notify(Object object, JSONObject packet) {
//		Log.i("ObserverList", object.toString());

		Class _Class = object.getClass();
		Class[] _ParameterType = new Class[] { JSONObject.class };
		
		try {
			Method _Method = _Class.getMethod("rp_" + packet.getString("Code"), _ParameterType);
			_Method.invoke(object, new Object[] { packet });
		} catch (Exception e) {
//			Log.e("ObserverList", e.getMessage());
		}
	}

	public void broadcast(JSONObject packet) {
		messageControl.send(packet);
	}

	public void broadcast(String packet) {
		try {
			JSONObject json = new JSONObject(packet);
			broadcast(json);
		} catch (JSONException e) {
			Log.e("broadcast", e.getMessage());
		}
	}
	
	private ArrayList<Object> list = new ArrayList<Object>();
	private MessageControl messageControl = new MessageControl();
}
