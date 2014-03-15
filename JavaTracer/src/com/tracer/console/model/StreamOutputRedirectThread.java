package com.tracer.console.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import sun.org.mozilla.javascript.internal.Synchronizer;

/**
* StreamRedirectThread is a thread which copies it's input to
* it's output and terminates when it completes.
*/

class StreamOutputRedirectThread extends Thread {

    private final Reader in;
    private static final int BUFFER_SIZE = 2048;
    private Console console;
    
    /**
	 * Set up for copy.
	 * @param out Stream to copy from
	 */
        
    StreamOutputRedirectThread(Console console,InputStream out) {
    	this.console = console;
        this.in = new InputStreamReader(out);
        setPriority(Thread.MAX_PRIORITY-1);
    }
    
    public void run() {
    	
        try {
            char[] cbuf = new char[BUFFER_SIZE];
            int count;            
            while ((count = in.read(cbuf, 0, BUFFER_SIZE)) >= 0) {
            	String string = new String(cbuf,0,count);
            	synchronized (console) {
            		 console.write(this,string); 
          	  }
                         
            }             
        } catch(IOException exc) {
            System.err.println("Child I/O Transfer - " + exc);
        }
    }

	
}