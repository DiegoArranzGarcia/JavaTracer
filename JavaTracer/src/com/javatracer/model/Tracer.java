package com.javatracer.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.javatracer.controller.TracerController;
import com.javatracer.model.configuration.Configuration;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;

/**
* This program traces the execution of another program.
* See "java Trace -help".
* It is a simple example of the use of the Java Debug Interface.
*
* @author Robert Field
*/
public class Tracer {

    // Running remote VM
    private VirtualMachine vm;

    // Thread transferring remote error stream to our error stream
    private Thread errThread = null;

    // Thread transferring remote output stream to our output stream
    private Thread outThread = null;

    // Mode for tracing the Trace program (default= 0 off)
    private int debugTraceMode = VirtualMachine.TRACE_NONE;

    // Do we want to watch assignments to fields
    private boolean watchFields,wasError = false;

    // Class patterns for which we don't want events
    
   private String[] excludes;
    
	private TracerController tracerController;
   
    private static final int BUFFER_SIZE = 2048;
    
    public Tracer(TracerController tracerController) {
		this.tracerController = tracerController;
  	}


    /**
	  * Parse the command line arguments.
      * Launch target VM.
 	  * Generate the trace.
     * @param nameXlm 
	  */
    public void trace(String[] args, String nameXlm) {

    	Configuration configuration = new Configuration();
    	excludes=configuration.getExcludes();
    		
        PrintWriter writer = new PrintWriter(System.out);
        
        int inx;
        for (inx = 0; inx < args.length; ++inx) {
            String arg = args[inx];
            if (arg.charAt(0) != '-') {
                break;
            }
            if (arg.equals("-output")) {
                try {
                    writer = new PrintWriter(new FileWriter(args[++inx]));
                } catch (IOException exc) {
                    System.err.println("Cannot open output file: " + args[inx]
                                       + " - " + exc);
                    System.exit(1);
                }
            } else if (arg.equals("-all")) {
                excludes = new String[0];
            } else if (arg.equals("-fields")) {
                watchFields = true;
            } else if (arg.equals("-dbgtrace")) {
                debugTraceMode = Integer.parseInt(args[++inx]);
            } else if (arg.equals("-help")) {
                usage();
                System.exit(0);
            } else {
                System.err.println("No option: " + arg);
                usage();
                System.exit(1);
            }
        }
        if (inx >= args.length) {
            System.err.println("<class> missing");
            usage();
            System.exit(1);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(args[inx]);
        for (++inx; inx < args.length; ++inx) {
            sb.append(' ');
            sb.append(args[inx]);
        }

        vm = launchTarget(args);
        generateTrace(writer,nameXlm);
    }

	/**
	* Generate the trace.
	* Enable events, start thread to display events,
	* start threads to forward remote error and output streams,
	* resume the remote VM, wait for the final event, and shutdown.
	 * @param nameXlm 
	*/
    
    void generateTrace(PrintWriter writer, String nameXlm) {
        
    	vm.setDebugTraceMode(debugTraceMode);
        EventThread eventThread = new EventThread(vm, excludes,nameXlm,false);
        eventThread.setEventRequests(watchFields);
        eventThread.start();
        redirectOutput();
 
        // Shutdown begins when event thread terminates
        try {
        	eventThread.join();
            errThread.join(); // Make sure output is forwarded
            outThread.join(); // before we exit
        } catch (InterruptedException exc) {
            // we don't interrupt
        }
        writer.close();
        tracerController.finishedTrace(nameXlm);
    }

    /**
	* Launch target VM.
	* Forward target's output and error.
	*/
    VirtualMachine launchTarget(String[] mainArgs) {
        LaunchingConnector connector = findLaunchingConnector();
        Map<String, Connector.Argument> arguments = connectorArguments(connector, mainArgs);
        try {
            return connector.launch(arguments);
        } catch (IOException exc) {
            throw new Error("Unable to launch target VM: " + exc);
        } catch (IllegalConnectorArgumentsException exc) {
            throw new Error("Internal error: " + exc);
        } catch (VMStartException exc) {
            throw new Error("Target VM failed to initialize: " + exc.getMessage());
        }
    }

    void redirectOutput() {
    	
    	Process process = vm.process();
        
        // Copy target's output and error to our output and error.
        errThread = new StreamRedirectThread("error reader", process.getErrorStream(), System.err);
        outThread = new StreamRedirectThread("output reader", process.getInputStream(), System.out);
       
        catchError(process);
        
        errThread.start();
        outThread.start();
        
        catchError(process);
        
    }

    /**
	* Find a com.sun.jdi.CommandLineLaunch connector
	*/
    LaunchingConnector findLaunchingConnector() {
        List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
        for (Connector connector : connectors) {
            if (connector.name().equals("com.sun.jdi.CommandLineLaunch")) {
                return (LaunchingConnector)connector;
            }
        }
        throw new Error("No launching connector");
    }

    /**
	* Return the launching connector's arguments.
	*/
    Map<String, Connector.Argument> connectorArguments(LaunchingConnector connector, String[] mainArgs) { 
        Map<String, Connector.Argument> arguments = connector.defaultArguments();
        Connector.Argument mainArg = (Connector.Argument)arguments.get("main");
        if (mainArg == null) {
            throw new Error("Bad launching connector");
        }
        mainArg.setValue(mainArgs[1]);

        if (true) {
            // We need a VM that supports watchpoints
            Connector.Argument optionArg =
                (Connector.Argument)arguments.get("options");
            if (optionArg == null) {
                throw new Error("Bad launching connector");
            }
            String optionValue = "-cp " + '"' + mainArgs[0] + ";" + mainArgs[2] + "\"";
            //String optionValue = "-cp " + '"' + mainArgs[0] + ";C:\\Users\\Diego\\Documents\\GitHub\\JavaTracer\\ProyectoConJar\\lib\\*\"";
            optionArg.setValue(optionValue);
            //optionArg.setValue("-classic");
        }
        return arguments;
    }

    /**
	* Print command line usage help
	*/
    
    void usage() {
        System.err.println("Usage: java Trace <options> <class> <args>");
        System.err.println("<options> are:");
        System.err.println(" -output <filename> Output trace to <filename>");
        System.err.println(" -all Include system classes in output");
        System.err.println(" -help Print this help message");
        System.err.println("<class> is the program to trace");
        System.err.println("<args> are the arguments to <class>");
    }
	
    private void ShowError(String error) {
		
		
		String errorMain="no se ha encontrado el m�todo principal";
        String errorLoadClass="no se ha encontrado o cargado la clase principal";
		
		if(error.contains(errorMain)){
			tracerController.showErrorMain();
			wasError=true;}
		
		if(error.contains(errorLoadClass)){
			tracerController.showErrorLoadClass();
		    wasError=true;	
		 }
    }
    
   private void catchError(Process process)
   {
	   
	   Reader in = new InputStreamReader( process.getErrorStream());
       Writer out = new OutputStreamWriter(System.err);
         
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
   
   
   public boolean getWasError()
   {
	   return wasError;
	   
   }
   
}
