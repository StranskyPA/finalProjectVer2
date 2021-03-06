package modeselection.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;

import lejos.hardware.Key;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import modeselection.vision.config.BasicVisionBot;

public class Util {
	
	/** 
	 * + 1 (mod n)
	 */
	public static int modUp(int x, int n) {
		return (x + 1) % n;
	}
	
	/**
	 * - 1 (mod n)
	 */
	public static int modDown(int x, int n) {
		return (x + n - 1) % n;
	}
	
	/**
	 * Log_2 of the value, rounding down
	 */
	public static int log2Floor(int value) {
		Util.assertArgument(value > 0, "Log_2 undefined for " + value);
		int log = -1;
		while (value > 0) {
			value /= 2;
			log += 1;
		}
		return log;
	}
	
	/**
	 * Purely integer exponentiation
	 */
	public static long pow(long base, int exp) {
		assertArgument(exp >= 0, "No negative exponents");
		if (exp == 0) {
			return 1;
		} else if (exp % 2 == 1) {
			return base * pow(base, exp - 1);
		} else {
			return pow(base * base, exp / 2);
		}
	}
	
	/**
	 * Reads in a File and dumps it into a String.
	 * 
	 * @param f
	 * @return a String containing all text from f.
	 * @throws FileNotFoundException
	 */
	public static String fileToString(File f) throws FileNotFoundException {
		Scanner s = new Scanner(f);
		StringBuilder sb = new StringBuilder();
		while (s.hasNextLine()) {
			sb.append(s.nextLine() + '\n');
		}
		s.close();
		return sb.toString();
	}
	
	public static <T> T fileToObject(File f, Function<String,T> extractor) throws FileNotFoundException {
		return extractor.apply(fileToString(f));
	}
	
	/**
	 * Takes a String and dumps it into a File.
	 * @throws IOException 
	 * 
	 * 
	 */
	public static void stringToFile(File f, String s) throws IOException {
		PrintWriter p = new PrintWriter(new FileWriter(f));
		p.println(s);
		p.close();
	}
	
	/**
	 * Takes any Object with a solid toString() implementation and dumps it into a File.
	 * @throws IOException 
	 * 
	 * 
	 */
	public static void objectToFile(File f, Object obj) throws IOException {
		stringToFile(f, obj.toString());
	}

	
	/**
	 * Stops all EV3 motors.
	 */
	public static void stopAllMotors() {
		Motor.A.stop(true);
		Motor.B.stop(true);
		Motor.C.stop(true);
		Motor.D.stop();
	}
	
	/**
	 * Checks if the button is down. 
	 * If so, stops all motors, then waits for button to go up.
	 * @param button
	 * @return true if button is down
	 */
	public static boolean isClicked(Key button) {
		if (button.isDown()) {
			stopAllMotors();
			while (button.isDown()) {}
			return true;
		}
		return false;
	}

	public static byte bool2byte(boolean bool) {
		return bool ? (byte)-1 : (byte)0;
	}
	
	public static boolean byte2bool(byte b) {
		return b != 0;
	}
	
	public static void assertion(boolean condition, Supplier<RuntimeException> exceptor) {
		if (!condition) {throw exceptor.get();}
	}
	
	public static void assertArgument(boolean condition, String msg) {
		assertion(condition, () -> new IllegalArgumentException(msg));
	}
	
	public static void assertState(boolean condition, String msg) {
		assertion(condition, () -> new IllegalStateException(msg));
	}
	
	public static void checkCmdLine(String[] args, int min, String msg) {
		if (args.length < min) {
			System.out.println("Usage: " + msg);
			System.exit(1);
		}
	}
	
	/**
	 * Takes an input containing matching pairs of curly braces ("{}") and returns 
	 * an array of strings where everything in the top level of braces 
	 * (and between braces) is placed in its own string.
	 */
	public static ArrayList<String> debrace(String input) {
		ArrayList<String> result = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		int numOpen = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '{') {
				numOpen += 1;
				if (numOpen == 1) {
					if (current.length() > 0) result.add(current.toString());
					current = new StringBuilder();
				} else {
					current.append(input.charAt(i));
				}
			} else if (input.charAt(i) == '}') {
				numOpen -= 1;
				if (numOpen > 0) {
					current.append(input.charAt(i));
				} else if (current.length() > 0) {
					result.add(current.toString());
					current = new StringBuilder();
				}
				while (i + 1 < input.length() && Character.isWhitespace(input.charAt(i + 1))) {
					i += 1;
				}
			} else {
				current.append(input.charAt(i));
			}
		}
		if (current.length() > 0) result.add(current.toString());
		return result;
	}

	public static double angleDiff(double theta1, double theta2) {
		return Math.atan2(Math.sin(theta1 - theta2), Math.cos(theta1 - theta2));
	}
	
	public static void rightJustifyLong(long value, int row) {
		String vStr = Long.toString(value);
		int x = BasicVisionBot.CHAR_COLS - vStr.length();
		String leading = "";
		for (int i = 0; i < x; i++) {
			leading += " ";
		}
		LCD.drawString(leading + vStr, 0, row);
	}
	
	public static void motorAt(NXTRegulatedMotor m, int speed) {
		m.setSpeed(speed);
		if (speed < 0) {
			m.backward();
		} else {
			m.forward();
		}
	}
	
	/**
	 * Converts a String to a target enum type.
	 * Throws IllegalArgumentException if this is not possible.
	 */
	public static <E extends Enum<E>> E toEnum(Class<E> enumType, String src) {
		for (E possibility: enumType.getEnumConstants()) {
			if (possibility.toString().equals(src)) {
				return possibility;
			}
		}
		throw new IllegalArgumentException(String.format("'%s' does not match enum type", src));
	}
}
