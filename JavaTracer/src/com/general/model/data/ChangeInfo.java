package com.general.model.data;

import com.general.model.data.variables.Data;

public class ChangeInfo {
	
	String variable;
	Data value;

	public ChangeInfo(String variable,Data value){
		this.variable = variable;
		this.value = value;
	}
	public String getVariable() {
	    return variable;
    }

	public void setVariable(String variable) {
	    this.variable = variable;
    }

	public Data getValue() {
	    return value;
    }

	public void setValue(Data value) {
	    this.value = value;
    }
}
