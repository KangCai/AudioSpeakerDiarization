package com.audioprocessingbox.myfunc;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.audioprocessingbox.enhancements.Normalizer;
import com.audioprocessingbox.features.LpcFeaturesExtractor;
import com.audioprocessingbox.utils.FileHelper;
import com.audioprocessingbox.vad.AutocorrellatedVoiceActivityDetector;


public class BasicFeatureExtraction {
	private final float sampleRate = 16000f;
	/**
	 * feature feature extraction
	 */
	public double[] extractFeatures(double[] voiceSample) {
		// Preprocess
        AutocorrellatedVoiceActivityDetector voiceDetector = new AutocorrellatedVoiceActivityDetector();
        Normalizer normalizer = new Normalizer();
        voiceDetector.removeSilence(voiceSample, sampleRate);
        normalizer.normalize(voiceSample, sampleRate);
        
        double[] lpcFeatures = new LpcFeaturesExtractor(sampleRate, 20).extractFeatures(voiceSample);

        return lpcFeatures;
    }
	
	public double[] extractFeatures(File voiceSampleFile) 
		throws UnsupportedAudioFileException, IOException {
	        
	        double[] audioSample = convertFileToDoubleArray(voiceSampleFile);

	        return extractFeatures(audioSample);
	}
	
	private double[] convertFileToDoubleArray(File voiceSampleFile) 
            throws UnsupportedAudioFileException, IOException {
        
        AudioInputStream sample = AudioSystem.getAudioInputStream(voiceSampleFile);
        AudioFormat format = sample.getFormat();
        float diff = Math.abs(format.getSampleRate() - sampleRate);
        if(diff > 5 * Math.ulp(0.0f)) {
            throw new IllegalArgumentException("The sample rate for this file is different than Recognito's " +
            		"defined sample rate : [" + format.getSampleRate() + "]");
        }
        return FileHelper.readAudioInputStream(sample);
    }
}
