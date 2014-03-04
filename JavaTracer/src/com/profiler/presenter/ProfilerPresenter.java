package com.profiler.presenter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.general.presenter.JavaTracerPresenter;
import com.profiler.model.Profiler;
import com.profiler.model.ProfilerModelInterface;
import com.profiler.view.ProfilerView;
import com.profiler.view.ProfilerViewInterface;

public class ProfilerPresenter {
	
	private JavaTracerPresenter controller;
	
	private ProfilerModelInterface profiler;
	private ProfilerViewInterface view;
	
	public ProfilerPresenter(){
		this.profiler = new Profiler();
	}
	
	public void setController(JavaTracerPresenter javaTracerController) {
		this.controller = javaTracerController;
	}
		
	public void showProfile(){
		view = new ProfilerView();
		HashMap<String, Integer> classes = profiler.getRegistredClasses();
		int numCalledMethods = profiler.getTotalTimeCalledMethods();	 
		view.loadPieChart(classes,numCalledMethods);
	}

	public ProfilerViewInterface getView() {
		return view;
	}

	public ProfilerModelInterface getProfiler() {
		return profiler;
	}

	public void setView(ProfilerView view) {
		this.view = view;
	}

	public void setProfiler(Profiler profiler) {
		this.profiler = profiler;
	}

}
