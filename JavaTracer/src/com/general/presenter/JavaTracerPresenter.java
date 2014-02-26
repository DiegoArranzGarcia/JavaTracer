package com.general.presenter;

import com.general.view.JavaTracerView;
import com.inspector.controller.InspectorController;
import com.javatracer.controller.TracerController;
import com.profiler.model.ProfilerModelInterface;
import com.profiler.presenter.ProfilerPresenter;

public class JavaTracerPresenter {

	private JavaTracerView view;
	
	private TracerController tracerController;
	private ProfilerPresenter profilerController;
	private InspectorController inspectorController;
	
	/**
	 *  Creates the controllers of aplication (Tracer, Profiler and Inspector controller). 
	 */
	
	public static void main(String[] args){
		new JavaTracerPresenter();
	}
	
	public JavaTracerPresenter(){
		this.tracerController = new TracerController();
		this.profilerController = new ProfilerPresenter();
		this.inspectorController = new InspectorController();
		this.view = new JavaTracerView();
		addController();
	}
	
	/**
	 * Set this object such as main controllers
	 */
	
	private void addController() {
		tracerController.setController(this);
		profilerController.setController(this);
		inspectorController.setController(this);
		view.setController(this);
	}

	public void showProfile() {
		profilerController.showProfile();
	}	
	
	// Getters and setters
	
	public TracerController getTracerController() {
		return tracerController;
	}

	public ProfilerPresenter getProfilerController() {
		return profilerController;
	}

	public InspectorController getInspectorController() {
		return inspectorController;
	}

	public void setTracerController(TracerController tracerController) {
		this.tracerController = tracerController;
	}

	public void setProfilerController(ProfilerPresenter profilerController) {
		this.profilerController = profilerController;
	}

	public void setInspectorController(InspectorController inspectorController) {
		this.inspectorController = inspectorController;
	}

	public ProfilerModelInterface getProfiler() {
		return profilerController.getProfiler();
	}

	public JavaTracerView getView() {
		return view;
	}

	public void setView(JavaTracerView view) {
		this.view = view;
	}

	public void clickedOnExit() {
		System.exit(0);
	}

	public void clickedOnInspectTrace() {
		view.setVisible(false);
		inspectorController.open();
	}

	public void clickedOnTraceProfile() {
		view.setVisible(false);
		tracerController.open();		
	}

	public void back() {
		view.setVisible(true);
	}

}
