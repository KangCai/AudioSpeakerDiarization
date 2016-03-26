package com.audioprocessingbox.utils;

import java.io.File;
import java.io.IOException;

import com.audioprocessingbox.features.mfcc.FeatureExtract;
import com.audioprocessingbox.features.mfcc.PreProcess;
import com.audioprocessingbox.utils.WavFile;
import com.audioprocessingbox.utils.WavFileException;


public class BasicFeatureExtraction {
	private final float sampleRate = 16000f;
	/**
	 * MFCC feature feature extraction
	 */
	public double[] extractMfcc(double[] voiceSample) {
		// Preprocess
		float[] floatArray = new float[voiceSample.length];
		for (int i = 0 ; i < voiceSample.length; i++) {
		    floatArray[i] = (float) voiceSample[i];
		}
		PreProcess prep = new PreProcess(floatArray, 512, (int)sampleRate);
		FeatureExtract fe = new FeatureExtract(prep.getFramedSignal(), (int)sampleRate, 512);
		//System.out.println(prep.getFramedSignal().length);
		fe.makeMfccFeatureVector();
		double[] mfccFeatures = fe.getFeatureVector().getRowFeature();
        return mfccFeatures;
    }	
	public double[] extractMfcc(File voiceSampleFile) {        
	        double[] audioSample;
			try {
				audioSample = convertFileToDoubleArray(voiceSampleFile);
				return extractMfcc(audioSample);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	}
	
	private double[] convertFileToDoubleArray(File voiceSampleFile) 
            throws IOException {
    	WavFile wavFile;
		try {
			wavFile = WavFile.openWavFile(voiceSampleFile);
			int nframes = (int) wavFile.getNumFrames();
			double[] buffer = new double[nframes];
			wavFile.readFrames(buffer, nframes);
	    	return buffer;
		} catch (WavFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}
