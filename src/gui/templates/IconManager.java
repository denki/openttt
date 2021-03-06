package gui.templates;

import java.net.URL;

import javax.swing.ImageIcon;

public class IconManager {
	private static String iconPath = "icons/";
	private static String px = "48";
	private static String px_small = "22";
	private static String suffix = ".png";

	public static ImageIcon getImageIcon(String name) {
		URL url;
		if (name.equals("main"))
			url = ClassLoader.getSystemClassLoader().getResource(iconPath + name + suffix);
		else if (name.contains("_small"))
			url = ClassLoader.getSystemClassLoader().getResource(iconPath + px_small + "/"
					+ name.subSequence(0, name.indexOf("_small")) + suffix);
		else
			url = ClassLoader.getSystemClassLoader().getResource(iconPath + px + "/" + name + suffix);
		return new ImageIcon(url);
	}

	public static String getPx() {
		return px;
	}

	public static void setPx(String pixels) {
		px = pixels;
	}
}
