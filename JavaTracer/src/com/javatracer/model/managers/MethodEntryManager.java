package com.javatracer.model.managers;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.ClassUtils;
import com.javatracer.model.data.MethodEntryInfo;
import com.javatracer.model.data.VariableInfo;
import com.javatracer.model.writers.XStreamWriter;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.event.MethodEntryEvent;

/**
 * This class handles every time that a method is called. In there the information 
 * is registred in the trace or used for profiling porpurses. 
 */
public class MethodEntryManager{
	
	private ClassUtils utils;
	private XStreamWriter writer;
			
	/**
	 * Constructor that need a DataBaseWriter to register the method information. Excludes classes are not
	 * printed and ignore called methods from excludes classes.
	 * @param dbw
	 * @param utils 
	 */
	public MethodEntryManager(XStreamWriter writer, ClassUtils utils)
	{
		this.writer = writer;
		this.utils = utils;
	}
	
	/**
	 * This method is trigger every time one method (in not excluded classes) is called. </br>
	 * The information that is collected is the following: </br>
	 * </ul>
	 * 		<li>Method's name</li>
	 * 		<li>Method's Class (Class where the method is called)</li>
	 * 		<li>Method's arguments</li>
	 * 		<li>Method's Class information (State of the "this" object where the mehotd is called)</li>
	 * </ul>
	 * @param event - The event that is going to be processed.
	 */
	
    public void methodEntryEvent(MethodEntryEvent event) {
    	
    	ThreadReference thread = event.thread();
       	Method method = event.method();
       	ReferenceType ref=method.declaringType(); //"class" where is declare 
       	String methodName = method.name();
        List<VariableInfo> arguments = processArguments(method,thread);
        String className = ClassUtils.getClass(method.declaringType());
        VariableInfo argument_this = processThis(event,ref,thread);
        MethodEntryInfo info = new MethodEntryInfo(methodName,className,arguments,argument_this);
        writer.writeOutput(info);
    }

	private List<VariableInfo> processArguments(Method method, ThreadReference thread) {
    	  
    	List<VariableInfo> arguments = new ArrayList<>();
    	
    	try {
			StackFrame stack = thread.frame(0);
			List<LocalVariable> variables = method.arguments();
			for (int i=0;i<variables.size();i++){
				LocalVariable var = variables.get(i);
				Value value = stack.getValue(var);
				Object varObj = utils.getObj(value,new ArrayList<Long>());
				String nameVar = var.name();
				arguments.add(new VariableInfo(nameVar,varObj));
			}
		} catch (Exception e) {
			
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
		
		VariableInfo variableThis = null;
		Value value = stack.thisObject();
		
		
		Object valueThis = utils.getObj(value,new ArrayList<Long>());
		variableThis = new VariableInfo("this", valueThis);
		
		return variableThis;
	}

}
