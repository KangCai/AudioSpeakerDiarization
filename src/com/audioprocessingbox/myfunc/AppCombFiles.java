package com.audioprocessingbox.myfunc;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.audioprocessingbox.utils.WavFile;
import com.audioprocessingbox.utils.WavFileException;


public class AppCombFiles {
	private static final long sampleRate = 16000;


	public static void main(String[] arg) throws UnsupportedAudioFileException {
		try {
			int[] postbuffer = new int[0], newbuffer = new int[0], buffer;	
			WavFile wavFile;
			for(int i = 103; i < 111; i++) {
				wavFile = WavFile.openWavFile(new File("/Users/karl/Work/database/speaker/certainSpeaker/" + i + ".wav"));
				if(sampleRate != wavFile.getSampleRate()) {
					throw new IllegalArgumentException("The sample rate for this file is different than Recognito's " +
							"defined sample rate : [" + wavFile.getSampleRate() + "]");
				}
				//Read wav data into int array
				int nframes = (int) wavFile.getNumFrames();
				buffer = new int[nframes];
				wavFile.readFrames(buffer, nframes);
				wavFile.close();
				//Combine two int array
				newbuffer = new int[postbuffer.length + buffer.length];
				System.arraycopy(postbuffer, 0, newbuffer, 0, postbuffer.length);
				System.arraycopy(buffer, 0, newbuffer, postbuffer.length, buffer.length);
				postbuffer = new int[postbuffer.length + buffer.length];
				postbuffer = newbuffer;
			}
			WavFile saveFile = WavFile.newWavFile(new File("/Users/karl/Work/javawork/combine.wav"), 1, postbuffer.length, 16, sampleRate);
			saveFile.writeFrames(postbuffer, postbuffer.length);
			saveFile.close();
			System.out.println(postbuffer.length / sampleRate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WavFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
