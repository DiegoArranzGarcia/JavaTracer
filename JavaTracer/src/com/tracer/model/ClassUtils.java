package com.tracer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.general.model.FileUtilities;
import com.general.model.variables.data.ArrayData;
import com.general.model.variables.data.Data;
import com.general.model.variables.data.IgnoredData;
import com.general.model.variables.data.NullData;
import com.general.model.variables.data.ObjectData;
import com.general.model.variables.data.SimpleData;
import com.general.model.variables.data.StringData;
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
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;

public class ClassUtils {
	
	private HashMap<String,Boolean> excludedClasses;
	private List<String> excludes;
	private HashMap<String,Boolean> fieldsOfClass;
	private String classPath;
	
	/**
	 * Initialise the ClassUtils class. 
	 * @param excludes - The excluded list of packages/class it's required to use this class.
	 */
	
	public ClassUtils(String classPath,List<String> excludes){
		this.excludedClasses = new HashMap<>();
		this.fieldsOfClass = new HashMap<>();
		this.excludes = excludes;
		this.classPath = classPath;
	}

	/**
	 * Returns if the value is excluded in the current configuration.
	 * @param value - The value object to ask if it's excluded.
	 * @return True if excluded, else false.
	 */
	
	private boolean isExcludedClass(Value value) {
		boolean excluded = false;
		String type = "";
		
		if (value instanceof ObjectReference)
			type = ClassUtils.getClass(((ObjectReference)value).referenceType());
		else if (value instanceof ArrayReference)
			type = ClassUtils.getClass(((ArrayReference)value).referenceType());
		
		if (!basicType(type) && (value instanceof ObjectReference || value instanceof ArrayReference)){
			if (!excludedClasses.containsKey(type)){
				excluded = isExcluded(type);
				excludedClasses.put(type,excluded);
			} else 
				excluded = excludedClasses.get(type);
		}
		return excluded;
	}
	
	/**
	 * Returns if the class/package it's excluded in the current configuration.
	 * @param name - name of the class.
	 * @return True if excluded else false.
	 */
	
	private boolean isExcluded(String name) {
		boolean excluded = false;
		int i = 0;
		while (!excluded && i<excludes.size()){
			String exclude = excludes.get(i);
			//It's a package
			if (exclude.contains(".*")){
				excluded = name.startsWith(exclude.substring(0,exclude.length()-1));
			} else {
				excluded = name.equals(exclude);
			}
			i++;
		}		
		return excluded;
	}

	/**
	 * Returns true if the class passed as String it's a primitive class of java. 
	 * <p>
	 * All basic types are : String, Short, Integer, Long, Float, Double, Char, Byte and Boolean.
	 * @param className - The class name
	 * @return True if it's a java primitive class of java else false.
	 */
	
	private boolean basicType(String className) {
		return (className.equals("java.lang.String") || className.equals("java.lang.Integer") || className.equals("java.lang.Float") || 
				className.equals("java.lang.Double")	|| className.equals("java.lang.Char") || className.equals("java.lang.Byte") || 
				className.equals("java.lang.Short") || className.equals("java.lang.Long") || className.equals("java.lang.Boolean"));
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

				boolean getInheritFields = getInheritFields(value); 
				List<Field> allFields = value.referenceType().allFields();
				List<Field> fields = value.referenceType().fields();

				List<Data> constantData = new ArrayList<>();
				List<Data> inheritData = new ArrayList<>();
				List<Data> fieldsData = new ArrayList<>();

				for (int i=0;i<allFields.size();i++){
					Field f = allFields.get(i);
					Value v = value.getValue(f);
					Data object = null;

					if (fields.contains(f) || (!fields.contains(f) && getInheritFields)){

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

				}

				result = new ObjectData(name,objectId,constantData,inheritData,fieldsData,getClass(value.referenceType()));
			} 
			
		} else {
			result = new IgnoredData(getClass(value.referenceType()),name);
		}
		
		return result;
	}


	private boolean getInheritFields(ObjectReference value) {
		
		boolean getInherit = false;
		if (!isExcludedClass(value)){
			String className =  ClassUtils.getClass(value.referenceType());
			if (!fieldsOfClass.containsKey(className)){
				getInherit = getInheritFields(className,value);
				fieldsOfClass.put(className,getInherit);
			} else {
				getInherit = fieldsOfClass.get(className); 
			}
		}
		return getInherit;
	}

	private boolean getInheritFields(String className, ObjectReference value) {		
		JavaClass javaClass = getJavaClass(className);
		return (javaClass != null) && !basicType(className) && !isExcluded(javaClass.getSuperclassName());
	}

	private JavaClass getJavaClass(String className) {
		JavaClass jc = null;
		try {
			String path = getPathForClass(classPath,className);
			ClassParser cp = new ClassParser(path);
			jc = cp.parse();
		} catch (Exception e){
			
		}
		return jc;
	}

	private String getPathForClass(String classPath,String className) {
		String path = "";
		className = className.replace(".",FileUtilities.SEPARATOR);
		path = classPath + FileUtilities.SEPARATOR + className + ".class";
		return path;
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

	/**
	 * Gets the class name of the ReferenceType passed as an argument.
	 * @param reference 
	 * @return Name of class of the argument reference
	 */
	
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

	/**
	 * Gets the all packages which class belongs to.
	 * @param fullClassName - The name of the class to parse.
	 * @return A list of the all packages which the class belongs to.
	 */
	
	public static List<String> getPackageList(String fullClassName) {
		List<String> packages = new ArrayList<String>();
		String[] split = fullClassName.split("\\.");
		for (int i = 0;i<split.length;i++)
			packages.add(split[i]);
		packages.remove(packages.size()-1);
		return packages;
	}
	
}
