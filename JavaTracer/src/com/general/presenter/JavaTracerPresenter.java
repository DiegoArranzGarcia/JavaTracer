package com.general.presenter;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.alee.laf.WebLookAndFeel;
import com.general.resources.FontLoader;
import com.general.settings.presenter.SettingsPresenter;
import com.general.view.AboutDialog;
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
	}
	
	public void showProfile() {
		profilerPresenter.loadTempProfile();
		String nameFile = tracerController.getXmlName();
		profilerPresenter.showProfile(nameFile);
		profilerPresenter.saveProfile(new File(nameFile));
	}	
	
	public void clickedOnExit() {
		System.exit(0);
	}

	public void clickedOnLoadTrace() {
		inspectorController.clickedOnLoadTrace();
	}

	public void clickedOnSettings() {
		settingsPresenter.show();
	}
	
	public void back() {
		tracerController.open();
	}
	
	public void clickedOnLoadProfile() {
		profilerPresenter.clickedOnOpenProfile();
	}

	public void clickedOnAbout() {
		AboutDialog aboutDialog = new AboutDialog();
		aboutDialog.setVisible(true);
	}
	
	public void clickedOnHelp(){
		
	}
	
	public void openTrace(String xmlPath) {
		inspectorController.open(xmlPath);
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

	public void setVisible(boolean b) {
		tracerController.setVisible(false);
	}

	public JFrame getView() {
		return tracerController.getView();
	}

}
