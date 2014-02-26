package com.javatracer.arguments.presenter;

import com.javatracer.arguments.view.ArgumentsView;
import com.javatracer.arguments.view.ArgumentsViewInterface;
import com.javatracer.controller.TracerController;

public class ArgumentsPresenter implements ArgumentsPresenterInterface{
	
	private TracerController tracerPresenter;
	private ArgumentsViewInterface view;

	private String[] arguments;
	
	public ArgumentsPresenter(){
		arguments = new String[0];
	}
	
	public void show() {
		
		if (view == null){
			view = new ArgumentsView();
			view.setPresenter(this);
		}
		
		view.setVisible(true); 
		view.loadArguments(arguments);
	}

	public void setPresenter(TracerController tracerController) {
		this.tracerPresenter = tracerController;
	}

	public void clickedOnSave() {
		arguments = view.getArguments();
		view.setVisible(false);
	}
	
	public String[] getArguments(){
		return arguments;
	}

}
