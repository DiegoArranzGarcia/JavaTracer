package com.tracer.console.presenter;

import java.io.File;

public class UpdaterSizeThread extends Thread {
	
	private static final int SLEEP_TIME = 1000;
	private SizeUpdater updater;
	private volatile boolean finished;
	private String fileName;
	
	public UpdaterSizeThread(String fileName,SizeUpdater updater){
		this.fileName = fileName;
		this.updater = updater;
		this.finished = false;
		setPriority(NORM_PRIORITY);
	}
	
	public void run() {	
		
		File f = new File(fileName);
		double size = f.length();
		while (!finished){
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (Exception e){
				e.printStackTrace();
			}
			
			double newSize = f.length();
			if (newSize != size){
				updater.updateSize(newSize);
				size = newSize;
			}
		}
		
	}

	public void finish() {
		finished = true;
	}
	

}
