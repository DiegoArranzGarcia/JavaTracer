package com.tracer.model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.general.model.configuration.JavaTracerConfiguration;
import com.general.model.data.ThreadInfo;
import com.profiler.model.ProfilerModelInterface;
import com.profiler.model.data.ExcludedClassesMethods;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ThreadDeathRequest;
import com.tracer.controller.RunConfiguration;
import com.tracer.model.managers.VMDeathManager;
import com.tracer.model.managers.DisconnectManager;
import com.tracer.model.managers.ExceptionManager;
import com.tracer.model.managers.MethodEntryManager;
import com.tracer.model.managers.MethodExitManager;
import com.tracer.model.managers.ThreadDeathManager;
import com.tracer.model.writers.TraceWriter;


public class EventThread extends Thread {

    private final VirtualMachine vm; // Running VM
    private boolean connected = true; // Connected to VM
    private boolean enableProfiling;
	private boolean vmDied = false; // VMDeath occurred
	
    // Maps ThreadReference to ThreadTrace instances
    private Map<ThreadReference, ThreadTrace> traceMap = new HashMap<>();
       
    //Managers
    private VMDeathManager vmDeathManager;
    private DisconnectManager disconnect;
    private ExceptionManager exceptionManager;
    private ThreadDeathManager threadeath;
    //private FieldWatchManager fieldwatch;
    private MethodEntryManager methodEntryManager;
    private MethodExitManager methodExitManager;
    //private StepManager step;
    //private PrepareManager prepare;
    private TraceWriter writer;
    
    private ProfilerModelInterface profiler;
    private Tracer tracer;
    
    private List<String>  excludes; // Packages to exclude
    private ExcludedClassesMethods excludesClassMethods;

    public EventThread(VirtualMachine vm, Tracer tracer, RunConfiguration config, ProfilerModelInterface profiler){
        super("event-handler");
        this.vm = vm;
        this.profiler = profiler;
        this.tracer = tracer;
        
        this.excludes = JavaTracerConfiguration.getInstance().getExludesFromFile();
        this.excludesClassMethods = JavaTracerConfiguration.getInstance().getExcludeClassMethods();
        
        this.enableProfiling = config.isProfiling_mode();
        
        if (profiler != null) {
        	profiler.clean();
        } else {
        	writer = new TraceWriter(config.getNameXml());
        	writer.writeThreadInfo(new ThreadInfo());
        }  
        
        ClassUtils utils = new ClassUtils(excludes);
        
        disconnect = new DisconnectManager();
        threadeath = new ThreadDeathManager(traceMap);
        vmDeathManager = new VMDeathManager(writer);
        methodEntryManager = new MethodEntryManager(writer,utils);
        methodExitManager = new MethodExitManager(writer,utils);
		exceptionManager = new ExceptionManager(writer,utils);
		
    }

    /**
	* Run the event handling thread.
	* As long as we are connected, get event sets off
	* the queue and dispatch the events within them.
	*/

    public void run() {
        EventQueue queue = vm.eventQueue();
                
        while (connected) {
            try {
            	
                 EventSet eventSet = queue.remove();
                 EventIterator it = eventSet.eventIterator();
                 while (it.hasNext())
                	 handleEvent(it.nextEvent());
                 eventSet.resume();
                 
            } catch (InterruptedException exc) {
               exc.printStackTrace();
            } catch (VMDisconnectedException discExc) {
            	discExc.printStackTrace();
                handleDisconnectedException();
                break;
            }
        }
    }

    /**
	* Create the desired event requests, and enable
	* them so that we will get events.
	* @param excludes Class patterns for which we don't want events
	* @param watchFields Do we want to watch assignments to fields
	*/
    
