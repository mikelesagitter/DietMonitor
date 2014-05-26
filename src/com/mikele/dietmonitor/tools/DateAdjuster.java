package com.mikele.dietmonitor.tools;

import android.util.Log;

public class DateAdjuster {
	
	public static String adjustStringNumber(String n){
		String nMod = null;
		if (Integer.parseInt(n) < 10) {
			nMod = "0" + String.valueOf(n);
		} else {
			nMod = String.valueOf(n);
		}
		Log.d("String changed to: ", nMod);
		return nMod;
	}
	
}
