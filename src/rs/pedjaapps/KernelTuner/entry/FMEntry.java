package rs.pedjaapps.KernelTuner.entry;

public class FMEntry
{

   private String name;
   private String date;
   private String size;
   private int folder;
   private String path;

   public FMEntry(String name, String date, String size, int folder, String path){
	   this.name = name;
	   this.date = date;
	   this.size = size;
	   this.folder = folder;
	   this.path = path;
   }
   
   public String getName(){
	   return name;
   }
   
	public String getSize(){
		return size;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getPath(){
		return path;
	}
	
	public int getFolder(){
		return folder;
	}
   
}
