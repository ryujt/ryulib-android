package ryulib.internet;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ryulib.listeners.OnStringListener;

public class HTTP {
	
	static public int ConnectTimeout = 5 * 1000;
	
	static public String get(String aurl) {
		StringBuilder result = new StringBuilder();

		try {
			URL url = new URL(aurl);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			if (connection != null) {
				connection.setConnectTimeout(ConnectTimeout);
				connection.setUseCaches(false);
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					BufferedReader _Reader = 
						new BufferedReader(new InputStreamReader(connection.getInputStream()));
					while (true) {
						String line = _Reader.readLine();
						if (line == null) break;
						
						result.append(line + '\n');
					}
					_Reader.close();
				} else {
					throw new Exception(String.format("ErrorCode = %d", connection.getResponseCode()));
				}
					
				connection.disconnect();
			}
		} catch (Exception e) {
			Log.e("HTTP.get", e.getMessage());
		}
		
		return result.toString();
	}

	static public void asyncGet(final String url, final OnStringListener resultEvent) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HTTP.get(url);
				if (resultEvent != null) resultEvent.onString(this, result);
			}
		});
		thread.start();
    }

}
