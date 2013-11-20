package Info;

import java.util.List;


public class MethodExitInfo implements InterfaceInfo{

	String methodName;
	String className;
	Object returnObject;
	List<Object> ArgumentsThis;
	List<Object> arguments;
	
	public MethodExitInfo(String methodName, String className,
			Object returnObject,List<Object> value, List<Object> Values_this) {
		this.methodName = methodName;
		this.className = className;
		this.returnObject = returnObject;
		this.ArgumentsThis=Values_this;
		this.arguments = value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
