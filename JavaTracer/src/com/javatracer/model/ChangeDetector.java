package com.javatracer.model;

import java.util.ArrayList;
import java.util.List;

import com.general.model.variables.data.ArrayData;
import com.general.model.variables.data.Data;
import com.general.model.variables.data.IgnoredData;
import com.general.model.variables.data.NullData;
import com.general.model.variables.data.ObjectData;
import com.general.model.variables.data.SimpleData;
import com.general.model.variables.data.StringData;
import com.javatracer.model.methods.data.ChangeInfo;

/**
 * Class used to detect changes between Data objects. The changes are return in
 * ChangeInfo objects.
 */

public class ChangeDetector {

	/**
	 * This method returns the changes between the two variables. The changes are made by comparing 
	 * the second variable in respect of the first variable. Depending on the type of the variables,
	 * the changes are evaluated in different ways. If the two variables are primitive values, no changes
	 * are notified. 
	 * 
	 * @see {@link #compareArrays(String, ArrayData, ArrayData)} </br>
	 * @see {@link #compareObjects(String, ObjectData, ObjectData)} </br>
	 * @see {@link #getChangesCreatedObject(String, ObjectData)} </br> 
	 * @see {@link #getChangesDeletedObject(String)}
	 * 
	 * @param variable1 - The variable with is compared the second one.
	 * @param variable2 - The second variable.
	 * 
	 * @return A list of the changes between the two variables.
	 */
	
	public List<ChangeInfo> getChangesBetween(Data variable1,Data variable2){
		return getChangesBetweenRec(variable1.getName(),variable1,variable2);
	}

	/**
	 * This method is used for getting the changes of objects recursively.
	 * 
	 * @param name - The name of the variable.
	 * @param variable1 - The value of the first variable.
	 * @param variable2 - The value fo the second variable.
	 * 
	 * @return A list of the changes between the two variables.
	 */
	
	private List<ChangeInfo> getChangesBetweenRec(String name,Data variable1,Data variable2){
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		if (variable1 instanceof ArrayData && variable2 instanceof ArrayData){
			changes = compareArrays(name,(ArrayData)variable1,(ArrayData)variable2);
		} else if (variable1 instanceof StringData && variable2 instanceof StringData){
			changes = compareStrings(name,(StringData)variable1,(StringData)variable2);
		} else if (variable1 instanceof StringData && variable2 instanceof NullData){
			changes = getChangesDeletedString(name);
		} else if (variable1 instanceof NullData && variable2 instanceof StringData){
			changes = getChangesCreatedString(name,(StringData)variable2);
		} else if (variable1 instanceof ObjectData && variable2 instanceof ObjectData){
			changes = compareObjects(name,(ObjectData)variable1,(ObjectData)variable2);
		} else if (variable1 instanceof ObjectData && variable2 instanceof NullData){
			changes = getChangesDeletedObject(name);
		} else if (variable1 instanceof NullData && variable2 instanceof ObjectData){
			changes = getChangesCreatedObject(name,(ObjectData)variable2);
		} else if (variable1 instanceof IgnoredData || variable2 instanceof IgnoredData){
			changes = new ArrayList<>();
		}
		
		return changes;
	}
	
	private List<ChangeInfo> getChangesSimpleData(String name,SimpleData variable1,SimpleData variable2) {
		
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		if (!variable1.getValue().equals(variable2.getValue())){
			ChangeInfo changeInfo = new ChangeInfo(name,variable2);
			changes.add(changeInfo);
		}
		
		return changes;
	}

	private List<ChangeInfo> getChangesDeletedString(String name) {
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		ChangeInfo changeInfo = new ChangeInfo(name,new NullData(name));
		changes.add(changeInfo);
		return changes;
	}

	private List<ChangeInfo> getChangesCreatedString(String name, StringData variable2) {
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		ChangeInfo changeInfo = new ChangeInfo(name,variable2);
		changes.add(changeInfo);
		return changes;
	}

	private List<ChangeInfo> compareStrings(String name, StringData variable1, StringData variable2) {
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		if (!variable1.getValue().equals(variable2.getValue())){
			ChangeInfo change = new ChangeInfo(name,variable2);
			changes.add(change);
		}
		return changes;
	}

	/**
	 * Returns the changes when the object has been deleted.
	 * 
	 * @param name - The name of the variable.
	 * 
	 * @return A list where there is a change of the object deleted.
	 */
	
	private List<ChangeInfo> getChangesDeletedObject(String name) {
		
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		ChangeInfo changeInfo = new ChangeInfo(name,new NullData(name));
		changes.add(changeInfo);
		
		return changes;
	}

	
	/**
	 * Returns the changes when the object is created.
	 * 
	 * @param name - Name of the variable
	 * @param variable2 - The value of the object created.
	 * @return The list of the changes of the created object.
	 */
	
