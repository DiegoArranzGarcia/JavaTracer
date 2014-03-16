package com.tracer.model.managers;

import java.util.Map;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.*;
import com.tracer.model.ThreadTrace;

public class StepManager {
	
	private Map<ThreadReference, ThreadTrace> traceMap;
	private VirtualMachine vm; // Running VM
		
		public StepManager(Map<ThreadReference, ThreadTrace> traceMap, VirtualMachine vm)
		{
			this.traceMap=traceMap;
			this.vm=vm;
		}

	// Forward event for thread specific processing
    public void stepEvent(StepEvent event) {
         stepEvent_Thread(event,threadTrace(event.thread()));
    }
    
   
    // Step to exception catch
    public void stepEvent_Thread(StepEvent event,ThreadTrace thread){
            try {
                    /*VARIABLES VISIBLES EN CADA STEP*/
                    
                    /*StackFrame frame = thread.frame(0);
                            List<LocalVariable> variables = frame.visibleVariables();
                            
                            
                            for (int i=0;i<variables.size();i++){
                                    System.out.println("\tVariable visibles: " + variables.get(i).name());
                            }*/
                            
     // Adjust call depth
     EventRequestManager mgr = vm.eventRequestManager();
     mgr.deleteEventRequest(event.request());
    
     StepRequest request = mgr.createStepRequest(thread.getThreadReference(), StepRequest.STEP_LINE, StepRequest.STEP_INTO);
     request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
     request.addCountFilter(1);
     request.enable();
    
                    } catch (Exception e) {
                            
                    }
            
    }

    
    /**
* Returns the ThreadTrace instance for the specified thread,
* creating one if needed.
*/
    public ThreadTrace threadTrace(ThreadReference thread) {
        ThreadTrace trace = traceMap.get(thread);
        if (trace == null) {
            trace = new ThreadTrace(thread,vm);
            traceMap.put(thread, trace);
        }
        return trace;
    }
}
