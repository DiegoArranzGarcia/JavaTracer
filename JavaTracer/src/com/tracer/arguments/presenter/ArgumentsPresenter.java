package com.tracer.arguments.presenter;

import com.general.presenter.JavaTracerPresenter;
import com.tracer.arguments.view.ArgumentsView;
import com.tracer.arguments.view.ArgumentsViewInterface;

public class ArgumentsPresenter implements ArgumentsPresenterInterface{
	
	private ArgumentsViewInterface view;
	private JavaTracerPresenter presenter;

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

	public void saveAction() {
		arguments = view.getArguments();
		//view.setVisible(false);
		//presenter.back();
	}
	
	public void cancelAction() {
		view.setVisible(false);	
		presenter.back();
	}
	
	 public void setController(JavaTracerPresenter javaTracerController) {
			this.presenter = javaTracerController;
		}
	
	public String[] getArguments(){
		return arguments;
	}

	
    public void closeWindow() {
    	view.setVisible(false);	
		presenter.back();
	    
    }

}
