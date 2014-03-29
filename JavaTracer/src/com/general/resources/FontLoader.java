package com.general.resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class FontLoader {
	
	public static String APP_FONT_NAME = "QuattrocentoSans-Regular";
	private static String APP_FONT_FILE = APP_FONT_NAME + ".ttf";
	private static FontLoader instance;
	
	private Font app_font;
	
	private FontLoader(){
		try {
			app_font = Font.createFont(Font.TRUETYPE_FONT,getClass().getResourceAsStream(APP_FONT_FILE));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static FontLoader getInstance(){
		if (instance == null) 
			instance = new FontLoader();
		return instance;	
	}
	
	
	public void initAppFont(){
		FontUIResource f = new FontUIResource(new Font(APP_FONT_NAME,Font.PLAIN,15));
		java.util.Enumeration<?> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements())
	    {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof javax.swing.plaf.FontUIResource)
	        {
	            UIManager.put(key, f);
	        }
	    }
	}

	public String getFontName() {
		return APP_FONT_NAME;
	}
	
	
}
