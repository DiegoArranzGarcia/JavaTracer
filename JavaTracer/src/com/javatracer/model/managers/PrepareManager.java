package com.javatracer.model.managers;

import java.util.List;
import com.sun.jdi.Field;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;

public class PrepareManager {
   
	private  final String[] excludes; 
	private VirtualMachine vm; // Running VM
	
	public PrepareManager(String[] excludes,VirtualMachine vm)
	{
		this.excludes=excludes;
		this.vm=vm;
		
	}
	
	/**
	 * A new class has been loaded.
	 * Set watchpoints on each of its fields
	*/
	
	 public void classPrepareEvent(ClassPrepareEvent event) {
	        EventRequestManager mgr = vm.eventRequestManager();
	        List<Field> fields = event.referenceType().visibleFields();
	        for (Field field : fields) {
	            ModificationWatchpointRequest req =
	                     mgr.createModificationWatchpointRequest(field);
	            for (int i=0; i<excludes.length; ++i) {
	                req.addClassExclusionFilter(excludes[i]);
	            }
	            req.setSuspendPolicy(EventRequest.SUSPEND_NONE);
	            req.enable();
	        }
	      //  String s=event.virtualMachine().allClasses().get(0).toString();
	       // System.out.println(s.substring(0, s.length()-65)+ "\n");
	    
	    }



}
