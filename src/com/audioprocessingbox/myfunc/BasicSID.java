package com.audioprocessingbox.myfunc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.audioprocessingbox.MatchResult;
import com.audioprocessingbox.Recognito;
import com.audioprocessingbox.VoicePrint;

public class BasicSID {
	private final float sampleRate = 16000f;
	private final double threshold = 50;
	private String universalModelFile;
	private String IdsMapFile;
	private Recognito<String> recognito;
	
	public void init(String idsmapFile, String umfile) throws IOException {
		IdsMapFile = idsmapFile;
		universalModelFile = umfile;
		File file=new File(IdsMapFile);
		if(!file.exists()) {
			recognito = new Recognito<String> (sampleRate);
			file.createNewFile();
		}
		else 
			recognito = new Recognito<String> (sampleRate, readIdsmap());
		//recognito.setUniversalModel(null);
		//Init UniversalModel.
		//Note: cannot update the model, if you want to update it, see the source "Recognito.java"
		VoicePrint universalModel = readUMfromFile(universalModelFile);
		recognito.setUniversalModel(universalModel);		
	}	
	
	public void createNewId(String id, String newidfile) throws IOException {
		//Add to the tail of the file
		FileWriter writer = new FileWriter(IdsMapFile);
		recognito.createVoicePrint(id, new File(newidfile));
		Iterator<Map.Entry<String, VoicePrint>> entries = recognito.getStore().entrySet().iterator();  		  
		while (entries.hasNext()) {  		  
		    Map.Entry<String, VoicePrint> entry = entries.next();  
		    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  	
		    writer.write(idAnddoubleArrayToString(entry.getKey(), entry.getValue().getFeatures()) + '\n');
		}  
		writer.close();
	}
	
	public void createNewId(String id, int[] rawwav) throws IOException {
		//Add to the tail of the file
		File file=new File(IdsMapFile);
		if(!file.exists())
			file.createNewFile();
		//FileWriter writer = new FileWriter(IdsMapFile, true);
		FileWriter writer = new FileWriter(IdsMapFile);
		recognito.createVoicePrint(id, new BasicIntDoubleConvetor().arrayIntToDouble(rawwav));
		Iterator<Map.Entry<String, VoicePrint>> entries = recognito.getStore().entrySet().iterator();  		  
		while (entries.hasNext()) {  		  
		    Map.Entry<String, VoicePrint> entry = entries.next();  
		    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  	
		    writer.write(idAnddoubleArrayToString(entry.getKey(), entry.getValue().getFeatures()) + '\n');
		}  
		writer.close();
	}
	
	public String getInfo() {
		String str = "";
		Iterator<Map.Entry<String, VoicePrint>> entries = recognito.getStore().entrySet().iterator();  		  
		while (entries.hasNext()) { 
			Map.Entry<String, VoicePrint> entry = entries.next(); 
		    str += "Key = " + entry.getKey() + ", Value = " + entry.getValue().getFeatures()[1] + '\n';  	
		}  
		str += "Model Value = " + recognito.getUniversalModel().getFeatures()[1];
		return str;
	}
		
	public String recognizeId(String testfile) throws IOException {	
		List<MatchResult<String>> matches;
		MatchResult<String> match;	
		matches = recognito.identify(new File(testfile));
		Map<String, VoicePrint> idsmap = recognito.getStore();
		System.out.println(idsmap.size());
		if(idsmap.isEmpty())
			return "No ID in Database! Please create new ID.";
		int i = 0;
		match = matches.get(0);
		
		String display= "";	
		for(i = 0; i < matches.size(); i++) {
			match = matches.get(i);
			if(match.getLikelihoodRatio() > threshold) 
				display += match.getKey() + ": the possibility is " + match.getLikelihoodRatio() + "%\n";
			else
				break; 
			
		}
		if(i == 0)
			return "Not found";
		
		return display;
	}
	
