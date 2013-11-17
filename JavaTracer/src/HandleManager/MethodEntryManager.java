package HandleManager;


import java.util.ArrayList;
import java.util.List;

import DataBase.DataBaseWriter;
import Info.MethodEntryInfo;
import Tracer.TracerUtilities;


import com.sun.jdi.Field;
import com.sun.jdi.Method;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
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
       	String methodName = method.name();
        List<Object> arguments = processArguments(method,thread);
        String className = TracerUtilities.getClass(method.declaringType());
        List<Object> arguments_this = processThis(event);
        MethodEntryInfo info = new MethodEntryInfo(methodName,className,arguments,arguments_this);
        writeOutput(info);
    }

	private List<Object> processArguments(Method method, ThreadReference thread) {
    	  
    	List<Object> arguments = new ArrayList<>();
    	
    	try {
			StackFrame stack = thread.frame(0);
			List<Value> argsValue = stack.getArgumentValues();
			Object varObj = null;
			for (int i=0;i<argsValue.size();i++){
				varObj = TracerUtilities.getObj(argsValue.get(i));
				arguments.add(varObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return arguments;
	}


	private List<Object> processThis(MethodEntryEvent event) {
	
	Field f=null;
	String tipo="";
	Object valor=null;
	Object varObj=null;
	List<Object> arguments_this = new ArrayList<>();
	List<Field> s=event.virtualMachine().allClasses().get(0).allFields();
	
	while (!s.isEmpty()){
		f = s.get(0);
		//tipo = f.typeName();
		valor=event.virtualMachine().allClasses().get(0).getValue(f);
		varObj = TracerUtilities.getObj((Value)valor);
		arguments_this.add(varObj);
	    s.remove(0);
	   }
	
		return arguments_this;
	}



}
