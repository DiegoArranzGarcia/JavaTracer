package com.javatracer.model.managers;

import com.sun.jdi.event.VMDisconnectEvent;

public class DisconnectManager {
	

	
	public boolean vmDisconnectEvent(VMDisconnectEvent event) {
        return false;
    }

}
