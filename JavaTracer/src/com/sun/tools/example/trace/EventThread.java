/*
 * Copyright (c) 2001, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 * This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code.
 */


package com.sun.tools.example.trace;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.Value;
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
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.StepRequest;
import com.sun.jdi.request.ThreadDeathRequest;

/**
 * This class processes incoming JDI events and displays them
 *
 * @author Robert Field
 */
public class EventThread extends Thread {

    private final VirtualMachine vm;   // Running VM
    private final String[] excludes;   // Packages to exclude
    private final PrintWriter writer;  // Where output goes

    static String nextBaseIndent = ""; // Starting indent for next thread

    private boolean connected = true;  // Connected to VM
    private boolean vmDied = true;     // VMDeath occurred

    // Maps ThreadReference to ThreadTrace instances
    private Map<ThreadReference, ThreadTrace> traceMap =
       new HashMap<>();

    EventThread(VirtualMachine vm, String[] excludes, PrintWriter writer) {
        super("event-handler");
        this.vm = vm;
        this.excludes = excludes;
        this.writer = writer;
    }

    /**
     * Run the event handling thread.
     * As long as we are connected, get event sets off
     * the queue and dispatch the events within them.
     */
    @Override
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
                // Ignore
            } catch (VMDisconnectedException discExc) {
                handleDisconnectedException();
                break;
            }
        }
    }

    /**
     * Create the desired event requests, and enable
     * them so that we will get events.
     * @param excludes     Class patterns for which we don't want events
     * @param watchFields  Do we want to watch assignments to fields
     */
    void setEventRequests(boolean watchFields) {
    	
        EventRequestManager mgr = vm.eventRequestManager();

        // want all exceptions
        ExceptionRequest excReq = mgr.createExceptionRequest(null,
                                                             true, true);
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
     * This class keeps context on events in one thread.
     * In this implementation, context is the indentation prefix.
     */
    class ThreadTrace {
        final ThreadReference thread;
        final String baseIndent;
        static final String threadDelta = "                     ";
        StringBuffer indent;
        

        ThreadTrace(ThreadReference thread) {
            this.thread = thread;
            this.baseIndent = nextBaseIndent;
            indent = new StringBuffer(baseIndent);
            nextBaseIndent += threadDelta;
            EventRequestManager mgr = vm.eventRequestManager();
            StepRequest request = mgr.createStepRequest(this.thread, StepRequest.STEP_LINE, StepRequest.STEP_OVER);
            request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
            request.addCountFilter(1);
            request.enable();      
            
        }

         void methodEntryEvent(MethodEntryEvent event)  {     
        	
        	 System.out.println("************ "+ event.method().name()+" ************");
        	
        	 try {
				List<LocalVariable> methodArgs = event.method().arguments();
				StackFrame frame = thread.frame(0);
				for (int i=0;i<methodArgs.size();i++){
					LocalVariable variable = methodArgs.get(i);
					Value v = frame.getValue(variable);
					System.out.println("\t|Argumento "+i+": " + variable.name() + " valor: " + v.toString()+"|");
					}
				System.out.println("\n");
			} catch (AbsentInformationException | IncompatibleThreadStateException e) {
				e.printStackTrace();
			}
        	
        }
        
        void methodExitEvent(MethodExitEvent event)  {
        	int numArgs = 0;
			try {
				numArgs = event.method().variables().size();
			} catch (AbsentInformationException e1) {
				e1.printStackTrace();
			}
        	/**
        	 * El tipo de valores que devuelve un método en el caso que lo haga.
        	 */
			
        	if(!event.method().returnTypeName().equals("void"))
        		System.out.println("El método "+ event.method().name() + " devuelve el valor " + event.returnValue() + " del tipo " + event.method().returnTypeName());
        	
        	
            try{
	            List<LocalVariable> variables = event.method().variables();
	            StackFrame frame = thread.frame(0);
				System.out.println("\t Al finalizar " +event.method().name()+":");
				for (int i=0;i<variables.size();i++)
					System.out.println("\t\t"+variables.get(i).name()+ " = " + frame.getValue(variables.get(i)));
				System.out.println("\n");
			 }
            catch(Exception e){}
            
            //System.out.println("************ " + event.method().name()+ " ************\n");
           
        }

        void fieldWatchEvent(ModificationWatchpointEvent event){
            Field field = event.field();
            Value value = event.valueToBe();
            System.out.println(field.name() + " = " + value.toString()+" (Variable global)");
        }

        void exceptionEvent(ExceptionEvent event) {
            
        	// Step to the catch
            EventRequestManager mgr = vm.eventRequestManager();
            try {StepRequest req = mgr.createStepRequest(thread, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
            req.addCountFilter(1);  // next step only
            req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
            req.enable();}
            catch(Exception e){
            	
           }
        }

        // Step to exception catch
        void stepEvent(StepEvent event){
        	try {
        		/*VARIABLES VISIBLES EN CADA STEP*/
        		
        		/*StackFrame frame = thread.frame(0);
				List<LocalVariable> variables = frame.visibleVariables();
				
				
				for (int i=0;i<variables.size();i++){
					System.out.println("\tVariable visibles: " + variables.get(i).name());
				}*/
				
	            // Adjust call depth
	            int cnt = 0;
	            indent = new StringBuffer(baseIndent);
	   
	                cnt = thread.frameCount();
	       
	            while (cnt-- > 0) {
	                indent.append("| ");
	            }

	            EventRequestManager mgr = vm.eventRequestManager();
	            mgr.deleteEventRequest(event.request());
	            
	            StepRequest request = mgr.createStepRequest(this.thread, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
	            request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
	            request.addCountFilter(1);
	            request.enable(); 
	            
			} catch (Exception e) {
				
			}
        	            
        }

        void threadDeathEvent(ThreadDeathEvent event)  {
            indent = new StringBuffer(baseIndent);
           }
         }
    

    /**
     * Returns the ThreadTrace instance for the specified thread,
     * creating one if needed.
     */
    ThreadTrace threadTrace(ThreadReference thread) {
        ThreadTrace trace = traceMap.get(thread);
        if (trace == null) {
            trace = new ThreadTrace(thread);
            traceMap.put(thread, trace);
        }
        return trace;
    }

    /**
     * Dispatch incoming events
     */
    private void handleEvent(Event event) {
        if (event instanceof ExceptionEvent) {
            exceptionEvent((ExceptionEvent)event);
        } else if (event instanceof ModificationWatchpointEvent) {
            fieldWatchEvent((ModificationWatchpointEvent)event);
        } else if (event instanceof MethodEntryEvent) {
            methodEntryEvent((MethodEntryEvent)event);
        } else if (event instanceof MethodExitEvent) {
            methodExitEvent((MethodExitEvent)event);
        } else if (event instanceof StepEvent) {
            stepEvent((StepEvent)event);
        } else if (event instanceof ThreadDeathEvent) {
            threadDeathEvent((ThreadDeathEvent)event);
        } else if (event instanceof ClassPrepareEvent) {
            classPrepareEvent((ClassPrepareEvent)event);
        } else if (event instanceof VMDeathEvent) {
            vmDeathEvent((VMDeathEvent)event);
        } else if (event instanceof VMDisconnectEvent) {
            vmDisconnectEvent((VMDisconnectEvent)event);
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
                        vmDeathEvent((VMDeathEvent)event);
                    } else if (event instanceof VMDisconnectEvent) {
                        vmDisconnectEvent((VMDisconnectEvent)event);
                    }
                }
                eventSet.resume(); // Resume the VM
                
            } catch (InterruptedException exc) {
                // ignore
            }
        }
    }


    // Forward event for thread specific processing
    private void methodEntryEvent(MethodEntryEvent event)  {
         threadTrace(event.thread()).methodEntryEvent(event);
    }

    // Forward event for thread specific processing
    private void methodExitEvent(MethodExitEvent event)  {
         threadTrace(event.thread()).methodExitEvent(event);
    }

    // Forward event for thread specific processing
    private void stepEvent(StepEvent event)  {
         threadTrace(event.thread()).stepEvent(event);
    }

    // Forward event for thread specific processing
    private void fieldWatchEvent(ModificationWatchpointEvent event){
         threadTrace(event.thread()).fieldWatchEvent(event);
    }

    void threadDeathEvent(ThreadDeathEvent event)  {
        ThreadTrace trace = traceMap.get(event.thread());
        if (trace != null) {  // only want threads we care about
            trace.threadDeathEvent(event);   // Forward event
        }
        
        String s=event.virtualMachine().allClasses().get(1).toString();
        System.out.println(s.substring(0, s.length()-65)+ "\n");
    }

    /**
     * A new class has been loaded.
     * Set watchpoints on each of its fields
     */
    private void classPrepareEvent(ClassPrepareEvent event)  {
        EventRequestManager mgr = vm.eventRequestManager();
        List<Field> fields = event.referenceType().visibleFields();
        for (Field field : fields) {
            ModificationWatchpointRequest req =
                     mgr.createModificationWatchpointRequest(field);
            for (int i=0; i<excludes.length; ++i) {
                req.addClassExclusionFilter(excludes[i]);
            }
            req.setSuspendPolicy(EventRequest.SUSPEND_NONE);
            req.enable();
        }
        String s=event.virtualMachine().allClasses().get(0).toString();
        System.out.println(s.substring(0, s.length()-65)+ "\n");
    
    }

    private void exceptionEvent(ExceptionEvent event) {
        ThreadTrace trace = traceMap.get(event.thread());
        if (trace != null) {  // only want threads we care about
            trace.exceptionEvent(event);      // Forward event
        }
    }

    public void vmDeathEvent(VMDeathEvent event) {
    	vmDied = true;
       }

    public void vmDisconnectEvent(VMDisconnectEvent event) {
        connected = false;
        }

}
