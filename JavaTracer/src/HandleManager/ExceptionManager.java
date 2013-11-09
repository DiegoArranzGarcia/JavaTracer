package HandleManager;

import java.util.Map;

import Tracer.ThreadTrace;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.ExceptionEvent;

public class ExceptionManager {
	
	private  Map<ThreadReference, ThreadTrace> traceMap;
	
	public ExceptionManager(Map<ThreadReference, ThreadTrace> traceMap)
	{
		this.traceMap=traceMap;
	}
	

	  public void exceptionEvent(ExceptionEvent event) {
	        ThreadTrace trace = traceMap.get(event.thread());
	        if (trace != null) { // only want threads we care about
	            trace.exceptionEvent(event); // Forward event
	            
	        }
	    }
}
