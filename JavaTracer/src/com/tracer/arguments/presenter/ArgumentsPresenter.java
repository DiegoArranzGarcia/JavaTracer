package com.tracer.arguments.presenter;

import com.tracer.arguments.view.ArgumentsView;
import com.tracer.arguments.view.ArgumentsViewInterface;
import com.tracer.presenter.TracerPresenter;

public class ArgumentsPresenter implements ArgumentsPresenterInterface{
	
	private ArgumentsViewInterface view;
	private TracerPresenter presenter;

	private String[] arguments;
	
	public ArgumentsPresenter(){
		arguments = new String[0];
	}
	
	public void show() {
		if (view == null){
			view = new ArgumentsView(presenter.getView());
			view.setPresenter(this);
		}
		
		view.setVisible(true); 
		view.loadArguments(arguments);
	}

	public void saveAction() {
		arguments = view.getArguments();
		view.setVisible(false);
		//presenter.back();
	}
	
	public void cancelAction() {
		view.setVisible(false);	
	}
	
	 public void setController(TracerPresenter tracerController) {
			this.presenter = tracerController;
		}
	
	public String[] getArguments(){
		return arguments;
	}

	
    public void closeWindow() {
    	view.setVisible(false);		    
    }

}
