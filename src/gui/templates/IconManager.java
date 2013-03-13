package gui.templates;

import javax.swing.ImageIcon;

public class IconManager {
	private static String iconPath = "icons/";
	private static String px = "48";
	private static String px_small = "22";
	private static String suffix = ".png";

	public static ImageIcon getImageIcon(String name) {
		ImageIcon result;
		if (name.equals("main"))
			result = new ImageIcon(iconPath + name + suffix);
		else if (name.contains("_small"))
			result = new ImageIcon(iconPath + px_small + "/"
					+ name.subSequence(0, name.indexOf("_small")) + suffix);
		else
			result = new ImageIcon(iconPath + px + "/" + name + suffix);
		return result;
	}

	public static String getPx() {
		return px;
	}

	public static void setPx(String pixels) {
		px = pixels;
	}
}
