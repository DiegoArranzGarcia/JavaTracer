package com.tracer.console.model;

import com.tracer.console.presenter.ConsolePresenterInterface;

public class Console {
	
	private ConsolePresenterInterface presenter;

	private StreamOutputRedirectThread out;
	private StreamOutputRedirectThread err;
	private StreamInputRedirect in;
	
	
	public void redirectProcessStrems(Process process){
		//redirect System.out
		out = new StreamOutputRedirectThread(this,process.getInputStream());
		//redirect System.err
		err = new StreamOutputRedirectThread(this,process.getErrorStream());
		//redirect System.in
		in = new StreamInputRedirect(this,process.getOutputStream());

		out.start();
		err.start();
	}
	
	public void closeStreams() {
		try {
			out.join();
			err.join();
			in = null;
		} catch (InterruptedException e) {
			
		}
	}
	
	public void setPresenter(ConsolePresenterInterface presenter){
		this.presenter = presenter;
	}

	public void input(String string){
		if (in != null)
			in.write(string);
	}

	public void write(StreamOutputRedirectThread thread,String string) {
		if (thread.equals(out)){
			presenter.writeOutput(string);
		} else if (thread.equals(err)){
			presenter.writeError(string);
		}
	}

}
