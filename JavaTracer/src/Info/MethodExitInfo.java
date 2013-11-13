package Info;


public class MethodExitInfo implements InterfaceInfo{

	String methodName;
	String className;
	Object returnObject;
	
	public MethodExitInfo(String methodName, String className,
			Object returnObject) {
		this.methodName = methodName;
		this.className = className;
		this.returnObject = returnObject;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
