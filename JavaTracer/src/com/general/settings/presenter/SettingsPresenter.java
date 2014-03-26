/**
 * 
 */
package com.general.settings.presenter;

import java.util.ArrayList;
import java.util.List;

import com.general.model.configuration.JavaTracerConfiguration;
import com.general.presenter.JavaTracerPresenter;
import com.general.settings.view.SettingsView;
import com.general.settings.view.SettingsViewInterface;

public class SettingsPresenter implements SettingsPresenterInterface {

	private SettingsViewInterface view;
	private JavaTracerPresenter presenter;

	private String[] excludes;
	private boolean excludedThis;
	private boolean excludedDataStructure;
	private boolean unlimitedLevels;
	private boolean unlimitedNodes;
	private int numLevels;
	private int numNodes;
	

	private List<String> excludesFromConfiguration;

	public SettingsPresenter(){
		addExludesFromConfiguration();
		addExcludedThisFromConfiguration();
		addExcludedDataStructureFromConfiguration();
		addUnlimitedLevelsFromConfiguration();
		addNumLevelsFromConfiguration();
		addNumNodesFromConfiguration();
		addUnlimitedNodesFromConfiguration();
		
	}

	public void show() {

		if (view == null){
			view = new SettingsView();
			view.setPresenter(this);
		}
		List<String> listExcludes = new ArrayList<>();
		try {
			JavaTracerConfiguration configuration = JavaTracerConfiguration.getInstance();
			listExcludes = configuration.getExludesFromFile();
			excludes = new String[listExcludes.size()];
			for (int i=0;i<listExcludes.size();i++)
				excludes[i] =listExcludes.get(i);
			
			excludedThis = configuration.getExcludedThisFromFile();
			excludedDataStructure = configuration.getExcludedDataStructureFromFile();
			unlimitedLevels = configuration.getUnlimitedLevelsFromFile();
			unlimitedNodes = configuration.getUnlimitedNodesFromFile(); 
			numLevels = configuration.getNumLevelsFromFile();
			numNodes = configuration.getNumNodesFromFile();

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		view.setVisible(true); 
		view.loadExcludes(excludes);
		view.loadExcludedThis(excludedThis);
		view.loadExcludedDataStructure(excludedDataStructure); 
		
		view.loadUnlimitedLevels(unlimitedLevels);
		view.loadUnlimitedNodes(unlimitedNodes);
		view.loadNumLevels(numLevels);
		view.loadNumNodes(numNodes);
	}


	public String[] getExcludes(){
		return excludes;
	}

	public void save() {
		saveActionTracer();
		saveActionInspector();
		view.setVisible(false);	
		presenter.back();

	}

	public void saveActionTracer() {

		excludes = view.getExcludes();
		List<String >excludesAux =new ArrayList<>();
		for(int i=0;i<excludes.length;i++) {
			excludesAux.add(excludes[i]);
		}
		
		excludedThis =view.getExcludedThis();
		JavaTracerConfiguration configuration = JavaTracerConfiguration.getInstance();
		configuration.setExcludes(excludesAux); 
		configuration.setExcludedThis(excludedThis);
		excludedDataStructure =view.getExcludedDataStructure();
		configuration.setExcludedDataStructure(excludedDataStructure);
		configuration.saveConfiguration();

	}
	
	public void saveActionInspector() {
		int nLevels =  view.getNumLevels();

		if (nLevels != -1) {
			numLevels =nLevels;
			JavaTracerConfiguration configuration = JavaTracerConfiguration.getInstance();
			configuration.setNumlevels(numLevels);

			int nNodes = view.getNumNodes(); 
			if (numNodes != -1) {
				numNodes = nNodes;
				configuration.setNumNodes(numNodes);
				unlimitedLevels = view.getUnlimitedLevels();
				configuration.setUnlimitedLevels(unlimitedLevels) ;
				unlimitedNodes = view.getUnlimitedNodes();
				configuration.setUnlimitedNodes(unlimitedNodes);
				
				configuration.saveConfiguration();
			}

		}
	}

	public void cancelAction() {
		view.setVisible(false);	    
		presenter.back();
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
			excludesFromConfiguration = JavaTracerConfiguration.getInstance().getExludesFromFile();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		excludes = new String[excludesFromConfiguration.size()];
		for (int i=0;i<excludesFromConfiguration.size();i++){
			excludes[i] = excludesFromConfiguration.get(i);
		} 
	}
	
	public void addExcludedThisFromConfiguration() {
		try {
			excludedThis =JavaTracerConfiguration.getInstance().getExcludedThisFromFile();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addUnlimitedLevelsFromConfiguration() {
		try {
			unlimitedLevels =JavaTracerConfiguration.getInstance().getUnlimitedLevelsFromFile();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addExcludedDataStructureFromConfiguration() {
		try {
			excludedDataStructure =JavaTracerConfiguration.getInstance().getUnlimitedLevelsFromFile();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addUnlimitedNodesFromConfiguration() {
		try {
			unlimitedNodes =JavaTracerConfiguration.getInstance().getUnlimitedLevelsFromFile();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void addNumLevelsFromConfiguration() {
		try {
			numLevels =JavaTracerConfiguration.getInstance().getNumLevelsFromFile();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void addNumNodesFromConfiguration() {
		try {
			numNodes =JavaTracerConfiguration.getInstance().getNumNodesFromFile();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isExcludedThis() {
	    return excludedThis;
    }

	public void setExcludedThis(boolean excludedThis) {
	    this.excludedThis = excludedThis;
    }

	public boolean isUnlimited() {
	    return unlimitedLevels;
    }

	public void setUnlimited(boolean unlimited) {
	   this. unlimitedLevels = unlimited;
    }

	public boolean isExcludedDataStructure() {
	    return excludedDataStructure;
    }

	public void setExcludedDataStructure(boolean excludedDataStructure) {
	   this. excludedDataStructure = excludedDataStructure;
    }



}
