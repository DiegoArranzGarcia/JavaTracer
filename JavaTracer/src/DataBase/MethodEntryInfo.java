package DataBase;

import java.util.List;

public class MethodEntryInfo {

	String methodName;
	String calledFromClass;
	List<Object> arguments;
	
	public MethodEntryInfo(String methodName, String calledFromClass, List<Object> value){
		this.methodName = methodName;
		this.calledFromClass = calledFromClass; 
		this.arguments = value;		
	}
	
}
