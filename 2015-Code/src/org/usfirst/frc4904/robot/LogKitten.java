package org.usfirst.frc4904.robot;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Comparator;

public class LogKitten {
	private FileOutputStream fileOutput;
	private final File file;
	private final String identifier;
	private final DebugLevel logLevel;
	private final DebugLevel printLevel;
	public final static DebugLevel LEVEL_FATAL = new DebugLevel("FATAL", 0);
	public final static DebugLevel LEVEL_ERROR = new DebugLevel("ERROR", 1);
	public final static DebugLevel LEVEL_WARN = new DebugLevel("WARN", 2);
	public final static DebugLevel LEVEL_VERBOSE = new DebugLevel("VERBOSE", 3);
	public final static DebugLevel LEVEL_DEBUG = new DebugLevel("DEBUG", 4);
	public static DebugLevel DEFAULT_LOG_LEVEL = LEVEL_VERBOSE;
	public static DebugLevel DEFAULT_PRINT_LEVEL = LEVEL_WARN;
	private static String LOG_PATH = "/home/lvuser/logs/";
	
	public LogKitten(String identifier, DebugLevel logLevel, DebugLevel printLevel) {
		this.identifier = identifier;
		this.logLevel = logLevel;
		String filePath = LOG_PATH + identifier + ".log"; // Set this sessions log to /home/lvuser/logs/[current time].log
		file = new File(filePath);
		try {
			// Delete file if it exists
			if (file.exists()) {
				file.delete();
			}
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
	
	public LogKitten(String identifier, DebugLevel printLevel) {
		this(identifier, DEFAULT_LOG_LEVEL, printLevel);
	}
	
	public LogKitten(String identifier) {
		this(identifier, DEFAULT_PRINT_LEVEL);
	}
	
	// If no identifier is given, use caller class name
	public LogKitten(DebugLevel logLevel, DebugLevel printLevel) {
		this(Thread.currentThread().getStackTrace()[1].getClassName(), logLevel, printLevel);
	}
	
	public LogKitten(DebugLevel printLevel) {
		this(Thread.currentThread().getStackTrace()[1].getClassName(), printLevel);
	}
	
	public LogKitten() {
		this(Thread.currentThread().getStackTrace()[1].getClassName());
	}
	
	public static void setDefaultLogLevel(DebugLevel DEFAULT_LOG_LEVEL) {
		LogKitten.DEFAULT_LOG_LEVEL = DEFAULT_LOG_LEVEL;
	}
	
	public static void setDefaultPrintLevel(DebugLevel DEFAULT_PRINT_LEVEL) {
		LogKitten.DEFAULT_PRINT_LEVEL = DEFAULT_PRINT_LEVEL;
	}
	
	public static void setLogPath(String LOG_PATH) {
		LogKitten.LOG_PATH = LOG_PATH;
	}
	
	private void logMessage(String message, DebugLevel level) {
		if (logLevel.compareTo(level) > 0) {
			try {
				String content = timestamp() + " " + level.getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + message + " \n";
				fileOutput.write(content.getBytes());
				fileOutput.flush();
			}
			catch (IOException ioe) {
				System.out.println("Error logging " + level.getName() + " message");
				ioe.printStackTrace();
			}
		}
		if (printLevel.compareTo(level) > 0) {
			System.out.println(identifier + " " + level.getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + message + " \n");
		}
	}
	
	public void f(String message) { // Log fatal message
		logMessage(message, LEVEL_FATAL);
	}
	
	public void e(String message) { // Log error message
		logMessage(message, LEVEL_ERROR);
	}
	
	public void w(String message) { // Log warn message
		logMessage(message, LEVEL_WARN);
	}
	
	public void v(String message) { // Log verbose message
		logMessage(message, LEVEL_VERBOSE);
	}
	
	public void d(String message) { // Log debug message
		logMessage(message, LEVEL_DEBUG);
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
	
	private static class DebugLevel implements Comparable<DebugLevel>, Comparator<DebugLevel> {
		private String name;
		private int severity;
		
		public DebugLevel(String name, int severity) {
			this.severity = severity;
			this.name = name;
		}
		
		public int getSeverity() {
			return severity;
		}
		
		public String getName() {
			return name;
		}
		
		public int compare(DebugLevel o1, DebugLevel o2) {
			return severity - o2.getSeverity();
		}
		
		public int compareTo(DebugLevel o) {
			if (compare(this, o) > 0) {
				return 1;
			} else if (compare(this, o) == 0) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}
