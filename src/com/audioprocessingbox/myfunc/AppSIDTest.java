package com.audioprocessingbox.myfunc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.audioprocessingbox.MatchResult;
import com.audioprocessingbox.Recognito;

public class AppSIDTest {
	public static void main(String[] arg) {
		Recognito<String> recognito = new Recognito<String>(16000.0f);

		try {
			recognito.createVoicePrint("Elvis", new File("/Users/karl/Work/javawork/combine.wav"));
			for(int i = 0; i < 20; i++) {
				recognito.createVoicePrint("female" + i, new File("/Users/karl/Work/database/age/female/" + i + ".wav"));
				recognito.createVoicePrint("male" + i, new File("/Users/karl/Work/database/age/male/" + i + ".wav"));
				recognito.createVoicePrint("children" + i, new File("/Users/karl/Work/database/age/children/" + i + ".wav"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// handle persistence the way you want, e.g.:
		// myUser.setVocalPrint(print);
		// userDao.saveOrUpdate(myUser);

		// Now check if the King is back
		List<MatchResult<String>> matches;
		MatchResult<String> match;
		long startMili=System.currentTimeMillis();
		int count1 = 0, count2 = 0;
		try {
			for(int i = 0; i < 100; i++) {
				matches = recognito.identify(new File("/Users/karl/Work/database/speaker/certainSpeaker/" + i + ".wav"));
				match = matches.get(0);
				if(match.getKey().equals("Elvis")) {
					count1++;
				}
			}
			
			for(int i = 0; i < 40; i++) {
				matches = recognito.identify(new File("/Users/karl/Work/database/speaker/childrennoise/" + i + ".wav"));
				match = matches.get(0);
				if(!match.getKey().equals("Elvis")) {
					count2++;
				}
				matches = recognito.identify(new File("/Users/karl/Work/database/speaker/female/" + i + ".wav"));
				match = matches.get(0);
				if(!match.getKey().equals("Elvis")) {
					count2++;
				}
				matches = recognito.identify(new File("/Users/karl/Work/database/speaker/male/" + i + ".wav"));
				match = matches.get(0);
				if(!match.getKey().equals("Elvis")) {
					count2++;
				}
			}
			
			/*
			matches = recognito.identify(new File("/Users/karl/Work/database/age/childrennoise/7.wav"));
			MatchResult<String> match = matches.get(0);
			
			if(match.getKey().equals("Elvis")) {
			    System.out.println("Elvis is back !!! " + match.getLikelihoodRatio() + "% positive about it...");
			}
			*/
			System.out.println(count1 * 1.0 / 100 + "," + count2 * 1.0 / 120);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endMili=System.currentTimeMillis();
		System.out.println("总耗时为："+(endMili-startMili) / 220+"毫秒");
	}
}
