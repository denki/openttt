package gui;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Interaction {
	public static File getSettingsDirectory() {
		String userHome = System.getProperty("user.home");
		if (userHome == null)
			throw new IllegalStateException("user.home==null");
		File home = new File(userHome);
		File settingsFile = new File(home, ".openttt");
		if (!settingsFile.exists())
			try {
				if (!settingsFile.createNewFile())
					System.out.println("ERROR: Could not create file.");
			} catch (IOException e) {
				System.out.println("ERROR: Cannot create config file.");
			}
		return settingsFile;
	}

	public static String loadConfig() {
		File conf = getSettingsDirectory();
		try {
			return loadFile(conf.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("ERROR: " + conf.getAbsolutePath()
					+ " not readable.");
		}
		return null;
	}

	public static String loadFile(String fileName) throws IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(fileName));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	private static void print(File file) {
		try {
			if (Desktop.getDesktop().isSupported(Desktop.Action.PRINT))
				try {
					Desktop.getDesktop().print(file);
				} catch (IOException e) {
					System.out
							.println("WARNING: Printing not supported on this platform. Trying to open in Browser.");
					try {
						Desktop.getDesktop().browse(file.toURI());
					} catch (IOException e2) {
						System.out
								.println("ERROR: Couldn't print or open file.");
					}
				}
			else {
				System.out
						.println("WARNING: Printing not supported on this platform. Trying to open in Browser.");
				try {
					Desktop.getDesktop().browse(file.toURI());
				} catch (IOException e2) {
					System.out.println("ERROR: Couldn't print or open file.");
				}
			}
		} catch (Exception e) {
			System.out
					.println("WARNING: Desktop API seems to be unsupported. Trying to open using CLI.");
			try {
				Process p = Runtime.getRuntime().exec(
						new String[] { "firefox", file.toString() });
				try {
					p.waitFor();
				} catch (InterruptedException e1) {
					System.out.println("Process interrupted");
				}
				int success = p.exitValue();
				switch (success) {
				case 0:
					System.out.println("Opening in CLI was successful.");
					break;
				default:
					System.out
							.println("ERROR: Unable to print or open. Install firefox please.");
				}
			} catch (IOException e1) {
				System.out
						.println("ERROR: File not found. Install firefox please.");
			}
		}
	}

	public static void print(String fileName) {
		File file;
		if (System.getProperty("java.io.tmpdir").charAt(0) == '/')
			file = new File(System.getProperty("java.io.tmpdir") + "/"
					+ fileName + ".html");
		else
			file = new File(System.getProperty("java.io.tmpdir") + "\\"
					+ fileName + ".html");
		print(file);
	}

	public static void saveConfig(String cfg) {
		saveText(getSettingsDirectory().getAbsolutePath(), cfg);
	}

	public static void saveHtml(String fileName, String text) {
		if (System.getProperty("java.io.tmpdir").charAt(0) == '/')
			saveText(System.getProperty("java.io.tmpdir") + "/" + fileName
					+ ".html", text);
		else
			saveText(System.getProperty("java.io.tmpdir") + "\\" + fileName
					+ ".html", text);
	}

	public static void saveText(String fileName, String text) {
		File file = new File(fileName);
		PrintWriter out;
		try {
			out = new PrintWriter(file);
			out.write(text);
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File not found.");
		}

	}

}
