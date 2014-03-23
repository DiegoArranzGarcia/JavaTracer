package com.tracer.model.managers;

import com.sun.jdi.event.VMDeathEvent;
import com.tracer.model.TraceWriter;


public class VMDeathManager {
 
	private TraceWriter writer;
	
	public VMDeathManager(TraceWriter writer) {
		this.writer = writer;
	}

	public void vmDeathEvent(VMDeathEvent event, boolean enableProfiling) {
		if (!enableProfiling){
			synchronized (writer) {
				writer.close();
			}
		}
    }


}
