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
	private JavaTracerPresenter presenter;
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
		List<String> listExcludes = new ArrayList<>();
        try {
	        listExcludes = JavaTracerConfigurationXml.getInstance().getExludesFromFile();
	        excludes = new String[listExcludes.size()];
	        for (int i=0;i<listExcludes.size();i++)
				excludes[i] =listExcludes.get(i);
	        
	        numLevels = JavaTracerConfigurationXml.getInstance().getNumLevelsFromFile();
	        numNodes = JavaTracerConfigurationXml.getInstance().getNumNodesFromFile();
	        
        }
        catch (Exception ex) {
	        ex.printStackTrace();
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
    	System.out.println("Excludes: "+excludes.length);
    	List<String >excludesAux =new ArrayList<>();
    	for(int i=0;i<excludes.length;i++) {
    		excludesAux.add(excludes[i]);
    	}
    	JavaTracerConfigurationXml.getInstance().setExcludes(excludesAux); 
		  JavaTracerConfigurationXml.getInstance().saveNewConfiguration();
		view.setVisible(false);
		presenter.back();
    }

    public void cancelActionTracer() {
    	view.setVisible(false);	    
    	presenter.back();
    }

    public void saveActionInspector() {
    	int nLevels =  view.getNumLevels();
	
	  if (nLevels != -1) {
		  numLevels =nLevels;
		  JavaTracerConfigurationXml.getInstance().setNumlevels(numLevels);
		 
      
		  int nNodes = view.getNumNodes(); 
		  if (numNodes != -1) {
			  numNodes = nNodes;
			  JavaTracerConfigurationXml.getInstance().setNumNodes(numNodes);
			  JavaTracerConfigurationXml.getInstance().saveNewConfiguration();
			  view.setVisible(false);	
			  presenter.back();
		  }
		 
	  }
    }

    public void cancelActionInspector() {
    	view.setVisible(false);	
    	presenter.back();
	    
    }
    
    public void closeWindow() {
    	presenter.back();
    }
    
    public void setController(JavaTracerPresenter javaTracerController) {
		this.presenter = javaTracerController;
	}
    
    private void addExludesFromConfiguration() {
    	try {
	        excludesFromConfiguration = JavaTracerConfigurationXml.getInstance().getExludesFromFile();
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
    		numLevels =JavaTracerConfigurationXml.getInstance().getNumLevelsFromFile();
        }
        catch (Exception ex) {
	        ex.printStackTrace();
        }
    }
    
    private void addNumNodesFromConfiguration() {
    	try {
    		numNodes =JavaTracerConfigurationXml.getInstance().getNumNodesFromFile();
        }
        catch (Exception ex) {
	        ex.printStackTrace();
        }
    }
}
