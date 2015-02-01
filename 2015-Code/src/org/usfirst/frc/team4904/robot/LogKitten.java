package org.usfirst.frc.team4904.robot;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class LogKitten {
	private FileOutputStream fileOutput;
	private File file;
	private int level;
	public final static int LEVEL_FATAL = 0;
	public final static int LEVEL_ERROR = 1;
	public final static int LEVEL_WARN = 2;
	public final static int LEVEL_VERBOSE = 3;
	public final static int LEVEL_DEBUG = 4;
	
	public LogKitten(String identifier, int logLevel) {
		level = logLevel;
		String filePath;
		filePath = "/home/lvuser/logs/" + identifier + "-" + new Date().getTime() + ".log"; // Set this sessions log to /home/lvuser/logs/[current time].log
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
	}
	
	public void f(String tag, String message) { // Log error message
		if (level < LEVEL_FATAL) {
			return;
		}
		try {
			String content = "FATAL: " + tag + ": " + message + " " + new Date().getTime() + "\n";
			fileOutput.write(content.getBytes());
			fileOutput.flush();
		}
		catch (IOException ioe) {
			System.out.println("Error logging error.");
			ioe.printStackTrace();
		}
	}
	
	public void e(String tag, String message) { // Log error message
		if (level < LEVEL_ERROR) {
			return;
		}
		try {
			String content = "ERROR: " + tag + ": " + message + " " + new Date().getTime() + "\n";
			fileOutput.write(content.getBytes());
			fileOutput.flush();
		}
		catch (IOException ioe) {
			System.out.println("Error logging error.");
			ioe.printStackTrace();
		}
	}
	
	public void w(String tag, String message) { // Log error message
		if (level < LEVEL_WARN) {
			return;
		}
		try {
			String content = "WARN: " + tag + ": " + message + " " + new Date().getTime() + "\n";
			fileOutput.write(content.getBytes());
			fileOutput.flush();
		}
		catch (IOException ioe) {
			System.out.println("Error logging error.");
			ioe.printStackTrace();
		}
	}
	
	public void v(String tag, String message) { // Log verbose message
		if (level < LEVEL_VERBOSE) {
			return;
		}
		try {
			String content = "VERBOSE: " + tag + ": " + message + " " + new Date().getTime() + "\n";
			fileOutput.write(content.getBytes());
			fileOutput.flush();
		}
		catch (IOException ioe) {
			System.out.println("Error logging verbose message.");
			ioe.printStackTrace();
		}
	}
	
	public void d(String tag, String message) { // Log debug message
		if (level < LEVEL_DEBUG) {
			return;
		}
		try {
			String content = "DEBUG: " + tag + ": " + message + " " + new Date().getTime() + "\n";
			fileOutput.write(content.getBytes());
			fileOutput.flush();
		}
		catch (IOException ioe) {
			System.out.println("Error logging debug message");
			ioe.printStackTrace();
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
}
