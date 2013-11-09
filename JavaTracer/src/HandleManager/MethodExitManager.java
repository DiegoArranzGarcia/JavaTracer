package HandleManager;

import java.util.Map;

import Tracer.ThreadTrace;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.MethodExitEvent;

public class MethodExitManager {

	private Map<ThreadReference, ThreadTrace> traceMap;
	private VirtualMachine vm; // Running VM
		
		public MethodExitManager(Map<ThreadReference, ThreadTrace> traceMap, VirtualMachine vm)
		{
			this.traceMap=traceMap;
			this.vm=vm;
		}
	
	
	// Forward event for thread specific processing
   public void methodExitEvent(MethodExitEvent event) {
         threadTrace(event.thread()).methodExitEvent(event);
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