	public String recognizeId(int[] rawwav) {
		List<MatchResult<String>> matches;
		MatchResult<String> match;	
		matches = recognito.identify(new BasicIntDoubleConvetor().arrayIntToDouble(rawwav));
		Map<String, VoicePrint> idsmap = recognito.getStore();
		System.out.println(idsmap.size());
		if(idsmap.isEmpty())
			return "No ID in Database! Please create new ID.";
		int i = 0;
		String display= "";
		for(i = 0; i < matches.size(); i++) {
			match = matches.get(i);
			if(match.getLikelihoodRatio() > threshold)
				display += match.getKey() + ": the possibility is " + match.getLikelihoodRatio() + "%\n";
			else
				break;
		}
		if(i == 0)
			return "Not found";
		return display;
	}
	
	public boolean resetSystem() throws IOException {
		File file = new File(IdsMapFile);
		if(file.exists()) {
			 file.delete(); 
			 init(IdsMapFile, universalModelFile);
			 return true;
		}
		return false;
	}
	
	//Do not use this function!
	public void generateUniversalModel(String modelFile) throws UnsupportedAudioFileException, IOException {
		Recognito<String> recognito = new Recognito<String>(16000.0f);
		for(int i = 0; i < 20; i++) {
			recognito.createVoicePrint("_female" + i, new File("/Users/karl/Work/database/age/female/" + i + ".wav"));
			recognito.createVoicePrint("_male" + i, new File("/Users/karl/Work/database/age/male/" + i + ".wav"));
			recognito.createVoicePrint("_children" + i, new File("/Users/karl/Work/database/age/children/" + i + ".wav"));
		}
		double[] umarray = recognito.getUniversalModel().getFeatures();
		FileWriter fwriter = new FileWriter(new File(modelFile));
		fwriter.write(doubleArrayToString(umarray));
		fwriter.close();
	}
	
	private Map<String, VoicePrint> readIdsmap() throws IOException {
		Map<String, VoicePrint> idsmap = new HashMap<String, VoicePrint>();
		FileInputStream f = new FileInputStream(IdsMapFile);  
		String key;
		double[] value;
		BufferedReader dr=new BufferedReader(new InputStreamReader(f));
		String line =  dr.readLine();
		while(line!= null && !line.equals("")){ 
			System.out.println(line);
			Map.Entry<String, double[]> pair = stringToIdAndDoubleArray(line);
			key = pair.getKey();
			//System.out.println(key);
			value = pair.getValue();
			VoicePrint vp = new VoicePrint(value);
			//System.out.println(vp.getFeatures().length);
			if(!idsmap.containsKey(key))
				idsmap.put(key, vp);
			line = dr.readLine();
		}
		dr.close();
		return idsmap;
	}

	private VoicePrint readUMfromFile(String universalModelFile) throws IOException {
		FileInputStream f = new FileInputStream(universalModelFile); 
		BufferedReader dr=new BufferedReader(new InputStreamReader(f));
		String line =  dr.readLine();
		dr.close();
		double[] umarray = stringToDoubleArray(line.toString());
		VoicePrint universalModel = new VoicePrint(umarray);
		return universalModel;
	}
	
	private String doubleArrayToString(double[] array) {
		String str = "";
		for(int i = 0; i < array.length; i++) {
			str += array[i] + " ";
		}
		return str.substring(0, str.length() - 1);
	}
	private String idAnddoubleArrayToString(String id, double[] array) {
		String str = id;
		str += " " + doubleArrayToString(array);
		return str;
	}
	private double[] stringToDoubleArray(String str) {
		String[] ls;
        ls = str.split(" ");
        //System.out.println(ls.length);
        double[] array = new double[ls.length];
        for(int i = 0; i < ls.length; i++)
        {
        	array[i] = Double.parseDouble(ls[i]);
        }
        return array;
	}
	
	private Map.Entry<String, double[]> stringToIdAndDoubleArray(String str) {
		String id = "";
		int i = 0;
		for(i = 0; i < str.length(); i++) {
			if(str.charAt(i) != ' ') {
				id += str.charAt(i);
			}
			else
				break;
		}
		String featureStr = str.substring(i + 1);
		Map.Entry<String, double[]> pair = new AbstractMap.SimpleEntry(id, stringToDoubleArray(featureStr));
		return pair;
	}
	
}
