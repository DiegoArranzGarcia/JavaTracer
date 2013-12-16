package com.javatracer.model;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.data.ArrayInfo;
import com.javatracer.model.data.ChangeInfo;
import com.javatracer.model.data.NullObject;
import com.javatracer.model.data.ObjectInfo;
import com.javatracer.model.data.VariableInfo;

public class ChangeDetector {
	
	public List<ChangeInfo> getChangesBetween(VariableInfo variable1,VariableInfo variable2){
		return getChangesBetweenRec(variable1.getName(),variable1.getValue(),variable2.getValue());
	}

	private List<ChangeInfo> getChangesBetweenRec(String name,Object variable1,Object variable2){
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		if (variable1 instanceof ArrayInfo && variable2 instanceof ArrayInfo){
			changes = compareArrays(name,(ArrayInfo)variable1,(ArrayInfo)variable2);
		} else if (variable1 instanceof ObjectInfo && variable2 instanceof ObjectInfo){
			changes = compareObjects(name,(ObjectInfo)variable1,(ObjectInfo)variable2);
		} else if (variable1 instanceof ObjectInfo && variable2 instanceof NullObject){
			changes = getChangesDeletedObject(name);
		} else if (variable1 instanceof NullObject && variable2 instanceof ObjectInfo){
			changes = getChangesCreatedObject(name,(ObjectInfo)variable2);
		}		
		return changes;
	}
	
	private List<ChangeInfo> getChangesDeletedObject(String name) {
		
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		ChangeInfo changeInfo = new ChangeInfo(name,new NullObject());
		changes.add(changeInfo);
		
		return changes;
	}

	private List<ChangeInfo> getChangesCreatedObject(String name,ObjectInfo variable2){
		
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		ChangeInfo changeInfo = new ChangeInfo(name,variable2);
		changes.add(changeInfo);
		
		return changes;
	}

	private List<ChangeInfo> compareObjects(String name,ObjectInfo variable1, ObjectInfo variable2) {
		
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		if (variable1.getId() != variable2.getId()){
			ChangeInfo change = new ChangeInfo(name,variable2);
			changes.add(change);
		} else{
			
			List<VariableInfo> fields1 = variable1.getFields();
			List<VariableInfo> fields2 = variable2.getFields();
			
			for (int i=0;i<fields1.size();i++){
				VariableInfo field1 = fields1.get(i);
				VariableInfo field2 = fields2.get(i);
				
				if (isPrimitive(field1.getClass())){
					if (!field1.getValue().equals(field2.getValue())){
						ChangeInfo change = new ChangeInfo(name + "." + field1.getName(),field2.getValue());
						changes.add(change);
					}
				} else {
					List<ChangeInfo> fieldChanges = getChangesBetweenRec(name + "." + field1.getName() , field1.getValue(), field2.getValue());
					changes.addAll(fieldChanges);
				}
				
			}
			
		}
			
		return changes;
	}

	private List<ChangeInfo> compareArrays(String name,ArrayInfo variable1, ArrayInfo variable2) {
		
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		List<Object> values1 = variable1.getValues();
		List<Object> values2 = variable2.getValues();
		
		for (int i=0;i<values1.size();i++){
				
			Object value1 = values1.get(i);
			Object value2 = values2.get(i);
		
			if (isPrimitive(values1.get(0).getClass())){
				if (!value1.equals(value2)){
					ChangeInfo change = new ChangeInfo(name + "[" + i + "]" ,value2);
					changes.add(change);
				}
			}
				
			else {
				List<ChangeInfo> fieldChanges = getChangesBetweenRec(name + "[" + i + "]" , value1 , value2);
				changes.addAll(fieldChanges);
			}
		
		}

		return changes;
	}

	
	public boolean isPrimitive(Class<?> c) {
		  if (c.isPrimitive()) {
		    return true;
		  } else if (c == Byte.class
		          || c == Short.class
		          || c == Integer.class
		          || c == Long.class
		          || c == Float.class
		          || c == Double.class
		          || c == Boolean.class
		          || c == Character.class) {
		    return true;
		  } else {
		    return false;
		  }
	}
}
