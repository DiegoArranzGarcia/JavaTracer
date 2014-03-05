package com.general.presenter;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.alee.laf.WebLookAndFeel;
import com.general.view.JavaTracerView;
import com.inspector.controller.InspectorController;
import com.profiler.model.ProfilerModelInterface;
import com.profiler.presenter.ProfilerPresenter;
import com.tracer.controller.TracerController;

public class JavaTracerPresenter {

	private JavaTracerView view;
	
	private TracerController tracerController;
	private ProfilerPresenter profilerController;
	private InspectorController inspectorController;
	
	/**
	 *  Creates the controllers of aplication (Tracer, Profiler and Inspector controller). 
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		new JavaTracerPresenter();
	}
	
	public JavaTracerPresenter() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		UIManager.setLookAndFeel(WebLookAndFeel.class.getCanonicalName () );
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
		tracerController.setPresenter(this);
		profilerController.setController(this);
		inspectorController.setController(this);
		view.setController(this);
	}
	
	public void showProfile() {
		profilerController.loadTempProfile();
		profilerController.showProfile();
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
	
	public void clickedOnViewProfile() {
		view.setVisible(false);
		profilerController.showProfile();
	}

	public void back() {
		view.setVisible(true);
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

}
