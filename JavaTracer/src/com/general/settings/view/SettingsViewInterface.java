/**
 * 
 */
package com.general.settings.view;

import com.general.settings.presenter.SettingsPresenterInterface;

public interface SettingsViewInterface {
	
	public void loadExcludes(String[] args); 
	public void loadExcludedThis(boolean excludedThis);
	public  void loadUnlimited(boolean unlimited);
	public void loadNumLevels(int numLevels);
	public void loadNumNodes(int numNodes);
	public String[] getExcludes();
	public boolean getExcludedThis();
	public boolean getUnlimited();
	public int getNumLevels();
	public int getNumNodes();
	public void setVisible(boolean visible);
	public void setPresenter(SettingsPresenterInterface presenter);

}