    void setEventRequests(boolean watchFields) {
            
    	EventRequestManager mgr = vm.eventRequestManager();
        // want all exceptions
        ExceptionRequest excReq = mgr.createExceptionRequest(null,true, true);
        // suspend so we can step
        excReq.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        excReq.enable();

        MethodEntryRequest menr = mgr.createMethodEntryRequest();
        for (int i=0; i<excludes.size(); ++i) {
            menr.addClassExclusionFilter(excludes.get(i));
        }
        menr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        menr.enable();

        MethodExitRequest mexr = mgr.createMethodExitRequest();
        for (int i=0; i<excludes.size(); ++i) {
            mexr.addClassExclusionFilter(excludes.get(i));
        }
        mexr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        mexr.enable();
        
        ThreadDeathRequest tdr = mgr.createThreadDeathRequest();
        // Make sure we sync on thread death
        tdr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        tdr.enable();

        ClassPrepareRequest cpr = mgr.createClassPrepareRequest();
        for (int i=0; i<excludes.size(); ++i) {
            cpr.addClassExclusionFilter(excludes.get(i));
        }
        cpr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        cpr.enable();
        
    }


    /**
	* Dispatch incoming events
	*/
    private void handleEvent(Event event) {
        if (event instanceof ExceptionEvent) {
        	if (!enableProfiling)
        		exceptionManager.exceptionEvent((ExceptionEvent)event);
        } else if (event instanceof ModificationWatchpointEvent) {
        	//fieldwatch.fieldWatchEvent((ModificationWatchpointEvent)event);
        } else if (event instanceof MethodEntryEvent) {
        	methodEntryEvent((MethodEntryEvent)event);
        } else if (event instanceof MethodExitEvent) {
        	methodExitEvent((MethodExitEvent)event);
        } else if (event instanceof StepEvent) {
            //step.stepEvent((StepEvent)event);
        } else if (event instanceof ThreadDeathEvent) {
            threadeath.threadDeathEvent((ThreadDeathEvent)event);
        } else if (event instanceof ClassPrepareEvent) {
            //prepare.classPrepareEvent((ClassPrepareEvent)event);
        } else if (event instanceof VMDeathEvent) {
        	vmDeathEvent((VMDeathEvent)event);
        } else if (event instanceof VMDisconnectEvent) {
            connected = disconnect.vmDisconnectEvent((VMDisconnectEvent)event);
        } else if (event instanceof ThreadStartEvent){
        	
        }
    }

    private void vmDeathEvent(VMDeathEvent event) {
    	vmDeathManager.vmDeathEvent(event,enableProfiling);
    	tracer.finishedTrace();
	}

	private void methodExitEvent(MethodExitEvent event) {
    	String methodName = event.method().toString();
		String className = ClassUtils.getClass(event.method().declaringType());
		if (!excludesClassMethods.isExcluded(className,methodName)){
			if (!enableProfiling)
				methodExitManager.methodExitEvent((MethodExitEvent)event);
		}
	}

	private void methodEntryEvent(MethodEntryEvent event) {
		String methodName = event.method().toString();
		String className = ClassUtils.getClass(event.method().declaringType());
		if (!excludesClassMethods.isExcluded(className,methodName)){
	    	if (enableProfiling)
	        	profiler.profileEvent(event);
	        else 
	        	methodEntryManager.methodEntryEvent(event);
		}
	}

	/***
	* A VMDisconnectedException has happened while dealing with
	* another event. We need to flush the event queue, dealing only
	* with exit events (VMDeath, VMDisconnect) so that we terminate
	* correctly.
	*/
    synchronized void handleDisconnectedException() {
        EventQueue queue = vm.eventQueue();
        while (connected) {
            try {
                EventSet eventSet = queue.remove();
                EventIterator iter = eventSet.eventIterator();
                while (iter.hasNext()) {
                    Event event = iter.nextEvent();
                    if (event instanceof VMDeathEvent) {
                        vmDeathManager.vmDeathEvent((VMDeathEvent)event,enableProfiling);
                    } else if (event instanceof VMDisconnectEvent) {
                        connected=disconnect.vmDisconnectEvent((VMDisconnectEvent)event);
                    }
                }
                eventSet.resume(); // Resume the VM
                
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }
    }


}

