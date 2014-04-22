package com.inspector.treeinspector.data;

import java.util.List;

import com.general.model.data.MethodInfo;
import com.general.model.data.variables.ArrayData;
import com.general.model.data.variables.Data;
import com.general.model.data.variables.IgnoredData;
import com.general.model.data.variables.NullData;
import com.general.model.data.variables.ObjectData;
import com.general.model.data.variables.SimpleData;
import com.general.model.data.variables.StringData;


public class MethodBox extends Box {

	private static int MAX_LENGTH = 10;
	private static String SUSPENSION_POINTS = "...";
	private MethodInfo method;

	public MethodBox(String pathOfNode, long id, MethodInfo method, boolean haveChildren) {
		super(pathOfNode,id,haveChildren);
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
			name = name.substring(0, name.length()) +")";
		else 
			name = name.substring(0, name.length()-2) +")";

		Data returnValue = method.getReturn_data();
		if(returnValue != null) {
			String return_value =  returnString(returnValue);

			name += " -> " + return_value;
		}

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
			text = string.getValue();
			if (text.length() > MAX_LENGTH) {
				text = string.getValue().substring(0,MAX_LENGTH)+SUSPENSION_POINTS;
			}
			
			text= "\"" + text + "\"" ;
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
			String value_string = string.getValue();
			if (value_string.length() > MAX_LENGTH) {
				value_string = value_string.substring(0,MAX_LENGTH)+SUSPENSION_POINTS;
			}
			text= string.getName() + " = \"" + value_string  + "\", " ;
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

	public void setMethodInfo(MethodInfo method){
		this.method = method;
	}

}
