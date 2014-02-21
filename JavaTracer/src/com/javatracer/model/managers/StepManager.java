package com.javatracer.model.managers;

import java.util.Map;

import com.javatracer.model.ThreadTrace;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.*;

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
     int cnt = 0;
      thread.SetIndent(new StringBuffer(thread.getBaseIndent())); 
    
    
     cnt = thread.getThreadReference().frameCount();
    
     while (cnt-- > 0) {
    	 thread.SetIndent(thread.getIndent().append("| "));
    	 
     }

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
