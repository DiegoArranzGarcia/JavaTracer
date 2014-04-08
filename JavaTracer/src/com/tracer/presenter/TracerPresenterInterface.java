
package com.tracer.presenter;

import java.io.File;

public interface TracerPresenterInterface {

	public void open(String xmlPath);
	public void clickedOnAbout();
	public void clickOnSettings();
	public void clickedOnLoadProfile();
	public void clickedOnLoadTrace();
	public void clickedOnProfile();
	public  void clickedOnTrace();
	public void clickedExit();
	public void editArguments();
	public boolean isExecutableJar(File file);
	public void selectedPath(String file_selected);

}
