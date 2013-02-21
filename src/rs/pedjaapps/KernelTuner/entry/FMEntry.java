/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
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
