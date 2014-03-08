/**
 * 
 */
package com.general.settings.view;

import com.general.settings.presenter.SettingsPresenterInterface;

public interface SettingsViewInterface {
	
	public void loadExcludes(String[] args); 
	public String[] getExcludes();
	public void setVisible(boolean visible);
	public void setPresenter(SettingsPresenterInterface presenter);

}
