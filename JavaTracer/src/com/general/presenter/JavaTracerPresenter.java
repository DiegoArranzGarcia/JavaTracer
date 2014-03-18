package com.general.presenter;

import java.io.File;

import javax.swing.UIManager;

import com.alee.laf.WebLookAndFeel;
import com.general.resources.FontLoader;
import com.general.settings.presenter.SettingsPresenter;
import com.inspector.controller.InspectorController;
import com.profiler.model.ProfilerModelInterface;
import com.profiler.presenter.ProfilerPresenter;
import com.tracer.arguments.presenter.ArgumentsPresenter;
import com.tracer.controller.TracerController;

public class JavaTracerPresenter {

	private TracerController tracerController;
	private ProfilerPresenter profilerPresenter;
	private InspectorController inspectorController;
	private SettingsPresenter settingsPresenter;
	private ArgumentsPresenter argumentsPresenter;
	
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
		setUIFont();
		this.tracerController = new TracerController();
		this.profilerPresenter = new ProfilerPresenter();
		this.inspectorController = new InspectorController();
		this.settingsPresenter = new SettingsPresenter();
		this.argumentsPresenter = new ArgumentsPresenter();
		setPresenter();
		tracerController.open();
	}
	
	private void setUIFont() {
		FontLoader fontLoader = FontLoader.getInstance();
		fontLoader.initAppFont();
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
	
	private void setPresenter() {
		tracerController.setPresenter(this);
		profilerPresenter.setController(this);
		inspectorController.setController(this);
		settingsPresenter.setController(this); 
		argumentsPresenter.setController(this); 
	}
	
	public void showProfile() {
		profilerPresenter.loadTempProfile();
		profilerPresenter.showProfile();
		String nameFile=tracerController.getNameXml()+"Profiler.xml";
		profilerPresenter.saveProfile(new File(nameFile));
	}	
	
	public void clickedOnExit() {
		System.exit(0);
	}

	public void clickedOnInspectTrace() {
		inspectorController.clickedOpen();
	}

	public void clickedOnTraceProfile() {
		tracerController.open();		
	}
	
	public void clickedOnViewProfile() {
		profilerPresenter.showProfile();
	}

	public void clickedOnSettings() {
		settingsPresenter.show();
	}
	
	public void clicekOnEditArguments() {
		argumentsPresenter.show();
	}
	
	public void back() {
		tracerController.open();
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

	public void openTrace(String xmlPath) {
		inspectorController.open(xmlPath);
	}

	public void setVisible(boolean b) {
		tracerController.setVisible(false);
	}

}
