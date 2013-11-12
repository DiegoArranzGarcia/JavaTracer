package Tracer;

import com.sun.jdi.Field;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;

/**
* This class keeps context on events in one thread.
* In this implementation, context is the indentation prefix.
*/
  public class ThreadTrace {
      final ThreadReference thread;
      final String baseIndent;
      static final String threadDelta = " ";
      StringBuffer indent;
      static String nextBaseIndent = ""; // Starting indent for next thread
      VirtualMachine vm; // Running VM
      

      public ThreadTrace(ThreadReference thread, VirtualMachine vm) {
          this.thread = thread;
          this.baseIndent = nextBaseIndent;
          this.vm=vm;
          indent = new StringBuffer(baseIndent);
          nextBaseIndent += threadDelta;
          EventRequestManager mgr = vm.eventRequestManager();
          StepRequest request = mgr.createStepRequest(this.thread, StepRequest.STEP_LINE, StepRequest.STEP_OVER);
          request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
          request.addCountFilter(1);
          request.enable();
          
      }
      
      public ThreadReference getThreadReference()
      {
    	  return thread;
    	  
      }
      
      public String getBaseIndent()
      {
    	  return baseIndent;
    	  
      }
      
            

	public static String getThreaddelta() {
		return threadDelta;
	}

	public StringBuffer getIndent() {
		return indent;
	}

	public static String getNextBaseIndent() {
		return nextBaseIndent;
	}

	public VirtualMachine getVm() {
		return vm;
	}

	
	public void SetIndent(StringBuffer indent)
	{
		this.indent=indent;
	}
	
	
	
       }

