package com.javatracer.arguments.view;

import com.javatracer.arguments.presenter.ArgumentsPresenterInterface;

public interface ArgumentsViewInterface {

	public void loadArguments(String[] args); 
	public String[] getArguments();
	public void setVisible(boolean visible);
	public void setPresenter(ArgumentsPresenterInterface presenter);
	
}
