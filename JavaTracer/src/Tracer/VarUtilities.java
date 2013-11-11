package Tracer;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.sun.jdi.ShortValue;
import com.sun.jdi.StringReference;
import com.sun.jdi.Value;

public class VarUtilities {
	
	public static Object getObj(Value value){
		Object object = null;
		if (value instanceof ArrayReference){
			ArrayReference arrayReference = (ArrayReference)value;
			List<Value> arrayValues = arrayReference.getValues();
			Object array = Array.newInstance(String.class,arrayValues.size());
			for (int i=0;i<arrayValues.size();i++){
				Array.set(array, i, VarUtilities.getObj(arrayValues.get(i)));
			}
			object = array;
		} else if (value instanceof PrimitiveValue){
			object = getPrimitiveObject(value);
		} else if (value instanceof StringReference){
			object = (String)value.toString().replaceAll("\"","");
		} else if (value instanceof ObjectReference){
			object = getObjectFromReference((ObjectReference)value);
		} 
		return object;
	}
	

	private static Object getObjectFromReference(ObjectReference value) {
		List<Field> fields = value.referenceType().allFields();
		Map<String,Object> values = new HashMap<>();
		for (int i=0;i<fields.size();i++){
			Field f = fields.get(i);
			Value v = value.getValue(f);
			Object object = null;
			if (v instanceof ObjectReference && ! (v instanceof StringReference)) object = ((ObjectReference)v).uniqueID(); 
			else object = VarUtilities.getObj(v);
			values.put(f.name(),object);
		}
		Object result = new ObjectInfo("class",values,value.uniqueID());
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

}
