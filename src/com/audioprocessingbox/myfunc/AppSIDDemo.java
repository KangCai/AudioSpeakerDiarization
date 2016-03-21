package com.audioprocessingbox.myfunc;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

public class AppSIDDemo {
	public static void main(String[] arg) throws UnsupportedAudioFileException, IOException {
		BasicSID sid = new BasicSID();
		//sid.generateUniversalModel();
		//开机有且调用一次，作用是读入um模型和上一次保存的ids
		//sid.init("aaa");
		sid.createNewId("Eval", "/Users/karl/Work/database/storage/combine.wav");
		System.out.println(sid.recognizeId("/Users/karl/Work/database/speaker/certainSpeaker/0.wav"));
		sid.resetSystem();
		sid.createNewId("JACK", "/Users/karl/Work/database/age/female/31.wav");
		System.out.println(sid.recognizeId("/Users/karl/Work/database/speaker/certainSpeaker/0.wav"));
		sid.createNewId("Eval", "/Users/karl/Work/database/storage/combine.wav");
		System.out.println(sid.recognizeId("/Users/karl/Work/database/speaker/certainSpeaker/0.wav"));
	}
}
