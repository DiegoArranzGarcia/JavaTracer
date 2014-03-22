package com.tracer.model.managers;

import java.util.Map;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.ThreadDeathEvent;
import com.tracer.model.ThreadTrace;

public class ThreadDeathManager {

	
	public ThreadDeathManager(Map<ThreadReference, ThreadTrace> traceMap){
		
	}
	
	public void threadDeathEvent(ThreadDeathEvent event) {
      
	}
	
}
