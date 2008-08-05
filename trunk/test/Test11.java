package ccl.util;
import java.awt.*;
import java.util.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.net.URL;
import java.io.*;
import java.net.*;
import java.lang.Math;
import ccl.awt.*;
public class Test11 {
	private static Random _rnd = new Random();
	public static int atoi(String s) {
		panicIf(s == null);
		if (s.equals("")) {
			return 0;		}
		return(Integer.parseInt(s.trim()));	}
	public static String itoa(int i) {
		return(String.valueOf(i));	}
	public static long max(long a_, long b_) {
		if (a_ > b_) {
			return a_;		}
		return(b_);	}
	public static int max(int a_, int b_) {
		if (a_ > b_) {
			return a_;		}
		return(b_);	}
	public static int min(int a_, int b_) {
		if (a_ < b_) {
			return a_;		}
		return(b_);	}
	public static void print(char c) {
		System.out.print(c);
		System.out.flush();	}
	public static void print(String s) {
		System.out.print(s);
		System.out.flush();	}
	public static void println(String s) {
		System.out.println(s);	}
	public static void println(Exception e) {
		System.err.println("Exception: " + e.getMessage());
		Thread.dumpStack();
		println(Thread.currentThread().toString());	}
	public static void panicIf(boolean bPanic) {
		if (bPanic) {
			throw(new ApplicationException());		}	}
	public static void panicIf(boolean bPanic, String sMessage) {
		if (bPanic) {
			throw(new ApplicationException(sMessage));		}	}
	private static boolean _bDebug = false;
	public static void setDebug(boolean bDebug) {
		_bDebug = bDebug;	}
	public static void debug(Object obj) {
		if (_bDebug) {
			println(obj.toString());		}	}
	public static void debug(int i) {
		if (_bDebug) {
			println("Int: " + i);		}	}
	public static void showLiveSignal() {
		showLiveSignal('.');	}
	public static void showLiveSignal(char c) {
		print(c);	}
	public static boolean rnd() {
		return(rnd(1) == 0);	}
	public static int rnd(int bis) {
		return rnd(0, bis);	}
	public static int rnd(int von, int bis) {
		panicIf(bis <= von);
		float fR = _rnd.nextFloat();
		int r = (int)(fR*(bis-von+1)+von);
		return( r );	}
	public static float rnd(float f) {
		float fR = (float)_rnd.nextFloat();
		return( f*fR );	}
	public static double rnd(double df) {
		double dR = _rnd.nextDouble();
		return( df * dR );	}
	private static final char[] _acUmlaut = { 'ä', 'Ä', 'ö', 'Ö',															 'ü', 'Ü', 'ß', 'é' };
	public static boolean isAlpha(char c_) {
		if (('A' <= c_ && c_ <= 'Z') ||			 ('a' <= c_ && c_ <= 'z'))		{
			return true;		}
		for(int i = 0; i < _acUmlaut.length; i++) {
			if (c_ == _acUmlaut[i]) {
				return true;			}		}
		return false;	}
	public static long timeToSeconds(String sTime_) {
		return ((long)MultiDate.getSecondsFromTime(sTime_));	}
	public static int getOccurances(String source, int zeichen) {
		int anzahl = -1;
		int index = 0;
		do {			index = source.indexOf(zeichen, index) + 1;
			anzahl++;
		} while (index != 0);
		return(anzahl);	}
	public static String multiplyChar(char c, int anzahl) {
		String s = "";
		while (anzahl > 0) {
			s += c;
			anzahl--;		}
		return(s);	}
	public static String multiplyChar(String sFill, int anzahl) {
		String sRet = "";
		while (anzahl > 0) {
			sRet += sFill;
			anzahl--;		}
		return(sRet);	}
	public static String paddWith(int number_, int stellen_,											char cPadd_)	{
		String sRetVal = itoa(number_);
		if (sRetVal.length() >= stellen_) {
			return(sRetVal);		}
		String sPadding = multiplyChar(cPadd_,												 stellen_ - sRetVal.length());
		sRetVal = sPadding + sRetVal;
		return(sRetVal);	}
	public static String paddWithSpace(int number, int stellen) {
		return paddWith(number, stellen, ' ');	}
	public static String paddWithZero(int number, int stellen) {
		return paddWith(number, stellen, '0');	}
	public static String rtrim(String s) {
		int index = s.length()-1;
		while (index >= 0 && s.charAt(index) == ' ') {
			index--;		}
		return(s.substring(0, index+1));	}
	public static String ltrim(String s) {
		int index = 0;  //s.length()-1;
		while (index < s.length() && s.charAt(index) == ' ') {
			index++;		}
		return(s.substring(index, s.length()));	}
	public static String unifySpaces(String s) {
		String sRetVal = new String();
		String sRest = s.trim();
		int index = 0;//s.length()-1;
		while (sRest != null && sRest.length() > 0) {
			index = sRest.indexOf(' ');
			if (index < 0) {
				sRetVal += sRest;
				sRest = null;
			} else {
				sRetVal += sRest.substring(0, index+1);
				sRest = sRest.substring(index+1, sRest.length());
				sRest = ltrim(sRest);			}		}
		return(sRetVal);	}}
