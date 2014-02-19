package com.javatracer.model;


import java.util.HashMap;
import java.util.Map;

import com.javatracer.model.managers.DeathManager;
import com.javatracer.model.managers.DisconnectManager;
import com.javatracer.model.managers.ExceptionManager;
import com.javatracer.model.managers.MethodEntryManager;
import com.javatracer.model.managers.MethodExitManager;
import com.javatracer.model.managers.ThreadDeathManager;
import com.javatracer.model.methods.data.ThreadInfo;
import com.javatracer.model.writers.JavaTraceWriter;
import com.javatracer.profiler.Profiler;
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


public class EventThread extends Thread {

    private final VirtualMachine vm; // Running VM
    private final String[] excludes; // Packages to exclude
    private boolean connected = true; // Connected to VM
    private boolean enableProfiling;
	private boolean vmDied = false; // VMDeath occurred
    
    // Maps ThreadReference to ThreadTrace instances
    private Map<ThreadReference, ThreadTrace> traceMap = new HashMap<>();
       
    //Managers
    private DeathManager death;
    private Profiler profiler;
    private DisconnectManager disconnect;
    private ExceptionManager exception;
    private ThreadDeathManager threadeath;
    //private FieldWatchManager fieldwatch;
    private MethodEntryManager methodentry;
    private MethodExitManager methodexit;
    //private StepManager step;
    //private PrepareManager prepare;
    private JavaTraceWriter writer;

    EventThread(VirtualMachine vm, String[] excludes, String nameXlm, boolean enableProfiling){
        super("event-handler");
        this.vm = vm;
        this.enableProfiling = enableProfiling;
        this.excludes = excludes;
        
        writer = new JavaTraceWriter(nameXlm);
        writer.writeThreadInfo(new ThreadInfo());
                
        //news Managers with virtual Machine  
        //or array excludes created in Trace class  
        
        death = new DeathManager();
        disconnect = new DisconnectManager();
        threadeath = new ThreadDeathManager(traceMap);
        //fieldwatch=new FieldWatchManager(traceMap,vm);
        
        ClassUtils utils = new ClassUtils(excludes);
        methodentry = new MethodEntryManager(writer,utils);
        methodexit = new MethodExitManager(writer,utils);
        //step=new StepManager(traceMap,vm);
        //prepare=new PrepareManager(excludes,vm);
		exception = new ExceptionManager(traceMap,vm);
		
		if (enableProfiling){
			profiler = new Profiler();			
		}
		
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
        for (int i=0; i<excludes.length; ++i) {
            menr.addClassExclusionFilter(excludes[i]);
        }
        menr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        menr.enable();

        MethodExitRequest mexr = mgr.createMethodExitRequest();
        for (int i=0; i<excludes.length; ++i) {
            mexr.addClassExclusionFilter(excludes[i]);
        }
        mexr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        mexr.enable();
        
        ThreadDeathRequest tdr = mgr.createThreadDeathRequest();
        // Make sure we sync on thread death
        tdr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        tdr.enable();

        ClassPrepareRequest cpr = mgr.createClassPrepareRequest();
        for (int i=0; i<excludes.length; ++i) {
            cpr.addClassExclusionFilter(excludes[i]);
        }
        cpr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        cpr.enable();
        
    }


    /**
	* Dispatch incoming events
	*/
    private void handleEvent(Event event) {
        if (event instanceof ExceptionEvent) {
            exception.exceptionEvent((ExceptionEvent)event);
        } else if (event instanceof ModificationWatchpointEvent) {
        	//fieldwatch.fieldWatchEvent((ModificationWatchpointEvent)event);
        } else if (event instanceof MethodEntryEvent) {	
            methodentry.methodEntryEvent((MethodEntryEvent)event);
            if (enableProfiling)
            	profiler .profileEvent((MethodEntryEvent)event);
        } else if (event instanceof MethodExitEvent) {
        	methodexit.methodExitEvent((MethodExitEvent)event);
        } else if (event instanceof StepEvent) {
            //step.stepEvent((StepEvent)event);
        } else if (event instanceof ThreadDeathEvent) {
            threadeath.threadDeathEvent((ThreadDeathEvent)event);
        } else if (event instanceof ClassPrepareEvent) {
            //prepare.classPrepareEvent((ClassPrepareEvent)event);
        } else if (event instanceof VMDeathEvent) {
        	finishTrace();
        } else if (event instanceof VMDisconnectEvent) {
            connected=disconnect.vmDisconnectEvent((VMDisconnectEvent)event);
        } else if (event instanceof ThreadStartEvent){
        	System.out.println(((ThreadStartEvent)event).thread().name());
        }
    }

    private void finishTrace() {
    	writer.close();
		
		if (enableProfiling)
			profiler.showProfile();
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
                        vmDied=death.vmDeathEvent((VMDeathEvent)event);
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

