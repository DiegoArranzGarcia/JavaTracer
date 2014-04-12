package com.general.presenter;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.alee.laf.WebLookAndFeel;
import com.general.resources.FontLoader;
import com.general.settings.presenter.SettingsPresenter;
import com.general.view.AboutDialog;
import com.inspector.presenter.InspectorPresenter;
import com.profiler.model.ProfilerModelInterface;
import com.profiler.presenter.ProfilerPresenter;
import com.tracer.presenter.TracerPresenter;

public class JavaTracerPresenter {

	private TracerPresenter tracerPresenter;
	private ProfilerPresenter profilerPresenter;
	private InspectorPresenter inspectorPresenter;
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
		this.tracerPresenter = new TracerPresenter();
		this.profilerPresenter = new ProfilerPresenter();
		this.inspectorPresenter = new InspectorPresenter();
		this.settingsPresenter = new SettingsPresenter();
		setPresenter();
		tracerPresenter.open();
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
		tracerPresenter.setPresenter(this);
		profilerPresenter.setController(this);
		inspectorPresenter.setController(this);
		settingsPresenter.setController(this); 
	}
	
	public void showProfile() {
		profilerPresenter.loadTempProfile();
		String nameFile = tracerPresenter.getXmlName();
		profilerPresenter.showProfile(nameFile);
		profilerPresenter.saveProfile(new File(nameFile));
	}	
	
	public void clickedOnExit() {
		System.exit(0);
	}

	public void clickedOnLoadTrace(JFrame frame) {
		inspectorPresenter.clickedOnLoadTrace(frame);
	}

	public void clickedOnSettings() {
		settingsPresenter.show();
	}
	
	public void back() {
		tracerPresenter.open();
	}
	
	public void clickedOnLoadProfile(JFrame frame) {
		profilerPresenter.clickedOnOpenProfile(frame);
	}

	public void clickedOnAbout() {
		AboutDialog aboutDialog = new AboutDialog();
		aboutDialog.setVisible(true);
	}
	
	public void clickedOnHelp(){
		
	}
	
	public void openTrace(String xmlPath) {
		inspectorPresenter.open(xmlPath);
	}
	
	// Getters and setters
	
	public TracerPresenter getTracerController() {
		return tracerPresenter;
	}

	public ProfilerPresenter getProfilerController() {
		return profilerPresenter;
	}

	public InspectorPresenter getInspectorController() {
		return inspectorPresenter;
	}

	public void setTracerController(TracerPresenter tracerController) {
		this.tracerPresenter = tracerController;
	}

	public void setProfilerController(ProfilerPresenter profilerController) {
		this.profilerPresenter = profilerController;
	}

	public void setInspectorController(InspectorPresenter inspectorController) {
		this.inspectorPresenter = inspectorController;
	}

	public ProfilerModelInterface getProfiler() {
		return profilerPresenter.getProfiler();
	}

	public void setVisible(boolean b) {
		tracerPresenter.setVisible(false);
	}

	public JFrame getView() {
		return tracerPresenter.getView();
	}

}
