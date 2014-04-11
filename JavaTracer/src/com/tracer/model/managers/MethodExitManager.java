package com.tracer.model.managers;

import java.util.ArrayList;
import java.util.List;

import com.general.model.variables.data.Data;
import com.general.settings.model.Settings;
import com.sun.jdi.*;
import com.sun.jdi.event.MethodExitEvent;
import com.tracer.model.ClassUtils;
import com.tracer.model.TraceWriter;
import com.tracer.model.methods.data.MethodExitInfo;

public class MethodExitManager{
		
	private ClassUtils utils; 
	private TraceWriter writer;
	
	public MethodExitManager(TraceWriter writer, ClassUtils utils)
	{
		this.writer = writer;
		this.utils = utils;
	}
	
	// Forward event for thread specific processing
    public void methodExitEvent(MethodExitEvent event) {
    	
    	ThreadReference thread = event.thread();
    	Method method = event.method();
        String className = ClassUtils.getClass(method.declaringType());
        
        String methodName = method.name();
        List<Data> arguments = processArguments(method,thread);

        Value returnValue = event.returnValue();
        Data returnObject = null; 

        if (!(returnValue instanceof VoidValue)) 
        	returnObject = utils.getObj("return",returnValue,new ArrayList<Long>());         

        ReferenceType ref = method.declaringType(); // "class" where is declaring the method
        
        Data object_this = null;
        
        if (!Settings.getInstance().isExcludedThis())
        	object_this = processThis(event,ref, thread);
        
        
        MethodExitInfo info = new MethodExitInfo(methodName,className,returnObject,arguments,object_this);

        synchronized (writer) { 
        	writer.writeMethodExitInfo(info);	
        }
         
    }
   

	private Data processThis(MethodExitEvent event, ReferenceType ref, ThreadReference thread) {
	
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

  
}
