package rs.pedjaapps.KernelTuner.entry;

import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.KernelTuner.*;

public class SideItems {

	public static List<SideMenuEntry> entries = new ArrayList<SideMenuEntry>();
	
	public static void addEntry(int image, String title, Class activity){
		entries.add(new SideMenuEntry(image, title, activity));	
	}
	
	public static List<SideMenuEntry> getEntries(){
		return entries;
	}
	
}
