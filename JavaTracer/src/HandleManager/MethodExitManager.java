package HandleManager;

import DataBase.DataBaseWriter;
import DataBase.MethodExitInfo;
import Tracer.TracerUtilities;

import com.sun.jdi.Method;
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
         Method method = event.method();
         String methodName = method.name();
         String className = TracerUtilities.getClass(method.declaringType());
         Value returnValue = event.returnValue();
         Object returnObject = null; 
         if (!(returnValue instanceof VoidValue)){ 
          returnObject = TracerUtilities.getObj(returnValue);
         }
         MethodExitInfo info = new MethodExitInfo(methodName,className,returnObject);
         writeOutput(info);
    }
   
   
    /**
     * Returns the ThreadTrace instance for the specified thread,
	 * creating one if needed.
	 */
  
}
