/**
 * 
 */
package com.tracer.view;

import java.util.List;

import javax.swing.JFrame;

import com.tracer.presenter.TracerPresenterInterface;

public interface TracerViewInterface {

	void setController(TracerPresenterInterface tracerPresenter);
	void setVisible(boolean b);
	String getPath();
	String getMainClass();
	String getNameXml(boolean p_b);
	void enableMainClassCombo();
	void loadClasses(List<String> classes);
	void setEnableProfileAndTracer(boolean b);
	public JFrame getView() ;
	void finishedTrace(String nameXml);
    void consoleMinimize();
    void consoleMaximize();

}
