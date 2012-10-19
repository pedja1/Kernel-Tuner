package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.os.SystemClock;

public class CPUInfo {

	public static String cpu0online = "/sys/devices/system/cpu/cpu0/online"; 
	public static String cpu1online = "/sys/devices/system/cpu/cpu1/online"; 
	public static String cpu2online = "/sys/devices/system/cpu/cpu2/online"; 
	public static String cpu3online = "/sys/devices/system/cpu/cpu3/online"; 


	public static String CPU0_FREQS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
	public static String CPU1_FREQS = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_available_frequencies";
	public static String CPU2_FREQS = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_available_frequencies";
	public static String CPU3_FREQS = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_available_frequencies";

	public static String CPU0_CURR_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
	public static String CPU1_CURR_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq";
	public static String CPU2_CURR_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_cur_freq";
	public static String CPU3_CURR_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_cur_freq";

	public static String CPU0_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
	public static String CPU1_MAX_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq";
	public static String CPU2_MAX_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq";
	public static String CPU3_MAX_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq";

	public static String CPU0_MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
	public static String CPU1_MIN_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq";
	public static String CPU2_MIN_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq";
	public static String CPU3_MIN_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq";

	public static String CPU0_CURR_GOV = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	public static String CPU1_CURR_GOV = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor";
	public static String CPU2_CURR_GOV = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_governor";
	public static String CPU3_CURR_GOV = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_governor";

	public static String CPU0_GOVS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
	public static String CPU1_GOVS = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_available_governors";
	public static String CPU2_GOVS = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_available_governors";
	public static String CPU3_GOVS = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_available_governors";
	public static String TIMES_IN_STATE_CPU0 = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
	public static String TIMES_IN_STATE_CPU1 = "/sys/devices/system/cpu/cpu1/cpufreq/stats/time_in_state";
	public static String TIMES_IN_STATE_CPU2 = "/sys/devices/system/cpu/cpu2/cpufreq/stats/time_in_state";
	public static String TIMES_IN_STATE_CPU3 = "/sys/devices/system/cpu/cpu3/cpufreq/stats/time_in_state";
	
	public static String VOLTAGE_PATH = "sd/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels";
	public static String VOLTAGE_PATH_TEGRA_3 = "/sdcard/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table";
	
	
	public static boolean cpu0Online(){
		boolean i = false;
		if(new File(cpu0online).exists()){
			i=true;
		}
		return i;
		
	}
	
	public static boolean cpu1Online(){
		boolean i = false;
		if(new File(cpu1online).exists()){
			i=true;
		}
		return i;
		
	}
	
	public static boolean cpu2Online(){
		boolean i = false;
		if(new File(cpu2online).exists()){
			i=true;
		}
		return i;
		
	}
	
	public static boolean cpu3Online(){
		boolean i = false;
		if(new File(cpu3online).exists()){
			i=true;
		}
		return i;
		
	}
	
	public static boolean voltageExists(){
		boolean i = false;
		if(new File(VOLTAGE_PATH).exists()){
			i=true;
		}
		else if(new File(VOLTAGE_PATH_TEGRA_3).exists()){
			i=true;
		}
		return i;
		
	}
	
	public static boolean TISExists(){
		boolean i = false;
		if(new File(TIMES_IN_STATE_CPU0).exists()){
			i=true;
		}
		return i;
		
	}
	
