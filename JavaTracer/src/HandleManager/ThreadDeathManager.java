package HandleManager;

import java.util.Map;

import Tracer.ThreadTrace;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.ThreadDeathEvent;

public class ThreadDeathManager {

private  Map<ThreadReference, ThreadTrace> traceMap;
	
	public ThreadDeathManager(Map<ThreadReference, ThreadTrace> traceMap)
	{
		this.traceMap=traceMap;
	}
	
	public void threadDeathEvent(ThreadDeathEvent event) {
        ThreadTrace trace = traceMap.get(event.thread());
        if (trace != null) { // only want threads we care about
            trace.threadDeathEvent(event); // Forward event
        }
    }
	
}
