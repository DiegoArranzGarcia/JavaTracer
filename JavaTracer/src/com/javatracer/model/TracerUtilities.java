package com.javatracer.model;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.data.ArrayInfo;
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

public class TracerUtilities {
	

	public static Object getObj(Value value,List<Long> objectProcessed){
		Object object = null;
		if (value instanceof ArrayReference){
			object = getArrayFromArrayReference((ArrayReference)value,objectProcessed);
		} else if (value instanceof PrimitiveValue){
			object = getPrimitiveObject(value);
		} else if (value instanceof StringReference){
			//object = (String)value.toString().replaceAll("\"","");
			object = getStringFromStringReference((StringReference)value,objectProcessed);
		} else if (value instanceof ObjectReference){
			object = getObjectFromObjectReference((ObjectReference)value,objectProcessed);
		} else if (value == null){
			object = new NullObject();
		}
		return object;
	}

	private static Object getArrayFromArrayReference(ArrayReference value, List<Long> objectsProcessed) {
		
		long arrayID = value.uniqueID();
		Object object = null;
		
		if (objectsProcessed.contains(arrayID)){
			object = new ObjectInfo(getClass(value.referenceType())+ "[]",value.uniqueID());
		}
		
		List<Object> elements = new ArrayList<>();
		List<Value> values = value.getValues();
		objectsProcessed.add(arrayID);
		
		for (int i=0;i<values.size();i++){
			Value v = values.get(i);
			elements.add(getObj(v,objectsProcessed));
		}
		
		object = new ArrayInfo(getClass(value.referenceType()),arrayID,value.length(),elements);
		
		return object;
	}
	
	private static Object getStringFromStringReference(StringReference value, List<Long> objectsProcessed) {
		
		long stringID = value.uniqueID();
		Object object = null;
		
		if (objectsProcessed.contains(stringID)){
			object = new ObjectInfo(getClass(value.referenceType())+ "[]",value.uniqueID());
		}
		
		objectsProcessed.add(stringID);

		
		object = new StringInfo(stringID,value.toString().replaceAll("\"",""));
		
		return object;
	}
	
	private static Object getObjectFromObjectReference(ObjectReference value,List<Long> objectsProcessed){
		long objectId = value.uniqueID();
		Object result = null;
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
				else object = TracerUtilities.getObj(v,objectsProcessed);
				values.add(new VariableInfo(f.name(),object));
			}
			result = new ObjectInfo(getClass(value.referenceType()),values,value.uniqueID());
		}		
		return result;
	}


	private static Object getPrimitiveObject(Value value) {
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

	public static String getClass(ReferenceType declaringType) {
		String className = "";
		String[] aux = declaringType.toString().split(" ");
		boolean found = false;
		int i = 0;
		while (!found && i<aux.length){
			if (aux[i].equals("class")){
				className = aux[i+1];
				found = true;
			}
			i++;
		}
		if (className.contains("[]")) className = className.replace("[]", "");
		return className;
	}
	
}
