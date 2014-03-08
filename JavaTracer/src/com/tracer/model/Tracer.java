package com.tracer.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.console.view.console;
import com.general.model.configuration.JavaTracerConfiguration;
import com.profiler.model.Profiler;
import com.profiler.model.ProfilerModelInterface;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.tracer.controller.RunConfiguration;
import com.tracer.controller.TracerController;

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
    private boolean watchFields;

    // Class patterns for which we don't want events
    
   private String[] excludes;
   private TracerController tracerController;
   
   private static final int BUFFER_SIZE = 2048;
   
    /**
	  * Parse the command line arguments.
      * Launch target VM.
 	  * Generate the trace.
     * @param nameXlm 
	  */
    public void trace(RunConfiguration config) {

    	JavaTracerConfiguration configuration = JavaTracerConfiguration.getInstance();
    	excludes = configuration.getExcludes();	
        PrintWriter writer = new PrintWriter(System.out);
        
        if (tracerController != null)
        	tracerController.starting();
        
        vm = launchTarget(config);
        
        if (tracerController != null)
        	tracerController.generatingTrace();
        
        generateTrace(writer,config,null);
        
    }
    
    public void profile(RunConfiguration config,ProfilerModelInterface profile){
    	JavaTracerConfiguration configuration = JavaTracerConfiguration.getInstance();
    	excludes = configuration.getExcludes();	
        PrintWriter writer = new PrintWriter(System.out);
        vm = launchTarget(config);
        generateTrace(writer,config,profile);
    }
    
	public void setController(TracerController javaTracerController) {
		this.tracerController = javaTracerController;	
	}

	/**
	* Generate the trace.
	* Enable events, start thread to display events,
	* start threads to forward remote error and output streams,
	* resume the remote VM, wait for the final event, and shutdown.
	 * @param config 
	*/
    
    void generateTrace(PrintWriter writer, RunConfiguration config,ProfilerModelInterface profiler) {
        
    	vm.setDebugTraceMode(debugTraceMode);
        EventThread eventThread = new EventThread(vm,excludes,config,profiler);
        eventThread.setEventRequests(watchFields);
        eventThread.start();
        redirectOutput(config.getNameXml());
 
        // Shutdown begins when event thread terminates
        try {
        	eventThread.join();
            errThread.join(); // Make sure output is forwarded
            outThread.join(); // before we exit
        } catch (InterruptedException exc) {
            // we don't interrupt
        }
        writer.close();
        
        if (tracerController != null)
        	tracerController.finishedTrace();
    }

    /**
	* Launch target VM.
	* Forward target's output and error.
	*/
    VirtualMachine launchTarget(RunConfiguration config) {
        LaunchingConnector connector = findLaunchingConnector();
        Map<String, Connector.Argument> arguments = connectorArguments(connector, config);
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

    void redirectOutput(String nameXlm) {
    	
    	Process process = vm.process();
    	console c=new console();
    	
    	// Copy target's output and error to our output and error.
        errThread = new StreamRedirectThread("error reader", process.getErrorStream(), System.err,nameXlm,c);
        outThread = new StreamRedirectThread("output reader", process.getInputStream(), System.out,nameXlm,c);
        
    	
    	
    	errThread.start();
        outThread.start();
        
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
    Map<String, Connector.Argument> connectorArguments(LaunchingConnector connector, RunConfiguration config) { 
        Map<String, Connector.Argument> arguments = connector.defaultArguments();
        
        Connector.Argument mainArg = (Connector.Argument)arguments.get("main");
        Connector.Argument optionArg = (Connector.Argument)arguments.get("options");
        String options = "";
        String main = "";
        
        if (mainArg == null) {
            throw new Error("Bad launching connector");
        } else if (optionArg == null) {
            throw new Error("Bad launching connector");
        }
     
        
        if (config.isJar()){
        	main = "\"" + config.getMain() + "\"";
        	options = "-jar";
        } else {
            main = config.getMain();
            String[] jars = config.getExternal_jars();
            String external_jars = "";
            
        	for (int i=0;i<jars.length;i++)
        		external_jars += jars[i] + "\\*;"; 
            
        	options = "-cp " + '"' + config.getMainClassPath() + ";" + external_jars + "\"";
        }
        String[] args = config.getArgs();
        
        for (int i=0;i<args.length;i++){
        	main += " \"" + args[i] + "\"";
        }
        
        mainArg.setValue(main);
        optionArg.setValue(options);
        
        return arguments;
    }  
      
}
