/**
 * 
 */
package com.general.settings.presenter;

import java.util.ArrayList;
import java.util.List;

import com.general.model.configuration.JavaTracerConfigurationXml;
import com.general.presenter.JavaTracerPresenter;
import com.general.settings.view.SettingsView;
import com.general.settings.view.SettingsViewInterface;

public class SettingsPresenter implements SettingsPresenterInterface {

	private SettingsViewInterface view;

	private String[] excludes;
	private JavaTracerPresenter controller;
	private int numLevels;
	private int numNodes;
	
	private List<String> excludesFromConfiguration;
	
	public SettingsPresenter(){
		addExludesFromConfiguration();
		addNumLevelsFromConfiguration();
		addNumNodesFromConfiguration();
	}
	
	public void show() {
		
		if (view == null){
			view = new SettingsView();
			view.setPresenter(this);
		}
		
		view.setVisible(true); 
		view.loadExcludes(excludes);
		view.loadNumLevels(numLevels);
		view.loadNumNodes(numNodes);
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
    	int nLevels =  view.getNumLevels();
	
	  if (nLevels != -1) {
		  numLevels =nLevels;
		  int nNodes = view.getNumNodes(); 
		  if (numNodes != -1) {
			  numNodes = nNodes;
			  System.out.println("Levels: " +numLevels);
			  System.out.println("Nodes" +numNodes);
			  view.setVisible(false);	
			  controller.back();
		  }
		 
	  }
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
    
    private void addExludesFromConfiguration() {
    	try {
	        excludesFromConfiguration = JavaTracerConfigurationXml.getInstance().getExludes();
        }
        catch (Exception ex) {
	        ex.printStackTrace();
        }
    	
    	excludes = new String[excludesFromConfiguration.size()];
		for (int i=0;i<excludesFromConfiguration.size();i++){
				excludes[i] = excludesFromConfiguration.get(i);
		} 
	}
    
    private void addNumLevelsFromConfiguration() {
    	try {
    		numLevels =JavaTracerConfigurationXml.getInstance().getNumLevels();
        }
        catch (Exception ex) {
	        ex.printStackTrace();
        }
    }
    
    private void addNumNodesFromConfiguration() {
    	try {
    		numNodes =JavaTracerConfigurationXml.getInstance().getNumNodes();
        }
        catch (Exception ex) {
	        ex.printStackTrace();
        }
    }
}
