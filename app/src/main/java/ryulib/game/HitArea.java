package ryulib.game;

import java.util.ArrayList;

import ryulib.graphic.Boundary;

public class HitArea {
	
	private ArrayList<Boundary> list = new ArrayList<Boundary>();
	
	public boolean checkCollision(HitArea hitArea) {
		// TODO 
		int sizeOfSrc = list.size();
		for (int i=0; i<sizeOfSrc; i++) {
			Boundary src = list.get(i);

			int sizeOfDst = hitArea.list.size();
			for (int j=0; j<sizeOfDst; j++) {
				Boundary dst = hitArea.list.get(j);
				
				if (src.CheckCollision(dst)) return true;
			}
		}
		
		return false;
	}
	
	public boolean getIsMyArea(int x, int y) {
		int size = list.size();
		for (int i=0; i<size; i++) {
			Boundary block = list.get(i);

			if (block.isMyArea(x, y)) return true;
		}
		return false;
	}

	public void clear() {
		list.clear();
	}
	
	public void add(Boundary boundary) {
		list.add(boundary);
	}
	
	public void remove(Boundary boundary) {
		list.remove(boundary);
	}
	
	public ArrayList<Boundary> getArrayList() {
		return list;
	}
	
}
