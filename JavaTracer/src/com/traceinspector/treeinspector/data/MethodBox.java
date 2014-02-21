package com.traceinspector.treeinspector.data;

import java.util.List;

import com.general.model.data.MethodInfo;
import com.general.model.variables.data.*;


public class MethodBox extends Box {

	private MethodInfo method;
	
	public MethodBox(long id,String name,List<Data> arguments,	Data returnValue,Data thisValue,boolean haveChildren){
		super(id,haveChildren);
	}
	
	public MethodBox(long id, MethodInfo method, boolean haveChildren) {
		super(id,haveChildren);
		this.method = method;
	}

	public String getBoxText() {
		
		 int i=0;
		 String name = method.getMethodName() + " (";
		 List<Data> arguments = method.getArguments();
		 
		 while(i<arguments.size()){
			name= name + completeArgumentString(arguments.get(i));
	    	i++;
		 }		 
		 
		 if(i==0)
			 name = name.substring(0, name.length()) +" )";
		 else 
			 name = name.substring(0, name.length()-2) +" )";
		 
		 Data returnValue = method.getReturn_data();
		 if(returnValue != null)
			name += " -> " + returnString(returnValue);
		 
		 return name;
	} 

	private String returnString(Data var) {
		String text = "";
		
		if (var instanceof ArrayData){
			ArrayData array = ((ArrayData)var);
			text= array.getClassName() +"[ ]";
		}else if (var instanceof ObjectData){
			ObjectData object = ((ObjectData)var);
			text= object.getClassName();
		}else if (var instanceof NullData){
			text= "null" ;
		}else if (var instanceof StringData){
			StringData string = ((StringData)var);
			text= "\"" + string.getValue() + "\"" ;
		}else if (var instanceof SimpleData){
			SimpleData data = ((SimpleData)var);
			text= data.getValue().toString();		
		}else if (var instanceof IgnoredData){
			IgnoredData data = ((IgnoredData)var);
			text= data.getName().toString();	
		}
		
		return text;
	}

	public String completeArgumentString(Object var) {

		String text = "";
		
		if (var instanceof ArrayData){
			ArrayData array = ((ArrayData)var);
			text= array.getClassName() +"[ ]"+" "+ array.getName() + ", " ;
		}else if (var instanceof ObjectData){
			ObjectData object = ((ObjectData)var);
			text= object.getClassName() +" "+ object.getName() + ", " ;
		}else if (var instanceof NullData){
			text= ((NullData) var).getName() + " = null, " ;
		}else if (var instanceof StringData){
			StringData string = ((StringData)var);
			text= string.getName() + " = \"" + string.getValue() + "\", " ;
		}else if (var instanceof SimpleData){
			SimpleData data = ((SimpleData)var);
			text= data.getName() + " = " + data.getValue().toString() + ", " ;		
		}else if (var instanceof IgnoredData){
			IgnoredData data = ((IgnoredData)var);
			text = data.getClassName() +" "+ data.getName() + ", " ;
		}
		
		return text;
	}
	
	public MethodInfo getMethodInfo(){
		return this.method;
	}

}
