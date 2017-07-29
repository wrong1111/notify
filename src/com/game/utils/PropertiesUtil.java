package com.game.utils;

import java.util.ResourceBundle;

public class PropertiesUtil {
	private static final String CONFIG_FILE = "init";

	private static ResourceBundle bundle;
	static {
		try {
			bundle = ResourceBundle.getBundle(CONFIG_FILE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return bundle.getString(key);
	}
	

}
