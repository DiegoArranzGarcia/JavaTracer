package DataBase;

import java.util.List;

public class MethodEntryInfo {

	String methodName;
	String calledFromClass;
	List<Object> arguments;
	Object returnObject;
	
	public MethodEntryInfo(String methodName, String calledFromClass, List<Object> value,
	Object returnObject){
		this.methodName = methodName;
		this.calledFromClass = calledFromClass; 
		this.arguments = value;
		this.returnObject = returnObject;
		
	}
	
}
