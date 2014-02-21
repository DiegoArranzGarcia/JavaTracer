package com.javatracer.model.managers;

import java.util.ArrayList;
import java.util.List;

import com.general.model.variables.data.Data;
import com.javatracer.model.ClassUtils;
import com.javatracer.model.methods.data.MethodExitInfo;
import com.javatracer.model.writers.JavaTraceWriter;
import com.sun.jdi.*;
import com.sun.jdi.event.MethodExitEvent;

public class MethodExitManager{
		
	private ClassUtils utils; 
	private JavaTraceWriter writer;
	
	public MethodExitManager(JavaTraceWriter writer, ClassUtils utils)
	{
		this.writer = writer;
		this.utils = utils;
	}
	
	// Forward event for thread specific processing
    public void methodExitEvent(MethodExitEvent event) {
    	ThreadReference thread = event.thread();
    	 Method method = event.method();
         String methodName = method.name();
         List<Data> arguments = processArguments(method,thread);
         
         String className = ClassUtils.getClass(method.declaringType());
         Value returnValue = event.returnValue();
         Data returnObject = null; 
         
         if (!(returnValue instanceof VoidValue)) 
        	 returnObject = utils.getObj("return",returnValue,new ArrayList<Long>());         
         
         /*if (className.equals("program.Program") && (methodName.equals("toString"))){
        	 returnObject = new StringData("return",1,"a");
         }*/
         
         ReferenceType ref=method.declaringType(); // "class" where is declaring the method
         Data object_this = processThis(event,ref, thread);
         MethodExitInfo info = new MethodExitInfo(methodName,className,returnObject,arguments,object_this);
         writer.writeMethodExitInfo(info);
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
    /**
     * Returns the ThreadTrace instance for the specified thread,
	 * creating one if needed.
	 */
  
}
