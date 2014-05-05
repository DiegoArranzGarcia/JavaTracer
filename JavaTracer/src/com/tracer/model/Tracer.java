package com.tracer.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.general.model.FileUtilities;
import com.profiler.model.ProfilerModelInterface;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.tracer.presenter.TracerPresenter;

/**
 * This program traces the execution of another program. See "java Trace -help".
 * It is a simple example of the use of the Java Debug Interface.
 * 
 * @author Robert Field
 */
public class Tracer {

	// Running remote VM
	private VirtualMachine vm;

	// Mode for tracing the Trace program (default= 0 off)
	private int debugTraceMode = VirtualMachine.TRACE_NONE;

	// Class patterns for which we don't want events

	private TracerPresenter tracerController;

	private EventThread eventThread;

	/**
	 * Parse the command line arguments. Launch target VM. Generate the trace.
	 * 
	 * @param nameXlm
	 */
	public void trace(RunConfiguration config) {

		tracerController.launching();
		vm = launchTarget(config);
		generateTrace(config, null);

	}

	public void profile(RunConfiguration config, ProfilerModelInterface profile) {
		tracerController.launching();
		vm = launchTarget(config);
		generateTrace(config, profile);
	}

	public void setController(TracerPresenter javaTracerController) {
		this.tracerController = javaTracerController;
	}

	/**
	 * Generate the trace. Enable events, start thread to display events, start
	 * threads to forward remote error and output streams, resume the remote VM,
	 * wait for the final event, and shutdown.
	 * 
	 * @param config
	 */

	void generateTrace(RunConfiguration config, ProfilerModelInterface profiler) {

		vm.setDebugTraceMode(debugTraceMode);
		eventThread = new EventThread(vm, this, config, profiler);
		eventThread.setEventRequests();
		eventThread.start();
		tracerController.redirectStreams(vm.process());
		tracerController.started();
	}

	/**
	 * Launch target VM. Forward target's output and error.
	 */
	VirtualMachine launchTarget(RunConfiguration config) {
		LaunchingConnector connector = findLaunchingConnector();
		Map<String, Connector.Argument> arguments = connectorArguments(
				connector, config);
		try {
			return connector.launch(arguments);
		} catch (IOException exc) {
			throw new Error("Unable to launch target VM: " + exc);
		} catch (IllegalConnectorArgumentsException exc) {
			throw new Error("Internal error: " + exc);
		} catch (VMStartException exc) {
			throw new Error("Target VM failed to initialize: "
					+ exc.getMessage());
		}
	}

	/**
	 * Find a com.sun.jdi.CommandLineLaunch connector
	 */
	LaunchingConnector findLaunchingConnector() {
		List<Connector> connectors = Bootstrap.virtualMachineManager()
				.allConnectors();
		for (Connector connector : connectors) {
			if (connector.name().equals("com.sun.jdi.CommandLineLaunch")) {
				return (LaunchingConnector) connector;
			}
		}
		throw new Error("No launching connector");
	}

	/**
	 * Return the launching connector's arguments.
	 */
	Map<String, Connector.Argument> connectorArguments(
			LaunchingConnector connector, RunConfiguration config) {
		Map<String, Connector.Argument> arguments = connector
				.defaultArguments();

		Connector.Argument mainArg = (Connector.Argument) arguments.get("main");
		Connector.Argument optionArg = (Connector.Argument) arguments
				.get("options");
		String options = "";
		String main = "";

		if (mainArg == null) {
			throw new Error("Bad launching connector");
		} else if (optionArg == null) {
			throw new Error("Bad launching connector");
		}

		if (config.isJar()) {
			main = "\"" + config.getMain() + "\"";
			options = "-jar";
		} else {
			main = config.getMain();
			String[] jars = config.getExternalJarPaths();
			String external_jars = "";

			for (int i = 0; i < jars.length; i++)
				external_jars += jars[i] + "\\*;";

			options = "-cp " + '"' + config.getClassPath()
					+ FileUtilities.PATH_SEPARTATOR + external_jars + "\"";
		}
		String[] args = config.getArgs();

		for (int i = 0; i < args.length; i++) {
			main += " \"" + args[i] + "\"";
		}

		mainArg.setValue(main);
		optionArg.setValue(options);

		return arguments;
	}

	public void finishedTrace() {
		tracerController.finishedTrace();
	}

	public void stopTrace() {
		eventThread.terminate();
	}

}
