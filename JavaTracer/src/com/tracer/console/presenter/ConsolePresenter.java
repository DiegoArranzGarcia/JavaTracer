package com.tracer.console.presenter;

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
	
	public void write(String string){
		view.write(string);
		view.repaint();
	}

	public void closeStreams(){
		console.closeStreams();
	}

	public void showConsole(){
		view.setVisible(true);
	}

	public void input(String string) {
		console.input(string);
	}	

}
