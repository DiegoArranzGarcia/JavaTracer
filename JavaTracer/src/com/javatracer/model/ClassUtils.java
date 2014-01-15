package com.javatracer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.javatracer.model.data.ArrayInfo;
import com.javatracer.model.data.IgnoredClass;
import com.javatracer.model.data.NullObject;
import com.javatracer.model.data.ObjectInfo;
import com.javatracer.model.data.StringInfo;
import com.javatracer.model.data.VariableInfo;
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

	public Object getObj(Value value,List<Long> objectProcessed){
		

		Object object = null;
		if (value instanceof ArrayReference){
			object = getArrayFromArrayReference((ArrayReference)value,objectProcessed);
		} else if (value instanceof PrimitiveValue){
			object = getPrimitiveObject(value);
		} else if (value instanceof StringReference){
			object = getStringFromStringReference((StringReference)value);
		} else if (value instanceof ObjectReference){
			object = getObjectFromObjectReference((ObjectReference)value,objectProcessed);
		} else if (value == null){
			object = new NullObject();
		}
		return object;
	}

	private Object getArrayFromArrayReference(ArrayReference value, List<Long> objectsProcessed) {
		
		Object object = null;
		
		if (!isExcludedClass(value)){ 
		
			long arrayID = value.uniqueID();
			if (objectsProcessed.contains(arrayID)){
				
				object = new ObjectInfo(getClass(value.referenceType())+ "[]",value.uniqueID());
				
			} else {
			
				List<Object> elements = new ArrayList<>();
				List<Value> values = value.getValues();
				objectsProcessed.add(arrayID);
				
				for (int i=0;i<values.size();i++){
					Value v = values.get(i);
					elements.add(getObj(v,objectsProcessed));
				}
				
				object = new ArrayInfo(getClass(value.referenceType()),arrayID,value.length(),elements);
			}
		
		} else {
			object = new IgnoredClass();
		}
		
		return object;
	}
	
	private Object getStringFromStringReference(StringReference value) {
		
		long stringID = value.uniqueID();
		Object object = null;
				
		object = new StringInfo(stringID,value.toString().replaceAll("\"",""));
		
		return object;
	}
	
	private Object getObjectFromObjectReference(ObjectReference value,List<Long> objectsProcessed){
		
		Object result = null;
		
		if (!isExcludedClass(value)){
			
			long objectId = value.uniqueID();
			
			if (objectsProcessed.contains(objectId)){
				result = new ObjectInfo(getClass(value.referenceType()),value.uniqueID());
			} else {
				
				objectsProcessed.add(objectId);
				List<Field> fields = value.referenceType().allFields();
				List<VariableInfo> values = new ArrayList<>();
				
				for (int i=0;i<fields.size();i++){
					Field f = fields.get(i);
					Value v = value.getValue(f);
					Object object = null;
					
					if ((v instanceof ArrayReference)){
						ArrayReference objectValue = (ArrayReference)v;
						object = getArrayFromArrayReference(objectValue,objectsProcessed);
					}
					else if (v instanceof ObjectReference && !(v instanceof StringReference)){
						ObjectReference objectValue = (ObjectReference)v;
						object = getObjectFromObjectReference(objectValue,objectsProcessed);
					}
					else {
						object = getObj(v,objectsProcessed);
					}
					
					values.add(new VariableInfo(f.name(),object));
				}
				
				result = new ObjectInfo(getClass(value.referenceType()),values,value.uniqueID());
			}		
		} else {
			result = new IgnoredClass();
		}
		return result;
	}


	private Object getPrimitiveObject(Value value) {
		Object object = null;
		if (value instanceof BooleanValue)
			object = ((BooleanValue) value).booleanValue();
		else if (value instanceof ByteValue)
			object = ((ByteValue) value).byteValue();
		else if (value instanceof CharValue)
			object = ((CharValue) value).charValue();
		else if (value instanceof DoubleValue)
			object = ((DoubleValue) value).doubleValue();
		else if (value instanceof FloatValue)
			object = ((FloatValue) value).floatValue();
		else if (value instanceof IntegerValue)
			object = ((IntegerValue) value).intValue();
		else if (value instanceof LongValue)
			object = ((LongValue) value).longValue();
		else if (value instanceof ShortValue)
			object = ((ShortValue)value).shortValue();
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
