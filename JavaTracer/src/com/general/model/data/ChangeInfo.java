package com.general.model.data;

public class ChangeInfo {
	
	String variable;
	Object value;

	public ChangeInfo(String variable,Object value){
		this.variable = variable;
		this.value = value;
	}
	public String getVariable() {
	    return variable;
    }

	public void setVariable(String variable) {
	    this.variable = variable;
    }

	public Object getValue() {
	    return value;
    }

	public void setValue(Object value) {
	    this.value = value;
    }
}
