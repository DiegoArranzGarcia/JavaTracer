package com.tracer.model;

import java.util.*;

import com.general.model.variables.data.*;
import com.sun.jdi.*;

public class ClassUtils {
	
	private HashMap<String,Boolean> excludedClasses;
	private List<String> excludes;
	
	public ClassUtils(List<String> excludes){
		this.excludedClasses = new HashMap<>();
		this.excludes = simplifyExclude(excludes);
	}
	
	private List<String>simplifyExclude(List<String> p_excludes) {
		
		for (int i=0;i<p_excludes.size();i++){
			if (p_excludes.get(i).contains(".*"))
				p_excludes.get(i).replace(".*","");
		}
		return p_excludes;
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
		while (!excluded && i<excludes.size()){
			String exclude = excludes.get(i); 
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

	public Data getObj(String name,Value value,List<Long> objectProcessed){
		

		Data object = null;
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

	private Data getArrayFromArrayReference(String name,ArrayReference value, List<Long> objectsProcessed) {
		
		Data object = null;
		long arrayID = value.uniqueID();
		
		if (!isExcludedClass(value)){ 
		
			if (objectsProcessed.contains(arrayID)){
				
				object = new ObjectData(name,value.uniqueID(),null,null,null,getClass(value.referenceType())+ "[]");
				
			} else {
			
				List<Data> elements = new ArrayList<>();
				List<Value> values = value.getValues();
				objectsProcessed.add(arrayID);
				
				for (int i=0;i<values.size();i++){
					Value v = values.get(i);
					elements.add(getObj("["+i+"]",v,objectsProcessed));
				}
				
				object = new ArrayData(name,arrayID,elements,value.length(),getClass(value.referenceType()));
			}
		
		} else {
			object = new IgnoredData(getClass(value.referenceType()),name);
		}
		
		return object;
	}
	
	private Data getStringFromStringReference(String name,StringReference value) {
		
		long stringID = value.uniqueID();				
		Data object = new StringData(name,stringID,value.toString().replaceAll("\"",""));
		
		return object;
	}
	
	private Data getObjectFromObjectReference(String name,ObjectReference value,List<Long> objectsProcessed){
		
		Data result = null;
		long objectId = value.uniqueID();
		
		if (!isExcludedClass(value)){
						
			if (objectsProcessed.contains(objectId)){
				result = new ObjectData(name,objectId,null,null,null,getClass(value.referenceType()));
			} else {
				
				objectsProcessed.add(objectId);
				List<Field> allFields = value.referenceType().allFields();
				List<Field> fields = value.referenceType().fields();
				
				List<Data> constantData = new ArrayList<>();
				List<Data> inheritData = new ArrayList<>();
				List<Data> fieldsData = new ArrayList<>();
				
				for (int i=0;i<allFields.size();i++){
					Field f = allFields.get(i);
					Value v = value.getValue(f);
					Data object = null;
					
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
					
					if (fields.contains(f))
						if (f.isStatic() || f.isFinal())
							constantData.add(object);
						else
							fieldsData.add(object);
					else
						inheritData.add(object);
					
				}
				
				result = new ObjectData(name,objectId,constantData,inheritData,fieldsData,getClass(value.referenceType()));
			}		
		} else {
			result = new IgnoredData(getClass(value.referenceType()),name);
		}
		return result;
	}


	private Data getPrimitiveObject(String name, Value value) {
		Data object = null;
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
