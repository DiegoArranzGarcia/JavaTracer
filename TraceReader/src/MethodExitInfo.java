

import java.util.List;


public class MethodExitInfo implements InterfaceInfo{

	String methodName;
	String className;
	Object returnObject;
	List<Object> ArgumentsThis;
	
	public MethodExitInfo(String methodName, String className,
			Object returnObject, List<Object> Values_this) {
		this.methodName = methodName;
		this.className = className;
		this.returnObject = returnObject;
		this.ArgumentsThis=Values_this;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
