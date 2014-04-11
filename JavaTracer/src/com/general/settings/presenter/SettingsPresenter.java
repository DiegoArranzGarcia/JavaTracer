/**
 * 
 */
package com.general.settings.presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.general.model.FileUtilities;
import com.general.presenter.JavaTracerPresenter;
import com.general.settings.model.Settings;
import com.general.settings.view.SettingsView;
import com.general.settings.view.SettingsViewInterface;

public class SettingsPresenter implements SettingsPresenterInterface {

	/*
	 * General
	 */
	private SettingsViewInterface view;
	private JavaTracerPresenter presenter;

	/*
	 * Tracer
	 */
	private String[] excludes;
	private List<String> excludesFromConfiguration;
	private boolean excludedThis;
	private boolean excludedLibraries;
	private boolean excludedDataStructure;
	
	/*
	 * Display-tree
	 */
	private boolean unlimitedLevels;
	private boolean unlimitedNodes;
	private int numLevels;
	private int numNodes;


	public SettingsPresenter(){
		addExludesFromConfiguration();
		addExcludedThisFromConfiguration();
		addExcludedLibrariesFromConfiguration();
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
			Settings configuration = Settings.getInstance();
			listExcludes = configuration.getExcludesList();
			excludes = new String[listExcludes.size()];
			for (int i=0;i<listExcludes.size();i++)
				excludes[i] =listExcludes.get(i);
			
			excludedThis = configuration.isExcludedThis();
			excludedLibraries = configuration.isExcludedLibrary();
			excludedDataStructure = configuration.isExcludedDataStructure();
			unlimitedLevels = configuration.isUnlimitedLevels();
			unlimitedNodes = configuration.isUnlimitedNodes(); 
			numLevels = configuration.getNumLevels();
			numNodes = configuration.getNumNodes();

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		view.setVisible(true); 

		view.removeComboboxConfig();
		
		view.loadExcludes(excludes);
		view.loadExcludedThis(excludedThis);
		view.loadExcludedLibraries(excludedLibraries);
		view.loadExcludedDataStructure(excludedDataStructure); 
		
		view.loadUnlimitedLevels(unlimitedLevels);
		view.loadUnlimitedNodes(unlimitedNodes);
		view.loadNumLevels(numLevels);
		view.loadNumNodes(numNodes);
		
		loadXmls();
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

	
	public void loadConfiguration(String nameXML) {
		
		Settings configuration = Settings.getInstance(); 
		configuration.loadFromFile(nameXML); 
		
		addExludesFromConfiguration();
		addExcludedThisFromConfiguration();
		addExcludedLibrariesFromConfiguration();
		addExcludedDataStructureFromConfiguration();
		addUnlimitedLevelsFromConfiguration();
		addNumLevelsFromConfiguration();
		addNumNodesFromConfiguration();
		addUnlimitedNodesFromConfiguration();
		
		view.loadExcludes(excludes);
		view.loadExcludedThis(excludedThis);
		view.loadExcludedLibraries(excludedLibraries);
		view.loadExcludedDataStructure(excludedDataStructure);
		view.loadUnlimitedLevels(unlimitedLevels);
		view.loadUnlimitedNodes(unlimitedNodes);
		view.loadNumLevels(numLevels);
		view.loadNumNodes(numNodes);
	}
	
	public void saveActionTracer() {

		excludes = view.getExcludes();
		List<String >excludesAux =new ArrayList<>();
		for(int i=0;i<excludes.length;i++) {
			excludesAux.add(excludes[i]);
		}
		
		excludedThis =view.getExcludedThis();
		Settings configuration = Settings.getInstance();
		configuration.setExcludes(excludesAux); 
		configuration.setExcludedThis(excludedThis);
		excludedLibraries = view.getExcludedLibraries();
		excludedDataStructure =view.getExcludedDataStructure();
		configuration.setExcludedDataStructure(excludedDataStructure);
		configuration.setExcludedLibrary(excludedLibraries);
		configuration.saveConfiguration(view.getSelectedCombobox());

	}
	
	public void saveActionInspector() {
		int nLevels =  view.getNumLevels();

		if (nLevels != -1) {
			numLevels =nLevels;
			Settings configuration = Settings.getInstance();
			configuration.setNumlevels(numLevels);

			int nNodes = view.getNumNodes(); 
			if (numNodes != -1) {
				numNodes = nNodes;
				configuration.setNumNodes(numNodes);
				unlimitedLevels = view.getUnlimitedLevels();
				configuration.setUnlimitedLevels(unlimitedLevels) ;
				unlimitedNodes = view.getUnlimitedNodes();
				configuration.setUnlimitedNodes(unlimitedNodes);
				
				configuration.saveConfiguration(view.getSelectedCombobox());
			}

		}
	}

	public void loadXmls() {
		File[] xmlFiles = Settings.getInstance().getFolderCofig().listFiles();

		List<String> xmlOnlyNames = new ArrayList<>();
	
		for(int i=0;i<xmlFiles.length;i++) {
			String extension = FileUtilities.getExtension(xmlFiles[i]);
			if (extension.equals("xml")) {
				String onlyName = FileUtilities.getOnlyName(xmlFiles[i].getName());
				xmlOnlyNames.add(onlyName);
			}
		}
		view.loadAllXmls(xmlOnlyNames);
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
			excludesFromConfiguration = Settings.getInstance().getExcludesList();
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
			excludedThis =Settings.getInstance().isExcludedThis();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addExcludedLibrariesFromConfiguration() {
		try {
			excludedLibraries =Settings.getInstance().isExcludedLibrary();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addUnlimitedLevelsFromConfiguration() {
		try {
			unlimitedLevels =Settings.getInstance().isUnlimitedLevels();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addExcludedDataStructureFromConfiguration() {
		try {
			excludedDataStructure =Settings.getInstance().isUnlimitedLevels();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addUnlimitedNodesFromConfiguration() {
		try {
			unlimitedNodes =Settings.getInstance().isUnlimitedLevels();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void addNumLevelsFromConfiguration() {
		try {
			numLevels =Settings.getInstance().getNumLevels();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void addNumNodesFromConfiguration() {
		try {
			numNodes =Settings.getInstance().getNumNodes();
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

	public boolean isExcludedLibraries() {
	    return excludedLibraries;
    }

	public void setExcludedLibraries(boolean excludedLibraries) {
	    this.excludedLibraries = excludedLibraries;
    }



}
