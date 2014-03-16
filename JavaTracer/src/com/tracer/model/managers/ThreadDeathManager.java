package com.tracer.model.managers;

import java.util.Map;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.ThreadDeathEvent;
import com.tracer.model.ThreadTrace;

public class ThreadDeathManager {

private  Map<ThreadReference, ThreadTrace> traceMap;
	
	public ThreadDeathManager(Map<ThreadReference, ThreadTrace> traceMap){
		this.traceMap=traceMap;
	}
	
	public void threadDeathEvent(ThreadDeathEvent event) {
      
	}
	
}