	private List<ChangeInfo> getChangesCreatedObject(String name,ObjectData variable2){
		
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		ChangeInfo changeInfo = new ChangeInfo(name,variable2);
		changes.add(changeInfo);
		
		return changes;
	}
	
	/**
	 * Returns the changes between the two objects.
	 * If the id changes, the attributes of the object and the id are included on the changes list.
	 * If the id doesnt changes, the attributes are compared one by one.
	 *  
	 * @param name - The name of the variable.
	 * @param variable1 - The first object.
	 * @param variable2 - The second object.
	 * @return A list with the changes listed above.
	 */
	
	private List<ChangeInfo> compareObjects(String name,ObjectData variable1, ObjectData variable2) {
		
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		if (variable1.getId() != variable2.getId()){
			ChangeInfo change = new ChangeInfo(name,variable2);
			changes.add(change);
		} else{
			
			List<Data> fields1 = variable1.getValue();
			List<Data> fields2 = variable2.getValue();
			
			if (fields1!=null){
				
				for (int i=0;i<fields1.size();i++){
					
					Data field1 = fields1.get(i);
					Data field2 = fields2.get(i);
					
					List<ChangeInfo> fieldChanges;
					
					if (field1 instanceof SimpleData && field2 instanceof SimpleData)
						fieldChanges = getChangesSimpleData(name + "." + field1.getName(),(SimpleData)field1,(SimpleData)field2);
					else 
						fieldChanges = getChangesBetweenRec(name + "." + field1.getName() , field1, field2);
					
					changes.addAll(fieldChanges);
									
				}
				
			}
			
		}
			
		return changes;
	}

	/**
	 * Returns the changes between the two arrays. First of all if size of the arrays are different and the first
	 * size is less than the second is because in the second array has been resized with more values. This changes are 
	 * written directly. If the size is equal or less the changes are compared one by one until the size of the first
	 * array.
	 *
	 * @param name - The name of the variable.
	 * @param variable1 - The value of the first array.
	 * @param variable2 - The value of the second array.
	 * @return A list with the changes between the two arrays.
	 */
	
	private List<ChangeInfo> compareArrays(String name,ArrayData variable1, ArrayData variable2) {
		
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		List<Data> values1 = variable1.getValue();
		List<Data> values2 = variable2.getValue();
		
		if (variable1.getLength()<variable2.getLength()){
			
			changes = compareTo(name, values1, values2,variable1.getLength());
			
			for (int i=variable1.getLength();i<variable2.getLength();i++){
				ChangeInfo change = new ChangeInfo(name + "[" + i + "]" ,values2.get(i));
				changes.add(change);
			}
			
			ChangeInfo changeSize = new ChangeInfo(name + ".lenght",variable2.getLength());
			changes.add(changeSize);
						
		} else if (variable2.getLength()<variable1.getLength()) {
			
			changes = compareTo(name, values1, values2,variable2.getLength());
			
			ChangeInfo changeSize = new ChangeInfo(name + ".lenght",variable2.getLength());
			changes.add(changeSize);
			
		} else {
			
			changes = compareTo(name,values1,values2,variable1.getLength());
			
		}

		return changes;
	}

	/**
	 * Private method to compare arrays. It compares the arrays from the position 0 to the position given in the last argument
	 * and returns the changes from the 0 position to the position given.
	 * 
	 * @param name - The name of the value.
	 * @param values1 - The list of values of the first array.
	 * @param values2 - The list of values of the second array.
	 * @param to - The position limit of the array.
	 * @return A list with the changes in the positions (0-to)
	 */
	
	private List<ChangeInfo> compareTo(String name,List<Data> values1,List<Data> values2, int to) {
		
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		for (int i=0;i<to;i++){
			
			Data value1 = values1.get(i);
			Data value2 = values2.get(i);
		
			List<ChangeInfo> fieldChanges;
				
			if (value1 instanceof SimpleData || value2 instanceof SimpleData)
				fieldChanges = getChangesSimpleData(name + "[" + i + "]", (SimpleData)value1, (SimpleData)value2);
			else
				fieldChanges = getChangesBetweenRec(name + "[" + i + "]" , value1 , value2);				
				
			changes.addAll(fieldChanges);
						
		}
		
		return changes;
	}
	
	/**
	 * Returns if the class that are passed as argument is a primite value or not.
	 * @param c - The Class passed.
	 * @return - True if it's primitive, false if not.
	 */
	
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
