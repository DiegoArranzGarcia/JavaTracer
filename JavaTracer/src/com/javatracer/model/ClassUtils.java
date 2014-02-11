package com.javatracer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.javatracer.model.variables.data.ArrayData;
import com.javatracer.model.variables.data.IgnoredData;
import com.javatracer.model.variables.data.NullData;
import com.javatracer.model.variables.data.ObjectData;
import com.javatracer.model.variables.data.SimpleData;
import com.javatracer.model.variables.data.StringData;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.BooleanValue;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharValue;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.Field;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.LongValue;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StringReference;
import com.sun.jdi.Value;

public class ClassUtils {
	
	private HashMap<String,Boolean> excludedClasses;
	private String[] excludes;
	
	public ClassUtils(String[] excludes){
		this.excludedClasses = new HashMap<>();
		this.excludes = simplifyExclude(excludes);
	}
	
	private String[] simplifyExclude(String[] excludes) {
		
		for (int i=0;i<excludes.length;i++){
			if (excludes[i].contains(".*"))
				excludes[i].replace(".*","");
		}
		return excludes;
	}

	private boolean isExcludedClass(Value value) {
		boolean excluded = false;
		String type = "";
		
		if (value instanceof ObjectReference)
			type = ClassUtils.getClass(((ObjectReference)value).referenceType());
		else if (value instanceof ArrayReference)
			type = ClassUtils.getClass(((ArrayReference)value).referenceType());
		
		if (!basicType(type) && (value instanceof ObjectReference || value instanceof ArrayReference)){
			if (!excludedClasses.containsKey(type)){
				excluded = isExcludedType(type);
				excludedClasses.put(type,excluded);
			} else 
				excluded = excludedClasses.get(type);
		}
		return excluded;
	}
	
	private boolean isExcludedType(String type) {
		String[] packages = getPackages(type);
		boolean excluded = false;
		int i = 0;
		while (!excluded && i<excludes.length){
			String exclude = excludes[i];
			int j = 0;
			while (!excluded && j<packages.length){
				excluded = exclude.contains(packages[j]);
				j++;
			}
			i++;
		}		
		return excluded;
	}

	private String[] getPackages(String type) {
		String[] split = type.split("\\.");
		if (split.length != 0){
			String completePackage = split[0];
			for (int i=1;i<split.length;i++){
				String aux = split[i];
				split[i] = completePackage + "." + aux;
				completePackage = split[i];
			}
		} else {
			split = new String[]{type};
		}
		return split;
	}

	private boolean basicType(String type) {
		return (type.equals("java.lang.String") || type.equals("java.lang.Integer") || type.equals("java.lang.Float") || 
				type.equals("java.lang.Double")	|| type.equals("java.lang.Char") || type.equals("java.lang.Byte") || 
				type.equals("java.lang.Short") || type.equals("java.lang.Long") || type.equals("java.lang.Boolean"));
	}

	public Object getObj(String name,Value value,List<Long> objectProcessed){
		

		Object object = null;
		if (value instanceof ArrayReference){
			object = getArrayFromArrayReference(name,(ArrayReference)value,objectProcessed);
		} else if (value instanceof PrimitiveValue){
			object = getPrimitiveObject(name,value);
		} else if (value instanceof StringReference){
			object = getStringFromStringReference(name,(StringReference)value);
		} else if (value instanceof ObjectReference){
			object = getObjectFromObjectReference(name,(ObjectReference)value,objectProcessed);
		} else if (value == null){
			object = new NullData(name);
		}
		return object;
	}

	private Object getArrayFromArrayReference(String name,ArrayReference value, List<Long> objectsProcessed) {
		
		Object object = null;
		long arrayID = value.uniqueID();
		
		if (!isExcludedClass(value)){ 
		
			if (objectsProcessed.contains(arrayID)){
				
				object = new ObjectData(name,value.uniqueID(),null,getClass(value.referenceType())+ "[]");
				
			} else {
			
				List<Object> elements = new ArrayList<>();
				List<Value> values = value.getValues();
				objectsProcessed.add(arrayID);
				
				for (int i=0;i<values.size();i++){
					Value v = values.get(i);
					elements.add(getObj("["+i+"]",v,objectsProcessed));
				}
				
				object = new ArrayData(name,arrayID,elements,value.length(),getClass(value.referenceType()));
			}
		
		} else {
			object = new IgnoredData(name);
		}
		
		return object;
	}
	
	private Object getStringFromStringReference(String name,StringReference value) {
		
		long stringID = value.uniqueID();
		Object object = null;
				
		object = new StringData(name,stringID,value.toString().replaceAll("\"",""));
		
		return object;
	}
	
	private Object getObjectFromObjectReference(String name,ObjectReference value,List<Long> objectsProcessed){
		
		Object result = null;
		long objectId = value.uniqueID();
		
		if (!isExcludedClass(value)){
						
			if (objectsProcessed.contains(objectId)){
				result = new ObjectData(name,objectId,null,getClass(value.referenceType()));
			} else {
				
				objectsProcessed.add(objectId);
				List<Field> fields = value.referenceType().allFields();
				List<Object> values = new ArrayList<>();
				
				for (int i=0;i<fields.size();i++){
					Field f = fields.get(i);
					Value v = value.getValue(f);
					Object object = null;
					
					if ((v instanceof ArrayReference)){
						ArrayReference objectValue = (ArrayReference)v;
						object = getArrayFromArrayReference(f.name(),objectValue,objectsProcessed);
					}
					else if (v instanceof ObjectReference && !(v instanceof StringReference)){
						ObjectReference objectValue = (ObjectReference)v;
						object = getObjectFromObjectReference(f.name(),objectValue,objectsProcessed);
					}
					else {
						object = getObj(f.name(),v,objectsProcessed);
					}
					
					values.add(object);
				}
				
				result = new ObjectData(name,objectId,values,getClass(value.referenceType()));
			}		
		} else {
			result = new IgnoredData(name);
		}
		return result;
	}


	private Object getPrimitiveObject(String name, Value value) {
		Object object = null;
		if (value instanceof BooleanValue)
			object = new SimpleData(name,((BooleanValue) value).booleanValue());
		else if (value instanceof ByteValue)
			object = new SimpleData(name,((ByteValue) value).byteValue());
		else if (value instanceof CharValue)
			object = new SimpleData(name,((CharValue) value).charValue());
		else if (value instanceof DoubleValue)
			object = new SimpleData(name,((DoubleValue) value).doubleValue());
		else if (value instanceof FloatValue)
			object = new SimpleData(name,((FloatValue) value).floatValue());
		else if (value instanceof IntegerValue)
			object = new SimpleData(name,((IntegerValue) value).intValue());
		else if (value instanceof LongValue)
			object = new SimpleData(name,((LongValue) value).longValue());
		else if (value instanceof ShortValue)
			object = new SimpleData(name,((ShortValue)value).shortValue());
		return object;
	}

	public static String getClass(ReferenceType reference) {
		String className = "";
		String[] aux = reference.toString().split(" ");
		boolean found = false;
		int i = 0;
		while (!found && i<aux.length){
			if (aux[i].equals("class")){
				className = aux[i+1];
				found = true;
			}
			i++;
		}
		if (className.contains("[]")) 
			className = className.replace("[]", "");
		return className;
	}
	
}
