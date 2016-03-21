package com.audioprocessingbox.myfunc;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.audioprocessingbox.utils.WavFile;
import com.audioprocessingbox.utils.WavFileException;
import com.audioprocessingbox.vad.AutocorrellatedVoiceActivityDetector;

public class AppVAD {
	public static final long sampleRate = 16000;
	
	public static void main(String[] arg) throws UnsupportedAudioFileException, IOException, WavFileException {
		String fileName = "/Users/karl/Work/database/forsimpletest/true";
		
		WavFile wavFile = WavFile.openWavFile(new File(fileName + ".wav"));
		int nframes = (int) wavFile.getNumFrames();
		double[] buffer = new double[nframes];
		wavFile.readFrames(buffer, nframes);
		double[] postbuffer = new AutocorrellatedVoiceActivityDetector().removeSilence(buffer, sampleRate);
		WavFile.newWavFile(new File(fileName + "rmsil.wav"), 1, nframes, 16, sampleRate).writeFrames(postbuffer, postbuffer.length);
		System.out.println(Arrays.toString(postbuffer));
	}
}
