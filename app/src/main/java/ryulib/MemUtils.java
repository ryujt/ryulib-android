package ryulib;

import java.util.ArrayList;

public class MemUtils {

	public static void KeepMemReference(byte[] data) {
		_List.add(data);
	}
	
	public static byte[] GetMem(int size) {
		byte[] data = new byte[size];
		_List.add(data);
		return data;
	}
	
	public static void FreeMem(byte[] data) {
		_List.remove(data);
	}

	private static ArrayList<byte[]> _List = new ArrayList<byte[]>();
	
}
