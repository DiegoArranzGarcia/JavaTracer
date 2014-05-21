package com.tracer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.general.model.FileUtilities;
import com.general.model.data.variables.ArrayData;
import com.general.model.data.variables.Data;
import com.general.model.data.variables.IgnoredData;
import com.general.model.data.variables.NullData;
import com.general.model.data.variables.ObjectData;
import com.general.model.data.variables.SimpleData;
import com.general.model.data.variables.StringData;
import com.general.settings.model.Settings;
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
	private Settings config;
	
	/**
	 * Initialise the ClassUtils class. 
	 * @param excludes - The excluded list of packages/class it's required to use this class.
	 */
	
	public ClassUtils(String classPath,List<String> excludes){
		this.excludedClasses = new HashMap<>();
		this.fieldsOfClass = new HashMap<>();
		this.excludes = excludes;
		this.classPath = classPath;
		this.config = Settings.getInstance();
	}

	/**
	 * Returns if the value is excluded in the current configuration.
	 * @param value - The value object to ask if it's excluded.
	 * @return True if excluded, else false.
	 */
	
	private boolean isExcludedClass(Value value) {
		boolean excluded = false;
		String className = "";
		
		if (value instanceof ObjectReference)
			className = ClassUtils.getClass(((ObjectReference)value).referenceType());
		else if (value instanceof ArrayReference)
			className = ClassUtils.getClass(((ArrayReference)value).referenceType());
		
		if (!basicType(className) && (value instanceof ObjectReference || value instanceof ArrayReference)){
			if (!excludedClasses.containsKey(className)){
				
				excluded = (isJavaDataStructure(className) && config.isExcludedDataStructure()) || (!isJavaDataStructure(className) && isExcluded(className));
				excludedClasses.put(className,excluded);
			
			} else 
				excluded = excludedClasses.get(className);
		}
		return excluded;
	}
	
	public boolean isExcludedClass(String className){
		
		boolean excluded;
		if (!excludedClasses.containsKey(className)){
			
			excluded = (isJavaDataStructure(className) && config.isExcludedDataStructure()) || (!isJavaDataStructure(className) && isExcluded(className));
			excludedClasses.put(className,excluded);
		
		} else 
			excluded = excludedClasses.get(className);
		
		return excluded;
	}
	
	private boolean isJavaDataStructure(String className){
		return (className.equals("java.util.ArrayList") || className.equals("java.util.HashMap") || className.equals("java.util.Vector") || 
				className.equals("java.util.HashSet") ||  className.equals("java.util.HashMap$Entry"));
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
		return getObj(false,name,value,objectProcessed);
	}
	
	public Data getObj(boolean force,String name,Value value,List<Long> objectProcessed){
		
		Data object = null;
		if (value instanceof ArrayReference){
			object = getArrayFromArrayReference(force,name,(ArrayReference)value,objectProcessed);
		} else if (value instanceof PrimitiveValue){
			object = getPrimitiveObject(name,value);
		} else if (value instanceof StringReference){
			object = getStringFromStringReference(name,(StringReference)value);
		} else if (value instanceof ObjectReference){
			object = getObjectFromObjectReference(force,name,(ObjectReference)value,objectProcessed);
		} else if (value == null){
			object = new NullData(name);
		}
		return object;
	}

	private Data getArrayFromArrayReference(boolean forceWriteContent, String name,ArrayReference value, List<Long> objectsProcessed) {
		
		Data object = null;
		long arrayID = value.uniqueID();
				
		if (forceWriteContent || !isExcludedClass(value)){ 
		
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
	
	public Data getObjectFromObjectReference(boolean force, String name,ObjectReference value,List<Long> objectsProcessed){
		
		Data result = null;
		long objectId = value.uniqueID();
		
		if (force || !isExcludedClass(value)){
			
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

				boolean forceChild = isJavaDataStructure(getClass(value.referenceType())) && !config.isExcludedDataStructure();
				
				for (int i=0;i<allFields.size();i++){
					Field f = allFields.get(i);
					Value v = value.getValue(f);
					Data object = null;

					if (fields.contains(f) || (!fields.contains(f) && getInheritFields)){
						
						if ((v instanceof ArrayReference)){
							ArrayReference arrayValue = (ArrayReference)v;
							object = getArrayFromArrayReference(forceChild,f.name(),arrayValue,objectsProcessed);
						}
						else if (v instanceof ObjectReference && !(v instanceof StringReference)){
							ObjectReference objectValue = (ObjectReference)v;
							object = getObjectFromObjectReference(forceChild,f.name(),objectValue,objectsProcessed);
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
				getInherit = getInheritFields(className);
				fieldsOfClass.put(className,getInherit);
			} else {
				getInherit = fieldsOfClass.get(className); 
			}
		}
		return getInherit;
	}
	
	
	/**
	 * Returns if the super class of class is excluded or not.
	 * @param className - The complete name class.
	 * @return True if the super class is not excluded, else false.
	 */
	
	private boolean getInheritFields(String className) {		
		JavaClass javaClass = getJavaClass(className);
		return (javaClass != null) && !basicType(className) && !isExcluded(javaClass.getSuperclassName());
	}

	/**
	 * Gets the JavaClass (File) of the class passed as argument.
	 * The path that will look for is classpath + class name.
	 * @param className - The complete name of the class.
	 * @return A JavaClass, if not founded it will return null.
	 */
	
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

	/**
	 * Transforms the class path and class name into a correct directory.
	 * @param classPath - The execution class path
	 * @param className - The class we are looking for.
	 * @return A String of the location of the ".class" file of the className
	 */
	
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
