package com.javatracer.model.managers;


import java.util.ArrayList;
import java.util.List;

import com.general.model.variables.data.Data;
import com.general.model.variables.data.StringData;
import com.javatracer.model.ClassUtils;
import com.javatracer.model.methods.data.MethodExitInfo;
import com.javatracer.model.writers.JavaTraceWriter;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Type;
import com.sun.jdi.Value;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodExitEvent;

public class ExceptionManager {
	
	private ClassUtils utils;
	private JavaTraceWriter writer;
	
	public ExceptionManager(JavaTraceWriter writer, ClassUtils utils){
		this.utils=utils;
	    this.writer=writer;
	}
	
    public void exceptionEvent(ExceptionEvent event) {
    	
    	ThreadReference thread = event.thread();

    	Location lo=event.catchLocation();
    	
    	
    	String classException=event.location().declaringType().name();
    	
    	if(!classException.contains("ClassLoader") && lo==null)
    	{
    	
    	String methodName=event.location().method().name();
    	String className= event.location().declaringType().name();
    	String returnValue=event.exception().referenceType().name();
    	Data returnObject=new StringData("Exception", 0, returnValue);
    	 
    	
    	ReferenceType ref=event.location().method().declaringType(); // "class" where is declaring the method
         Data object_this = processThis(event,ref, thread);
        	
    	
    	MethodExitInfo info = new MethodExitInfo(methodName,className,returnObject,null,object_this);
        writer.writeMethodExitInfo(info);
        
    	
    
    	
    	}
    	
    
    }
	  
		
	private Data processThis(ExceptionEvent event, ReferenceType ref, ThreadReference thread) {
		
		StackFrame stack=null;

		try {
			stack = thread.frame(0);
		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		}
		
		Data valueThis = utils.getObj("this",stack.thisObject(),new ArrayList<Long>());
		
		return valueThis;
	}
    
    
    
}
