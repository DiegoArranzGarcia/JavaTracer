package com.general.resources;

import javax.swing.ImageIcon;

public class ImageLoader {
	
	private static ImageLoader instance;
	
	private static String INFO_IMAGE = "info.png";
	private static String APPLICATION_IMAGE = "app.jpe";
	private static String EXPANDED_ICON = "expanded.gif";
	private static String FOLDED_ICON = "folded.gif";
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

	public void setPlusIcon(ImageIcon plusIcon) {
		this.plusIcon = plusIcon;
	}

	public void setMinusIcon(ImageIcon minusIcon) {
		this.minusIcon = minusIcon;
	}

	private ImageLoader(){
		instance = this;
		applicationIcon = new ImageIcon(getClass().getResource(APPLICATION_IMAGE));
		helpIcon = new ImageIcon(getClass().getResource(INFO_IMAGE));
		expandedIcon = new ImageIcon(getClass().getResource(EXPANDED_ICON));
		foldedIcon = new ImageIcon(getClass().getResource(FOLDED_ICON));
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

}
