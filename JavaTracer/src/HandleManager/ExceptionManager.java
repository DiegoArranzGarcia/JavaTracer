package HandleManager;

import java.util.Map;

import Tracer.ThreadTrace;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;

public class ExceptionManager {
	
	private  Map<ThreadReference, ThreadTrace> traceMap;
	private VirtualMachine vm;
	
	public ExceptionManager(Map<ThreadReference, ThreadTrace> traceMap, VirtualMachine vm)
	{
		this.traceMap=traceMap;
	    this.vm=vm;
	}
	

	  public void exceptionEvent(ExceptionEvent event) {
	        ThreadTrace trace = traceMap.get(event.thread());
	        if (trace != null) { // only want threads we care about
	        	exceptionEvent_Thread(event,trace); // Forward event
	            
	        }
	    }
	  
	  public void exceptionEvent_Thread(ExceptionEvent event, ThreadTrace thread) {
          
          // Step to the catch
      EventRequestManager mgr = vm.eventRequestManager();
      try {StepRequest req = mgr.createStepRequest(thread.getThreadReference(), StepRequest.STEP_LINE, StepRequest.STEP_INTO);
      req.addCountFilter(1); // next step only
      req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
      req.enable();}
      catch(Exception e){
              
     }
  }
}
