package com.profiler.presenter;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;

public interface ProfilerPresenterInterface {
	
	public void save();
	public void cancel();
	public Iterator<Entry<String,Integer>> getClassesInfo();
	public void openProfile(File file);
	public void saveProfile(File file);
	public void closeWindow();

}
