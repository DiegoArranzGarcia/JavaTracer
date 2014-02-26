package com.tracer.model.managers;

import java.util.Map;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.ThreadDeathEvent;
import com.tracer.model.ThreadTrace;

public class ThreadDeathManager {

private  Map<ThreadReference, ThreadTrace> traceMap;
	
	public ThreadDeathManager(Map<ThreadReference, ThreadTrace> traceMap)
	{
		this.traceMap=traceMap;
	}
	
	public void threadDeathEvent(ThreadDeathEvent event) {
        ThreadTrace trace = traceMap.get(event.thread());
        if (trace != null) { // only want threads we care about
        	threadDeathEvent_Thread(event,trace);// Forward event
        }
    }
	
	public void threadDeathEvent_Thread(ThreadDeathEvent event,ThreadTrace thread) {
        thread.SetIndent(new StringBuffer(thread.getBaseIndent()));
		
        
       }
	
}