	public static List<String> frequencies() {
		List<String> frequencies = new ArrayList<String>();
		
		
		try {
			
			File myFile = new File(CPU0_FREQS);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				
				//frequencies.add(aDataRow.trim());
				aBuffer += aDataRow + "\n";
			}
		frequencies = Arrays.asList(aBuffer.split("\\s"));
			
			myReader.close();
	   			
		} catch (Exception e) {
			try{
				
	 			FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU0);
	 			
	 			DataInputStream in = new DataInputStream(fstream);
	 			BufferedReader br = new BufferedReader(new InputStreamReader(in));
	 			String strLine;
	 			
	 			
	 			while ((strLine = br.readLine()) != null)   {
	 				
	 				String[] delims = strLine.split(" ");
	 				String freq = delims[0];
	 				frequencies.add(freq);

	 			}
	 			
	 			if(frequencies.get(0).length()>frequencies.get(frequencies.size()-1).length()){
	 				Collections.reverse(frequencies);
	 			}
	 			
	 			
	 			in.close();
			}
			catch(Exception ee){
			
			}
		}
		return frequencies;
		
	}
	
	public static List<String> governors() {
		List<String> governors = new ArrayList<String>();
		
		
		try {
			
			File myFile = new File(CPU0_GOVS);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}
		governors = Arrays.asList(aBuffer.split("\\s"));
			
			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return governors;
		
	}
	
	public static String cpu0MinFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU0_MIN_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu0MaxFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU0_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu1MinFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU1_MIN_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu1MaxFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU1_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu2MinFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU2_MIN_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu2MaxFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU2_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu3MinFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU3_MIN_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu3MaxFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU3_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu0CurFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU0_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu1CurFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU1_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu2CurFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU2_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu3CurFreq(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU3_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu0CurGov(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU0_CURR_GOV);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu1CurGov(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU1_CURR_GOV);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu2CurGov(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU2_CURR_GOV);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return aBuffer.trim();
		
	}
	
	public static String cpu3CurGov(){
		String aBuffer = "offline";
		try {
			
			File myFile = new File(CPU3_CURR_GOV);
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer="";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			myReader.close();
	   			
		} catch (Exception e) {
			
		}
		return aBuffer.trim();
		
	}
	
	
	public static List<String> tisTime() {
		List<String> tisTime = new ArrayList<String>();
		List<Integer> cpu0Times = new ArrayList<Integer>();
		List<Integer> cpu1Times = new ArrayList<Integer>();
		List<Integer> cpu2Times = new ArrayList<Integer>();
		List<Integer> cpu3Times = new ArrayList<Integer>();
		
		try{
			  
			  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU0);
			
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
	
			  while ((strLine = br.readLine()) != null)   {	
			      String[] delims = strLine.split(" ");
			      cpu0Times.add(Integer.parseInt(delims[1]));
			  }
			
			  in.close();
			    }catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
			  }
		if(cpu1Online()==true){
			try{
				  
				  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU1);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      cpu1Times.add(Integer.parseInt(delims[1]));
				  }
				
				  in.close();
				    }catch (Exception e){
				    	for(int i = 0; i<cpu0Times.size(); i++){
							cpu1Times.add(0);
						}
				  }
		}
		else{
			for(int i = 0; i<cpu0Times.size(); i++){
				cpu1Times.add(0);
			}
		}
		if(cpu2Online()==true){
			try{
				  
				  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU2);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      cpu2Times.add(Integer.parseInt(delims[1]));
				  }
				
				  in.close();
				    }catch (Exception e){
				    	for(int i = 0; i<cpu0Times.size(); i++){
							cpu2Times.add(0);
						}
				  }
		}
		else{
			for(int i = 0; i<cpu0Times.size(); i++){
				cpu2Times.add(0);
			}
		}
		if(cpu3Online()==true){
			try{
				  
				  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU3);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      cpu3Times.add(Integer.parseInt(delims[1]));
				  }
				
				  in.close();
				    }catch (Exception e){
				    	for(int i = 0; i<cpu0Times.size(); i++){
							cpu3Times.add(0);
						}
				  }
		}
		else{
			for(int i = 0; i<cpu0Times.size(); i++){
				cpu3Times.add(0);
			}
		}
		
		for(int i =0; i<cpu0Times.size(); i++){
			
			String min = String.valueOf(((((cpu0Times.get(i)+cpu1Times.get(i)+cpu2Times.get(i)+cpu3Times.get(i)) / 100) / 60) % 60));
		       String sec = String.valueOf((((cpu0Times.get(i)+cpu1Times.get(i)+cpu2Times.get(i)+cpu3Times.get(i)) / 100) % 60));
		       String sat = String.valueOf((((cpu0Times.get(i)+cpu1Times.get(i)+cpu2Times.get(i)+cpu3Times.get(i))/ 100) / 3600));
		       String time = sat+"h:"+min+"m:"+sec+"s";
			tisTime.add(time);
		}
		return tisTime;
		
	}
	
	public static List<String> tisPercent() {
		List<String> tisPercent = new ArrayList<String>();
		List<Integer> cpu0Times = new ArrayList<Integer>();
		List<Integer> cpu1Times = new ArrayList<Integer>();
		List<Integer> cpu2Times = new ArrayList<Integer>();
		List<Integer> cpu3Times = new ArrayList<Integer>();
		List<Integer> cpuTimes = new ArrayList<Integer>();
		
		try{
			  
			  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU0);
			
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
	
			  while ((strLine = br.readLine()) != null)   {	
			      String[] delims = strLine.split(" ");
			      cpu0Times.add(Integer.parseInt(delims[1]));
			  }
			
			  in.close();
			    }catch (Exception e){
			  
			  }
		if(cpu1Online()==true){
			try{
				  
				  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU1);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      cpu1Times.add(Integer.parseInt(delims[1]));
				  }
				
				  in.close();
				    }catch (Exception e){
				    	for(int i = 0; i<cpu0Times.size(); i++){
							cpu1Times.add(0);
						}
				  }
		}
		else{
			for(int i = 0; i<cpu0Times.size(); i++){
				cpu1Times.add(0);
			}
		}
		if(cpu2Online()==true){
			try{
				  
				  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU2);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      cpu2Times.add(Integer.parseInt(delims[1]));
				  }
				
				  in.close();
				    }catch (Exception e){
				    	for(int i = 0; i<cpu0Times.size(); i++){
							cpu2Times.add(0);
						}
				  }
		}
		else{
			for(int i = 0; i<cpu0Times.size(); i++){
				cpu2Times.add(0);
			}
		}
		if(cpu3Online()==true){
			try{
				  
				  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU3);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      cpu3Times.add(Integer.parseInt(delims[1]));
				  }
				
				  in.close();
				    }catch (Exception e){
				    	for(int i = 0; i<cpu0Times.size(); i++){
							cpu3Times.add(0);
						}
				  }
		}
		else{
			for(int i = 0; i<cpu0Times.size(); i++){
				cpu3Times.add(0);
			}
		}
		
		for(int i =0; i<cpu0Times.size(); i++){
				cpuTimes.add((cpu0Times.get(i)+cpu1Times.get(i)+cpu2Times.get(i)+cpu3Times.get(i)));

			
			
		}
		
		for(int i =0; i< cpuTimes.size(); i++){
			tisPercent.add(String.valueOf((cpuTimes.get(i)*100/totalTime())));
		}
		
		return tisPercent;
		
	}
	
	public static List<Integer> voltages() {
		List<Integer> voltages = new ArrayList<Integer>();
		
		try {
		
			FileInputStream fstream = new FileInputStream(VOLTAGE_PATH);
			
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				
				voltages.add(Integer.parseInt(strLine.substring(9,
						strLine.length() - 0).trim()));
			}

			in.close();
		} catch (Exception e) {
			try{
				  
				  FileInputStream fstream = new FileInputStream(VOLTAGE_PATH_TEGRA_3);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      voltages.add(Integer.parseInt(delims[1]));
				  }
				
				  in.close();
				    }catch (Exception ex){
				  
				  }
		}
		System.out.println(voltages);
		return voltages;
		 
	}
	
	public static List<String> voltageFreqs() {
		List<String> voltageFreqs = new ArrayList<String>();
		
		try {
		
			FileInputStream fstream = new FileInputStream(VOLTAGE_PATH);
			
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				
				voltageFreqs.add(strLine.substring(0,
						strLine.length() - 10).trim());
			}

			in.close();
		} catch (Exception e) {
		
			try{
				  
				  FileInputStream fstream = new FileInputStream(VOLTAGE_PATH_TEGRA_3);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      voltageFreqs.add(delims[0].substring(0, delims[0].length()-4));
				  }
				
				  in.close();
				    }catch (Exception ex){
				  
				  }
			
		}
		System.out.println(voltageFreqs);
		return voltageFreqs;
	
	}
	
	
	public static List<Integer> allVoltages() {
		List<Integer> allVoltages = new ArrayList<Integer>();
		
		for(int i = 700000; i<1412500; i+=12500 ){
			allVoltages.add(i);
		}
		return allVoltages;
		
	}
	
	public static List<Integer> allVoltagesTegra3() {
		List<Integer> allVoltages = new ArrayList<Integer>();
		
		for(int i = 700000; i<1412500; i+=12500 ){
			allVoltages.add(i/1000);
		}
		return allVoltages;
		
	}
	
	public static int totalTime(){
		List<Integer> cpu0Times = new ArrayList<Integer>();
		List<Integer> cpu1Times = new ArrayList<Integer>();
		List<Integer> cpu2Times = new ArrayList<Integer>();
		List<Integer> cpu3Times = new ArrayList<Integer>();
		List<Integer> cpuTimes = new ArrayList<Integer>();
		
		try{
			  
			  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU0);
			
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
	
			  while ((strLine = br.readLine()) != null)   {	
			      String[] delims = strLine.split(" ");
			      cpu0Times.add(Integer.parseInt(delims[1]));
			  }
			
			  in.close();
			    }catch (Exception e){
			  
			  }
		if(cpu1Online()==true){
			try{
				  
				  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU1);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      cpu1Times.add(Integer.parseInt(delims[1]));
				  }
				
				  in.close();
				    }catch (Exception e){
				    	for(int i = 0; i<cpu0Times.size(); i++){
							cpu1Times.add(0);
						}
				  }
		}
		else{
			for(int i = 0; i<cpu0Times.size(); i++){
				cpu1Times.add(0);
			}
		}
		if(cpu2Online()==true){
			try{
				  
				  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU2);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      cpu2Times.add(Integer.parseInt(delims[1]));
				  }
				
				  in.close();
				    }catch (Exception e){
				    	for(int i = 0; i<cpu0Times.size(); i++){
							cpu2Times.add(0);
						}
				  }
		}
		else{
			for(int i = 0; i<cpu0Times.size(); i++){
				cpu2Times.add(0);
			}
		}
		if(cpu3Online()==true){
			try{
				  
				  FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU3);
				
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
		
				  while ((strLine = br.readLine()) != null)   {	
				      String[] delims = strLine.split(" ");
				      cpu3Times.add(Integer.parseInt(delims[1]));
				  }
				
				  in.close();
				    }catch (Exception e){
				    	for(int i = 0; i<cpu0Times.size(); i++){
							cpu3Times.add(0);
						}
				  }
		}
		else{
			for(int i = 0; i<cpu0Times.size(); i++){
				cpu3Times.add(0);
			}
		}
		
		for(int i =0; i<cpu0Times.size(); i++){
				cpuTimes.add((cpu0Times.get(i)+cpu1Times.get(i)+cpu2Times.get(i)+cpu3Times.get(i)));
	
		}
		int a=0;
		for(int i =0; i<cpuTimes.size(); i++){
			a=a+cpuTimes.get(i);
		}
		return a;
	} 
	
	public static String uptime(){
		String uptime;
		 
		  int time =(int) SystemClock.elapsedRealtime();
		 
		
		  String s = String.valueOf((int)((time / 1000) % 60));
		    String m = String.valueOf((int)((time / (1000*60)) % 60));
		     String h = String.valueOf((int)((time / (1000*3600)) % 24));
		     String d = String.valueOf((int)(time / (1000*60*60*24)));
		     StringBuilder builder = new StringBuilder();
		     if(!d.equals("0")){
		    	 builder.append(d+"d:");
		    	
		     }
		     if(!h.equals("0")){
		    	 builder.append(h+"h:");
		    	 
		     }
		     if(!m.equals("0")){
		    	 builder.append(m+"m:");
		    	 
		     }
		     
		    	 builder.append(s+"s");
		    	 
		     
		    uptime= builder.toString();
		
		return uptime;
		
		
	}
	public static String deepSleep(){
		String deepSleep;
		 
		  int time =(int) (SystemClock.elapsedRealtime()-SystemClock.uptimeMillis());
		 
		  String s = String.valueOf((int)((time / 1000) % 60));
	    String m = String.valueOf((int)((time / (1000*60)) % 60));
	     String h = String.valueOf((int)((time / (1000*3600)) % 24));
	     String d = String.valueOf((int)(time / (1000*60*60*24)));
	     StringBuilder builder = new StringBuilder();
	     if(!d.equals("0")){
	    	 builder.append(d+"d:");
	    	
	     }
	     if(!h.equals("0")){
	    	 builder.append(h+"h:");
	    	 
	     }
	     if(!m.equals("0")){
	    	 builder.append(m+"m:");
	    	 
	     }
	     
	    	 builder.append(s+"s");
	    	 
	     
	     deepSleep= builder.toString();
		
		return deepSleep;
	}
	
	public static String cpuTemp(){
	String cpuTemp = "";
		try {

			File myFile = new File(
					"/sys/class/thermal/thermal_zone1/temp");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
					fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			cpuTemp = aBuffer.trim();

			myReader.close();

		} catch (Exception e2) {
		}
		
		return cpuTemp;
	}
	
	public static String cpuInfo(){
		String cpuInfo = "";
		try {

			File myFile = new File("/proc/cpuinfo");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
					fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			cpuInfo = aBuffer.trim();

			myReader.close();

		} catch (Exception e2) {
		}
		return cpuInfo;
	}
	
	public static List<String> availableGovs(){
		File govs = new File("/sys/devices/system/cpu/cpufreq/");
	     List<String> availableGovs = new ArrayList<String>();
	  
	   
	      if(govs.exists()){
	     File[] files = govs.listFiles();
	     
	     for (File file : files){
	      availableGovs.add(file.getName());
	      

	     }
	      }
	    
	      availableGovs.removeAll(Arrays.asList("vdd_table"));
		return availableGovs;
	   
	}
	
	public static List<String> govSettings() {

	     List<String> govSettings = new ArrayList<String>();
	     
	     for(String s : availableGovs()){
	    	 File gov = new File("/sys/devices/system/cpu/cpufreq/"+s+"/");
		    
		      if(gov.exists()){
		     File[] files = gov.listFiles();
		     
		     for (File file : files){
		     
		      govSettings.add(file.getName());
		    
		     }
		      }}
		return govSettings;}

}
