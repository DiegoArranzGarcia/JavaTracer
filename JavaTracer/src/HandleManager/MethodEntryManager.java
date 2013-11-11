package HandleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DataBase.DataBaseWriter;
import DataBase.MethodEntryInfo;
import Tracer.ThreadTrace;
import Tracer.TracerUtilities;

import com.sun.jdi.Method;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.MethodEntryEvent;

public class MethodEntryManager extends VMEventsManager{

	private Map<ThreadReference, ThreadTrace> traceMap;
	private VirtualMachine vm; // Running VM
		
	public MethodEntryManager(Map<ThreadReference, ThreadTrace> traceMap, VirtualMachine vm, DataBaseWriter dbw)
	{
		super(dbw);
		this.traceMap=traceMap;
		this.vm=vm;
	}
	
	// Forward event for thread specific processing
    public void methodEntryEvent(MethodEntryEvent event) {

       	ThreadReference thread = event.thread();
       	Method method = event.method();
       	String methodName = method.name();
        List<Object> arguments = processArguments(method,thread);
        String className = TracerUtilities.getClass(method.declaringType());
        MethodEntryInfo info = new MethodEntryInfo(methodName,className,arguments);
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

	/**
	 * Returns the ThreadTrace instance for the specified thread,
	 * creating one if needed.
	 */
	
    public ThreadTrace threadTrace(ThreadReference thread) {
        ThreadTrace trace = traceMap.get(thread);
        if (trace == null) {
            trace = new ThreadTrace(thread,vm);
            traceMap.put(thread, trace);
        }
        return trace;
    }

}
