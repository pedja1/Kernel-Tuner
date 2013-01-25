package rs.pedjaapps.KernelTuner;

import java.util.ArrayList;
import java.util.List;

public class SideItems {

	static List<SideMenuEntry> entries = new ArrayList<SideMenuEntry>();
	
	public static void addEntry(int image, String title, Class activity){
		entries.add(new SideMenuEntry(image, title, activity));	
	}
	
	public static List<SideMenuEntry> getEntries(){
		return entries;
	}
	
}
