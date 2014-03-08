/**
 * 
 */
package com.general.settings.presenter;

import com.general.presenter.JavaTracerPresenter;
import com.general.settings.view.SettingsView;
import com.general.settings.view.SettingsViewInterface;

public class SettingsPresenter implements SettingsPresenterInterface {

	private SettingsViewInterface view;

	private String[] excludes;
	private JavaTracerPresenter controller;
	
	public SettingsPresenter(){
		excludes = new String[0];
	}
	
	public void show() {
		
		if (view == null){
			view = new SettingsView();
			view.setPresenter(this);
		}
		
		view.setVisible(true); 
		view.loadExcludes(excludes);
	}

	
	public String[] getExcludes(){
		return excludes;
	}

    public void saveActionTracer() {
    	excludes = view.getExcludes();
		view.setVisible(false);
		controller.back();
    }

    public void cancelActionTracer() {
    	view.setVisible(false);	    
    	controller.back();
    }

    public void saveActionInspector() {
	    // TODO Auto-generated method stub
	    
    }

    public void cancelActionInspector() {
    	view.setVisible(false);	
    	controller.back();
	    
    }
    
    public void closeWindow() {
    	controller.back();
    }
    
    public void setController(JavaTracerPresenter javaTracerController) {
		this.controller = javaTracerController;
	}
}
