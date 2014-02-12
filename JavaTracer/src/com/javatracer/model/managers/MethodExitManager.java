package com.javatracer.model.managers;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.ClassUtils;
import com.javatracer.model.methods.data.MethodExitInfo;
import com.javatracer.model.variables.data.StringData;
import com.javatracer.model.writers.XStreamWriter;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VoidValue;
import com.sun.jdi.event.MethodExitEvent;

public class MethodExitManager{
		
	private ClassUtils utils; 
	private XStreamWriter writer;
	
	public MethodExitManager(XStreamWriter writer, ClassUtils utils)
	{
		this.writer = writer;
		this.utils = utils;
	}
	
	// Forward event for thread specific processing
    public void methodExitEvent(MethodExitEvent event) {
    	ThreadReference thread = event.thread();
    	 Method method = event.method();
         String methodName = method.name();
         List<Object> arguments = processArguments(method,thread);
         
         String className = ClassUtils.getClass(method.declaringType());
         Value returnValue = event.returnValue();
         Object returnObject = null; 
         if (!(returnValue instanceof VoidValue)){ 
        	 returnObject = utils.getObj("return",returnValue,new ArrayList<Long>());
         }
         
         if (className.equals("program.Program") && (methodName.equals("toString"))){
        	 returnObject = new StringData("return",1,"a");
         }
         
         ReferenceType ref=method.declaringType(); // "class" where is declaring the method
         Object object_this = processThis(event,ref, thread);
         MethodExitInfo info = new MethodExitInfo(methodName,className,returnObject,arguments,object_this);
         writer.writeOutput(info);
    }
   

	private Object processThis(MethodExitEvent event, ReferenceType ref, ThreadReference thread) {
	
		StackFrame stack=null;

		try {
			stack = thread.frame(0);
		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		}
		
		Object valueThis = utils.getObj("this",stack.thisObject(),new ArrayList<Long>());
		
		return valueThis;
	}


    private List<Object> processArguments(Method method, ThreadReference thread) {
    	  
    	List<Object> arguments = new ArrayList<>();
    	
    	try {
			StackFrame stack = thread.frame(0);
			List<LocalVariable> variables = method.arguments();
			for (int i=0;i<variables.size();i++){
				LocalVariable var = variables.get(i);
				String nameVar = var.name();
				Object varObj = utils.getObj(nameVar,stack.getValue(var),new ArrayList<Long>());
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
