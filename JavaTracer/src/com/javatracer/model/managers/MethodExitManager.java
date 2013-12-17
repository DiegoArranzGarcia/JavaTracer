package com.javatracer.model.managers;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.TracerUtilities;
import com.javatracer.model.data.MethodExitInfo;
import com.javatracer.model.data.VariableInfo;
import com.javatracer.model.writers.DataBaseWriter;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VoidValue;
import com.sun.jdi.event.MethodExitEvent;

public class MethodExitManager extends VMEventsManager{
	
	public MethodExitManager(){};	
	
	public MethodExitManager(DataBaseWriter dbw)
	{
		super(dbw);
	}
	
	// Forward event for thread specific processing
    public void methodExitEvent(MethodExitEvent event) {
    	ThreadReference thread = event.thread();
    	 Method method = event.method();
         String methodName = method.name();
         List<VariableInfo> arguments = processArguments(method,thread);
         
         String className = TracerUtilities.getClass(method.declaringType());
         Value returnValue = event.returnValue();
         Object returnObject = null; 
         if (!(returnValue instanceof VoidValue)){ 
        	 returnObject = TracerUtilities.getObj(returnValue,new ArrayList<Long>());
         }
         
         ReferenceType ref=method.declaringType(); // "class" where is declaring the method
         VariableInfo object_this = processThis(event,ref, thread);
         MethodExitInfo info = new MethodExitInfo(methodName,className,returnObject,arguments,object_this);
         writeOutput(info);
    }
   

	private VariableInfo processThis(MethodExitEvent event, ReferenceType ref, ThreadReference thread) {
	
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
    /**
     * Returns the ThreadTrace instance for the specified thread,
	 * creating one if needed.
	 */
  
}
