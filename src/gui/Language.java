package gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Language {

	private static Map<String, String> keys;
	private static String language = null;
	private static List<String> languages;
	private static String prefix = "en_";
	private static Language theInstance;

	private Language() {
		keys = new TreeMap<String, String>();
		languages = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"language.dat"));
			String line = null;
			while ((line = in.readLine()) != null)
				if (line.contains("=")) {
					String[] tabs = line.split("=");
					if (tabs[0].equals("available_languages")) {
						String[] langs = tabs[1].split(",");
						for (String s : langs) {
							languages.add(s);
							if (prefix.substring(0, 2)
									.equals(s.substring(0, 2)))
								language = s.substring(3);
						}
					} else
						keys.put(tabs[0], tabs[1]);
				}
			in.close();
		} catch (IOException e) {
			System.out.println("ERROR: Can't read language.dat");
		}
	}

	private static void createInstance() {
		if (theInstance == null)
			theInstance = new Language();
	}

	public static String get(String key) {
		createInstance();
		String result = keys.get(prefix + key);
		if (result == null)
			System.out.println("Language can't find " + key);
		return result;
	}

	public static String getLanguage() {
		return language;
	}

	public static List<String> getLanguages() {
		createInstance();
		return languages;
	}

	public static String getPrefix() {
		return prefix;
	}

	public static void setLanguage(String langString) {
		createInstance();
		prefix = langString.substring(0, 2) + "_";
		for (String lang : languages)
			if (lang.substring(0, 2).equals(prefix.substring(0, 2)))
				language = lang.substring(3);
	}

}
