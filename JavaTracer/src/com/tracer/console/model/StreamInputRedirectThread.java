package com.tracer.console.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
* StreamRedirectThread is a thread which copies it's input to
* it's output and terminates when it completes.
*/

class StreamInputRedirect {

    private OutputStream out;
    private static final int BUFFER_SIZE = 2048;
    private Console console;
    
    /**
	 * Set up for copy.
	 * @param in Stream to copy from
	 * @param in Stream to copy to
	 */
        
    StreamInputRedirect(Console console,OutputStream out) {
    	this.console = console;
        this.out = out;
    }
    
    public void write(String s){
        try {
            s += "\n";
            out.write(s.getBytes());
            out.flush();
        } catch(IOException exc) {
            System.err.println("Child I/O Transfer - " + exc);
        }
    }

	
}