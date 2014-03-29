package com.general.resources;

import javax.swing.ImageIcon;

public class ImageLoader {
	
	private static ImageLoader instance;
	
	private static String INFO = "info.png";
	private static String APPLICATION = "app.jpe";
	private static String EXPANDED = "expanded.gif";
	private static String FOLDED = "folded.gif";
	private static String PLUS_ICON = "plus.png";
	private static String MINUS_ICON = "minus.png";
	private static String ARROW_UP = "arrow-up.png";
	private static String ARROW_DOWN = "arrow-down.png";
	private static String DELETE = "delete.png";
	private static String MINIMIZE = "minimize.png";
	private static String MAXIMIZE = "maximize.png";
	private static String RESTORE = "restore.png";
	private static String PLAY = "play.png";
	private static String STOP = "stop.png";
	private static String CLASS = "class.gif";
	private static String PACKAGE = "package.png";
	private static String METHOD = "method.gif";
	
	private ImageIcon applicationIcon;
	private ImageIcon helpIcon;
	private ImageIcon expandedIcon;
	private ImageIcon foldedIcon;
	private ImageIcon plusIcon;
	private ImageIcon minusIcon;
	private ImageIcon arrowUpIcon;
	private ImageIcon arrowDownIcon;
	private ImageIcon deleteIcon;
	private ImageIcon minimizeIcon;
	private ImageIcon maximizeIcon;
	private ImageIcon restoreIcon;
	private ImageIcon stopIcon;
	private ImageIcon playIcon;
	private ImageIcon classIcon;
	private ImageIcon packageIcon;
	private ImageIcon methodIcon;

	private ImageLoader(){
		instance = this;
		applicationIcon = new ImageIcon(getClass().getResource(APPLICATION));
		helpIcon = new ImageIcon(getClass().getResource(INFO));
		expandedIcon = new ImageIcon(getClass().getResource(EXPANDED));
		foldedIcon = new ImageIcon(getClass().getResource(FOLDED));
		plusIcon = new ImageIcon(getClass().getResource(PLUS_ICON));
		minusIcon = new ImageIcon(getClass().getResource(MINUS_ICON));
		arrowUpIcon = new ImageIcon(getClass().getResource(ARROW_UP));
		arrowDownIcon = new ImageIcon(getClass().getResource(ARROW_DOWN));
		deleteIcon = new ImageIcon(getClass().getResource(DELETE));
		minimizeIcon = new ImageIcon(getClass().getResource(MINIMIZE));
		restoreIcon = new ImageIcon(getClass().getResource(RESTORE));
		maximizeIcon = new ImageIcon(getClass().getResource(MAXIMIZE));
		playIcon = new ImageIcon(getClass().getResource(PLAY));
		stopIcon = new ImageIcon(getClass().getResource(STOP));
		packageIcon = new ImageIcon(getClass().getResource(PACKAGE));
		classIcon = new ImageIcon(getClass().getResource(CLASS));
		methodIcon = new ImageIcon(getClass().getResource(METHOD));
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

	public ImageIcon getArrowDownIcon() {
		return arrowDownIcon;
	}

	public ImageIcon getArrowUpIcon() {
		return arrowUpIcon;
	}	
	
	public ImageIcon getDeleteIcon(){
		return deleteIcon;
	}

	public ImageIcon getMinimizeIcon() {
		return minimizeIcon;
	}

	public ImageIcon getMaximizeIcon() {
		return maximizeIcon;
	}

	public ImageIcon getRestoreIcon() {
		return restoreIcon;
	}

	public ImageIcon getStopIcon() {
		return stopIcon;
	}

	public ImageIcon getPlayIcon() {
		return playIcon;
	}

	public ImageIcon getClassIcon() {
		return classIcon;
	}

	public ImageIcon getPackageIcon() {
		return packageIcon;
	}

	public ImageIcon getMethodIcon() {
		return methodIcon;
	}
	
	public void setPlusIcon(ImageIcon plusIcon) {
		this.plusIcon = plusIcon;
	}

	public void setMinusIcon(ImageIcon minusIcon) {
		this.minusIcon = minusIcon;
	}
}
