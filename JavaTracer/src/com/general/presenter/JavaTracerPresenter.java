package com.general.presenter;

import javax.swing.UIManager;

import com.alee.laf.WebLookAndFeel;
import com.general.model.configuration.JavaTracerConfigurationXml;
import com.general.settings.presenter.SettingsPresenter;
import com.general.view.JavaTracerView;
import com.inspector.controller.InspectorController;
import com.profiler.model.ProfilerModelInterface;
import com.profiler.presenter.ProfilerPresenter;
import com.tracer.controller.TracerController;

public class JavaTracerPresenter {

	private JavaTracerView view;
	
	private TracerController tracerController;
	private ProfilerPresenter profilerPresenter;
	private InspectorController inspectorController;
	private SettingsPresenter settingsPresenter;
	private JavaTracerConfigurationXml tracerConfiguration;
	
	/**
	 * Launch the application, no arguments needed.
	 */
	public static void main(String[] args) {
		new JavaTracerPresenter();
	}
	
	/**
	 * Creates all conections with the presenters. 
	 */
	
	public JavaTracerPresenter(){
		setLookAndFeel();
		this.tracerController = new TracerController();
		this.profilerPresenter = new ProfilerPresenter();
		this.inspectorController = new InspectorController();
		this.view = new JavaTracerView();
		this.settingsPresenter = new SettingsPresenter();
		this.tracerConfiguration  = JavaTracerConfigurationXml.getInstance();
		addController();
	}
	
	/**
	 * Sets Weblaf look and feel.
	 */
	private void setLookAndFeel(){
		
		try{
			UIManager.setLookAndFeel(WebLookAndFeel.class.getCanonicalName());
		}catch (Exception e){}
	}

	/**
	 * Set this object such as main controllers
	 */
	
	private void addController() {
		tracerController.setPresenter(this);
		profilerPresenter.setController(this);
		inspectorController.setController(this);
		settingsPresenter.setController(this); 
		view.setController(this);
	}
	
	public void showProfile() {
		profilerPresenter.loadTempProfile();
		profilerPresenter.showProfile();
	}	
	
	public void clickedOnExit() {
		System.exit(0);
	}

	public void clickedOnInspectTrace() {
		view.setVisible(false);
		inspectorController.open();
		inspectorController.clickedOpen();
	}

	public void clickedOnTraceProfile() {
		view.setVisible(false);
		tracerController.open();		
	}
	
	public void clickedOnViewProfile() {
		view.setVisible(false);
		profilerPresenter.showProfile();
	}

	public void clickedOnSettings() {
		view.setVisible(false);
		settingsPresenter.show();
	}
	
	public void back() {
	view.setVisible(true);
	}
	
	// Getters and setters
	
	public TracerController getTracerController() {
		return tracerController;
	}

	public ProfilerPresenter getProfilerController() {
		return profilerPresenter;
	}

	public InspectorController getInspectorController() {
		return inspectorController;
	}

	public void setTracerController(TracerController tracerController) {
		this.tracerController = tracerController;
	}

	public void setProfilerController(ProfilerPresenter profilerController) {
		this.profilerPresenter = profilerController;
	}

	public void setInspectorController(InspectorController inspectorController) {
		this.inspectorController = inspectorController;
	}

	public ProfilerModelInterface getProfiler() {
		return profilerPresenter.getProfiler();
	}

	public JavaTracerView getView() {
		return view;
	}

	public void setView(JavaTracerView view) {
		this.view = view;
	}

}
