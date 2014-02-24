package com.profiler.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.profiler.model.Profiler;
import com.profiler.view.ProfilerView;

public class ProfilerController {
	
	private ProfilerView view;
	private Profiler profiler;
	
	public ProfilerController(){
		this.profiler = new Profiler();
	}
	
	public void showProfile(){
		view = new ProfilerView(createDataset());
	}
	
	public PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        HashMap<String, Integer> classes = profiler.getRegistredClasses();
        Iterator<Entry<String,Integer>> iterator = classes.entrySet().iterator();        
        int numCalledMethods = profiler.getTotalTimeCalledMethods();
        
        
        while (iterator.hasNext()){
        	Entry<String,Integer> entry = iterator.next();
        	double times = entry.getValue().doubleValue();
        	double percentage = (times/numCalledMethods)*100;
        	dataset.setValue(entry.getKey(),percentage);
        }
        
        return dataset;
    }

	public ProfilerView getView() {
		return view;
	}

	public Profiler getProfiler() {
		return profiler;
	}

	public void setView(ProfilerView view) {
		this.view = view;
	}

	public void setProfiler(Profiler profiler) {
		this.profiler = profiler;
	}

}
