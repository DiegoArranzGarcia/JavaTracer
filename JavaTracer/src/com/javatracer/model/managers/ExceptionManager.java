package com.javatracer.model.managers;


import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import com.general.model.variables.data.Data;
import com.general.model.variables.data.StringData;
import com.javatracer.model.ClassUtils;
import com.javatracer.model.methods.data.MethodExitInfo;
import com.javatracer.model.writers.TraceWriter;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Type;
import com.sun.jdi.Value;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodExitEvent;

public class ExceptionManager {
	
	private ClassUtils utils;
	private TraceWriter writer;
	
	public ExceptionManager(TraceWriter writer, ClassUtils utils){
		this.utils=utils;
	    this.writer=writer;
	}
	
    public void exceptionEvent(ExceptionEvent event) {
    	
    	ThreadReference thread = event.thread();

    	Location lo=event.catchLocation();
    	
    	
    	String classException=event.location().declaringType().name();
    	
    	if(!classException.contains("ClassLoader") && lo==null)
    	{
    		
    			
    			try {
    				List<StackFrame> list=null;
					list=thread.frames();
					
					int i=0;
					
					while(list.size()>i){
						
						StackFrame frame=list.get(i);
						
						String method=frame.location().method().name();
						String clasName=frame.location().declaringType().name();
						
						List<Data> arguments = processArguments(frame.location().method(),frame.thread());
					    
						
						String returnValue=event.exception().referenceType().name();
				    	Data returnObject=new StringData("Exception", 0, returnValue);
				    	 
				    	
				    	ReferenceType ref=event.location().method().declaringType(); // "class" where is declaring the method
				         Data object_this = processThis(event,ref, thread);
				        	
				    	
				    	MethodExitInfo info = new MethodExitInfo(method,clasName,returnObject,arguments,object_this);
				        writer.writeMethodExitInfo(info);
				        
						
						
						i++;
					}
			    
    			} catch (IncompatibleThreadStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		
				}
    			
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
    
	private List<Data> processArguments(Method method, ThreadReference thread) {
  	  
    	List<Data> arguments = new ArrayList<>();
    	
    	try {
			StackFrame stack = thread.frame(0);
			List<LocalVariable> variables = method.arguments();
			for (int i=0;i<variables.size();i++){
				LocalVariable var = variables.get(i);
				String nameVar = var.name();
				Data varObj = utils.getObj(nameVar,stack.getValue(var),new ArrayList<Long>());
				arguments.add(varObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return arguments;

	}
    /**
     * Returns the ThreadTrace instance for the specified thread,
	 * creating one if needed.
	 */

    
}
