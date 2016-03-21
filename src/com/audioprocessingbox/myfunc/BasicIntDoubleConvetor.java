package com.audioprocessingbox.myfunc;

/**
 * For Mono, 16bit, only!
 * @author karl
 *
 */
public class BasicIntDoubleConvetor {
	private static final float floatScale = 32768;
	public double[] arrayIntToDouble (int[] data) {
		int numFrames = data.length;
		double[] postdata = new double[numFrames];
		for (int f=0 ; f<numFrames ; f++)
		{
			postdata[f] = (double) data[f] / floatScale;
		}
		return postdata;
	}
	public int[] arrayDoubleToInt (int[] data) {
		int numFrames = data.length;
		int[] postdata = new int[numFrames];
		for (int f=0 ; f<numFrames ; f++)
		{
			postdata[f] = (int) (data[f] * floatScale);
		}
		return postdata;
	}
}
