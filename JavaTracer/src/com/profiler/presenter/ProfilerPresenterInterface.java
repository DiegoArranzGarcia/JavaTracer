package com.profiler.presenter;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;

import com.profiler.model.ProfilerTree;

public interface ProfilerPresenterInterface {
	
	public void save();
	public void cancel();
	public Iterator<Entry<String,Integer>> getClassesInfo();
	public void openProfile(File file);
	public void saveProfile(File file);
	public ProfilerTree getTree();
	public void doubleClick(String completeName);
	public void clickedOnOpenProfile();
	public void clickedOnSettings();
	public void clickedOnAbout();

}
