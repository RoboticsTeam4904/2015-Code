package org.usfirst.frc4904.robot;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class LogKitten {
	private FileOutputStream fileOutput;
	private final File file;
	private final int level;
	private final int printLevel;
	private final String identifier;
	public final static int LEVEL_FATAL = 0;
	public final static int LEVEL_ERROR = 1;
	public final static int LEVEL_WARN = 2;
	public final static int LEVEL_VERBOSE = 3;
	public final static int LEVEL_DEBUG = 4;
	
	public LogKitten(String identifier, int logLevel, int printLevel) {
		this.identifier = identifier;
		level = logLevel;
		String filePath;
		filePath = "/home/lvuser/logs/" + identifier + "-" + timestamp() + ".log"; // Set this sessions log to /home/lvuser/logs/[current time].log
		file = new File(filePath);
		try {
			// Create new file if it doesn't exist (this should happen)
			if (!file.exists()) {
				file.createNewFile();
			}
			// Create FileOutputStream to actually write to the file.
			fileOutput = new FileOutputStream(file);
		}
		catch (IOException ioe) {
			System.out.println("Could not open logfile");
			ioe.printStackTrace();
		}
		this.printLevel = printLevel;
	}
	
	public LogKitten(String identifier, int logLevel) {
		this(identifier, logLevel, LEVEL_FATAL);
	}
	
	public void f(String tag, String message) { // Log error message
		if (level < LEVEL_FATAL) {
			return;
		}
		try {
			String content = timestamp() + " FATAL: " + tag + ": " + message + " " + "\n";
			fileOutput.write(content.getBytes());
			fileOutput.flush();
		}
		catch (IOException ioe) {
			System.out.println("Error logging error.");
			ioe.printStackTrace();
		}
		if (printLevel >= LEVEL_FATAL) {
			System.out.println(identifier + " " + "FATAL: " + tag + ": " + message + "\n");
		}
	}
	
	public void e(String tag, String message) { // Log error message
		if (level < LEVEL_ERROR) {
			return;
		}
		try {
			String content = timestamp() + " ERROR: " + tag + ": " + message + " " + "\n";
			fileOutput.write(content.getBytes());
			fileOutput.flush();
		}
		catch (IOException ioe) {
			System.out.println("Error logging error.");
			ioe.printStackTrace();
		}
		if (printLevel >= LEVEL_ERROR) {
			System.out.println(identifier + " " + "ERROR: " + tag + ": " + message + "\n");
		}
	}
	
	public void w(String tag, String message) { // Log error message
		if (level < LEVEL_WARN) {
			return;
		}
		try {
			String content = timestamp() + " WARN: " + tag + ": " + message + " " + "\n";
			fileOutput.write(content.getBytes());
			fileOutput.flush();
		}
		catch (IOException ioe) {
			System.out.println("Error logging error.");
			ioe.printStackTrace();
		}
		if (printLevel >= LEVEL_WARN) {
			System.out.println(identifier + " " + "WARN: " + tag + ": " + message + "\n");
		}
	}
	
	public void v(String tag, String message) { // Log verbose message
		if (level < LEVEL_VERBOSE) {
			return;
		}
		try {
			String content = timestamp() + " VERBOSE: " + tag + ": " + message + " " + "\n";
			fileOutput.write(content.getBytes());
			fileOutput.flush();
		}
		catch (IOException ioe) {
			System.out.println("Error logging verbose message.");
			ioe.printStackTrace();
		}
		if (printLevel >= LEVEL_VERBOSE) {
			System.out.println(identifier + " " + "VERBOSE: " + tag + ": " + message + "\n");
		}
	}
	
	public void d(String tag, String message) { // Log debug message
		if (level < LEVEL_DEBUG) {
			return;
		}
		try {
			String content = timestamp() + " DEBUG: " + tag + ": " + message + " " + "\n";
			fileOutput.write(content.getBytes());
			fileOutput.flush();
		}
		catch (IOException ioe) {
			System.out.println("Error logging debug message");
			ioe.printStackTrace();
		}
		if (printLevel >= LEVEL_DEBUG) {
			System.out.println(identifier + " " + "DEBUG: " + tag + ": " + message + " " + Integer.toString(printLevel) + "\n");
		}
	}
	
	public void clean() {
		try {
			fileOutput.close();
		}
		catch (IOException ioe) {
			System.out.println("Could not close logfile output. This should never happen");
			ioe.printStackTrace();
		}
	}
	
	private String timestamp() {
		Calendar now = Calendar.getInstance();
		String timestamp = Integer.toString(now.get(Calendar.YEAR));
		timestamp += "-" + Integer.toString(now.get(Calendar.MONTH) + 1);
		timestamp += "-" + Integer.toString(now.get(Calendar.DATE));
		timestamp += "_" + Integer.toString(now.get(Calendar.HOUR_OF_DAY));
		timestamp += ":" + Integer.toString(now.get(Calendar.MINUTE));
		timestamp += ":" + Integer.toString(now.get(Calendar.SECOND));
		return timestamp;
	}
}
