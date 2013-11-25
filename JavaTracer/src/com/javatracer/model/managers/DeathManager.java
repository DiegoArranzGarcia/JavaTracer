package com.javatracer.model.managers;

import com.sun.jdi.event.VMDeathEvent;


public class DeathManager {
 
	
	public boolean vmDeathEvent(VMDeathEvent event) {
         return true;
    }


}
