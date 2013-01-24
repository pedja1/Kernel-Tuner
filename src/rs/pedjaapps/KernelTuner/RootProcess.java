package rs.pedjaapps.KernelTuner;

import java.io.IOException;

import android.util.Log;

public class RootProcess {
	static Process process;

	public RootProcess(){
		
	}
	
	public RootProcess(String command){
		try {
			RootProcess.process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("Kernel Tuner-Root Process Constructor", e.getMessage());
		}
	}

	public static Process getProcess() {
		return process;
	}
	
}
/*class Child extends RootProcess{
	  public Child()throws IOException{
	   throw new IOException();
	  }
	}*/