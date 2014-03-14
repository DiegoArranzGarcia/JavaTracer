package com.tracer.console.presenter;

import javax.swing.JComponent;

import com.tracer.console.model.Console;
import com.tracer.console.view.ConsoleView;

public class ConsolePresenter implements ConsolePresenterInterface{
	
	private Console console;
	private ConsoleView view;
	
	public ConsolePresenter(){
		this.console = new Console();
		this.view = new ConsoleView();
		console.setPresenter(this);
		view.setPresenter(this);
	}

	public void redirectStreams(Process process) {
		console.redirectProcessStrems(process);
	}
	
	public void writeOutput(String string){
		view.writeOutput(string);
	}
	
	public void writeError(String string){
		view.writeError(string);
	}

	public void closeStreams(){
		console.closeStreams();
		view.setEditable(false);
	}

	public void input(String string) {
		console.input(string);
	}

	public JComponent getConsole() {
		return view;
	}	

}
