package com.javatracer.model;

import java.io.*;

/**
* StreamRedirectThread is a thread which copies it's input to
* it's output and terminates when it completes.
*/

class StreamRedirectThread extends Thread {

    private final Reader in;
    private final Writer out;
    private static final int BUFFER_SIZE = 2048;
    private static String FILE_OUTPUT_NAME = "output.txt";

    /**
	 * Set up for copy.
	 * @param name Name of the thread
	 * @param in Stream to copy from
	 * @param out Stream to copy to
     * @param nameXlm 
     * @param wp 
	 */
    
   /* StreamRedirectThread(String name, InputStream in, OutputStream out) {
        super(name);
        this.in = new InputStreamReader(in);
        this.out = new OutputStreamWriter(out);
        setPriority(Thread.MAX_PRIORITY-1);
    }*/
    
    StreamRedirectThread(String name, InputStream in, OutputStream out, String nameXml) {
        super(name);
        this.in = new InputStreamReader(in);
        this.out = new OutputStreamWriter(out);
      
		PrintStream g = null;
        try {
	        g = new PrintStream(new File(nameXml+"_"+FILE_OUTPUT_NAME));
        }
        catch (FileNotFoundException ex) {
	        ex.printStackTrace();
        }
		System.setOut(g );
        setPriority(Thread.MAX_PRIORITY-1);
    }


    /**
     * Copy.
     */
    
    public void run() {
        try {
            char[] cbuf = new char[BUFFER_SIZE];
            int count;
            while ((count = in.read(cbuf, 0, BUFFER_SIZE)) >= 0) {
                out.write(cbuf, 0, count);
            }
            
          out.flush();
          
        } catch(IOException exc) {
            System.err.println("Child I/O Transfer - " + exc);
        }
    }

	
}