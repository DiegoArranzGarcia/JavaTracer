package HandleManager;

import java.util.ArrayList;
import java.util.List;

import DataBase.DataBaseWriter;
import Info.MethodExitInfo;
import Tracer.TracerUtilities;

import com.sun.jdi.Field;
import com.sun.jdi.Method;
import com.sun.jdi.Value;
import com.sun.jdi.VoidValue;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;

public class MethodExitManager extends VMEventsManager{
	
	public MethodExitManager(){};	
	
	public MethodExitManager(DataBaseWriter dbw)
	{
		super(dbw);
	}
	
	// Forward event for thread specific processing
    public void methodExitEvent(MethodExitEvent event) {
         

    	 Method method = event.method();
         String methodName = method.name();
         String className = TracerUtilities.getClass(method.declaringType());
         Value returnValue = event.returnValue();
         Object returnObject = null; 
         if (!(returnValue instanceof VoidValue)){ 
          returnObject = TracerUtilities.getObj(returnValue);
         }
         List<Object> arguments_this = processThis(event);
         MethodExitInfo info = new MethodExitInfo(methodName,className,returnObject,arguments_this);
         writeOutput(info);
    }
   
    private List<Object> processThis(MethodExitEvent event) {
    	
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


   
    /**
     * Returns the ThreadTrace instance for the specified thread,
	 * creating one if needed.
	 */
  
}
