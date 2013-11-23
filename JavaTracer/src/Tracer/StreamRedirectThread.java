package Tracer;

import java.io.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
* StreamRedirectThread is a thread which copies it's input to
* it's output and terminates when it completes.
*/

class StreamRedirectThread extends Thread {

    private final Reader in;
    private final Writer out;

    private static final int BUFFER_SIZE = 2048;

    /**
	 * Set up for copy.
	 * @param name Name of the thread
	 * @param in Stream to copy from
	 * @param out Stream to copy to
	 */
    
    StreamRedirectThread(String name, InputStream in, OutputStream out) {
        super(name);
        this.in = new InputStreamReader(in);
        this.out = new OutputStreamWriter(out);
        setPriority(Thread.MAX_PRIORITY-1);
    }

    /**
     * Copy.
     */
    
    public void run() {
        try {
            char[] cbuf = new char[BUFFER_SIZE];
            int count;
            String error="";
            int i=0;
            while ((count = in.read(cbuf, 0, BUFFER_SIZE)) >= 0) {
                out.write(cbuf, 0, count);
            }
            
            while(i<BUFFER_SIZE)
            {
             error=error+cbuf[i];
             i++;
            }
            
            ShowError(error);
            out.flush();
          
        } catch(IOException exc) {
            System.err.println("Child I/O Transfer - " + exc);
        }
    }

	private void ShowError(String error) {
		
		String print="";
		String errorMain="no se ha encontrado el método principal";
        String errorLoadClass="no se ha encontrado o cargado la clase principal";
		
		if(error.contains(errorMain))
			print="This class must contain the method: public static void main(String[] args)";
		
		if(error.contains(errorLoadClass))
			print="was not found or loaded main class";
        
		JOptionPane.showMessageDialog(new JFrame(), print);
		
		
	}
}