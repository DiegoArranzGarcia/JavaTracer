package com.tracer.console.presenter;

import javax.swing.JComponent;

import com.tracer.console.model.Console;
import com.tracer.console.view.ConsoleView;

public class ConsolePresenter implements ConsolePresenterInterface,SizeUpdater{
	
	private Console console;
	private ConsoleView view;
	private UpdaterSizeThread updater;
	
	private String lastFileName;
	
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

	public void resetConsole() {
		view.clear();
		view.setEditable(true);
	}

	public void launching() {
		view.launching();
		updater = new UpdaterSizeThread(lastFileName,this);
		updater.start();
	}

	public void tracing() {
		view.tracing();
	}
	
	public void profiling() {
		view.profiling();
	}
	
	public void finished(){
		view.finished();
		updater.finish();
	}

	public void setLastFileName(String lastFilename){
		this.lastFileName = lastFilename;
	}

	public void updateSize(double size) {
		view.updateSize(lastFileName,size);
	}

}
