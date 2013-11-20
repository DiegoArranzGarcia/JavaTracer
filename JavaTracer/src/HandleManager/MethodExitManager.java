package HandleManager;

import java.util.ArrayList;
import java.util.List;

import DataBase.DataBaseWriter;
import Info.MethodExitInfo;
import Tracer.TracerUtilities;

import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
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
         List<Object> arguments = processArguments(method,thread);
         
         String className = TracerUtilities.getClass(method.declaringType());
         Value returnValue = event.returnValue();
         Object returnObject = null; 
         if (!(returnValue instanceof VoidValue)){ 
          returnObject = TracerUtilities.getObj(returnValue);
         }
         ReferenceType ref=method.declaringType(); // "class" where is declaring the method
         List<Object> arguments_this = processThis(event,ref, thread);
         MethodExitInfo info = new MethodExitInfo(methodName,className,returnObject,arguments,arguments_this);
         writeOutput(info);
    }
   
    private List<Object> processThis(MethodExitEvent event, ReferenceType ref,ThreadReference thread ) {   	
    	Field f=null;
    	Object valor=null;
    	Object varObj=null;
    	StackFrame stack=null;
    	List<Object> arguments_this = new ArrayList<>();
    	List<Field> fields=ref.allFields();
    	
    	try {
    		stack = thread.frame(0);
    		} catch (IncompatibleThreadStateException e) {
    			e.printStackTrace();
    		}
    	
    	while (!fields.isEmpty()){
    		f = fields.get(0);
    		valor = stack.thisObject().getValue(f);
    		varObj = TracerUtilities.getObj((Value)valor);
    		arguments_this.add(varObj);
    	    fields.remove(0);
    	   }
    	
    		return arguments_this;
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
  
}
