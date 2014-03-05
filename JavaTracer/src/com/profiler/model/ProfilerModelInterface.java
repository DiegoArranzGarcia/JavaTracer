package com.profiler.model;

import java.io.File;
import java.util.HashMap;

import com.sun.jdi.event.MethodEntryEvent;

public interface ProfilerModelInterface {

	public int getTotalTimeCalledMethods();
	public HashMap<String, Integer> getRegistredClasses();
	public void profileEvent(MethodEntryEvent event);
	public void saveProfile(ProfileData data,File file);
	public ProfileData openProfile(File file);

}
