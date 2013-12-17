package com.javatracer.model.managers;


import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.TracerUtilities;
import com.javatracer.model.data.MethodEntryInfo;
import com.javatracer.model.data.VariableInfo;
import com.javatracer.model.writers.DataBaseWriter;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.MethodEntryEvent;



public class MethodEntryManager extends VMEventsManager{
	
	public MethodEntryManager(DataBaseWriter dbw)
	{
		super(dbw);
	}
	
	// Forward event for thread specific processing
    public void methodEntryEvent(MethodEntryEvent event) {
    	
    	ThreadReference thread = event.thread();
       	Method method = event.method();
       	ReferenceType ref=method.declaringType(); //"class" where is declare 
       	String methodName = method.name();
        List<VariableInfo> arguments = processArguments(method,thread);
        String className = TracerUtilities.getClass(method.declaringType());
        VariableInfo argument_this = processThis(event,ref,thread);
        MethodEntryInfo info = new MethodEntryInfo(methodName,className,arguments,argument_this);
        writeOutput(info);
    }

	private List<VariableInfo> processArguments(Method method, ThreadReference thread) {
    	  
    	List<VariableInfo> arguments = new ArrayList<>();
    	
    	try {
			StackFrame stack = thread.frame(0);
			List<LocalVariable> variables = method.arguments();
			for (int i=0;i<variables.size();i++){
				LocalVariable var = variables.get(i);
				Object varObj = TracerUtilities.getObj(stack.getValue(var),new ArrayList<Long>());
				String nameVar = var.name();
				arguments.add(new VariableInfo(nameVar,varObj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return arguments;
	}


	private VariableInfo processThis(MethodEntryEvent event, ReferenceType ref, ThreadReference thread) {
	
		StackFrame stack=null;

		try {
			stack = thread.frame(0);
		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		}
		
		Object valueThis = TracerUtilities.getObj(stack.thisObject(),new ArrayList<Long>());
		VariableInfo variableThis = new VariableInfo("this", valueThis);
		
		return variableThis;
	}



}
