package com.profiler.model;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;

import com.sun.jdi.event.MethodEntryEvent;

public interface ProfilerModelInterface {

	public ProfilerTree getProfileTree();
	public void profileEvent(MethodEntryEvent event);
	public void saveProfile(ProfilerTree data,File file);
	public ProfilerTree openProfile(File file);
	public void clean();
	public Iterator<Entry<String, Integer>> getClassesInfo();

}
