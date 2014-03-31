/**
 * 
 */
package com.general.settings.view;

import com.general.settings.presenter.SettingsPresenterInterface;

public interface SettingsViewInterface {
	
	public void loadExcludes(String[] args); 
	public void loadExcludedThis(boolean excludedThis);
	public void loadExcludedLibraries(boolean excludedLibraries);
	public void loadExcludedDataStructure(boolean excludedDataStructure);
	public  void loadUnlimitedLevels(boolean unlimited);
	public void loadUnlimitedNodes(boolean unlimited);
	public void loadNumLevels(int numLevels);
	public void loadNumNodes(int numNodes);
	public String[] getExcludes();
	public boolean getExcludedThis();
	public boolean getExcludedLibraries();
	public boolean getExcludedDataStructure();
	public boolean getUnlimitedLevels();
	public boolean getUnlimitedNodes();
	public int getNumLevels();
	public int getNumNodes();
	public void setVisible(boolean visible);
	public void setPresenter(SettingsPresenterInterface presenter);

}
