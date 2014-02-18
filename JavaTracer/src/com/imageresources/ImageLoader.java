package com.imageresources;

import javax.swing.ImageIcon;

public class ImageLoader {
	
	private static ImageLoader instance;
	private static String HELP_IMAGE = "imageHelp.jpe";
	private static String APPLICATION_IMAGE = "app.jpe";
	private static String EXPANDED_ICON = "expanded.gif";
	private static String FOLDED_ICON = "folded.gif";
	private static String PLUS_ICON = "minus.png";
	private static String MINUS_ICON = "minus.png";
	
	private ImageIcon applicationIcon;
	private ImageIcon helpIcon;
	private ImageIcon expandedIcon;
	private ImageIcon foldedIcon;
	private ImageIcon plusIcon;
	private ImageIcon minusIcon;

	private ImageLoader(){
		instance = this;
		applicationIcon = new ImageIcon(getClass().getResource(APPLICATION_IMAGE));
		helpIcon = new ImageIcon(getClass().getResource(HELP_IMAGE));
		expandedIcon = new ImageIcon(getClass().getResource(EXPANDED_ICON));
		foldedIcon = new ImageIcon(getClass().getResource(FOLDED_ICON));
		plusIcon = new ImageIcon(getClass().getResource(PLUS_ICON));
		minusIcon = new ImageIcon(getClass().getResource(MINUS_ICON));
	}
	
	public static ImageLoader getInstance(){
		if (instance == null) return new ImageLoader();
		else return instance;
	}

	public ImageIcon getApplicationIcon() {
		return applicationIcon;
	}

	public ImageIcon getHelpIcon() {
		return helpIcon;
	}
	
	public ImageIcon getExpandedIcon() {
		return expandedIcon;
	}
	
	public ImageIcon getFoldedIcon() {
		return foldedIcon;
	}

	public ImageIcon getPlusIcon() {
		return plusIcon;
	}

	public ImageIcon getMinusIcon() {
		return minusIcon;
	}
	
	
	

}
