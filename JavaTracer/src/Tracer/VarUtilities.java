package Tracer;

import java.util.List;
import java.util.Map;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.Field;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.StringReference;
import com.sun.jdi.Value;

public class VarUtilities {
	
	public static Object getObj(Value value){
		Object object = null;
		if (value instanceof ArrayReference){
			ArrayReference arrayReference = (ArrayReference)value;
			List<Value> arrayValues = arrayReference.getValues();
			Object[] array = new Object[arrayValues.size()];
			for (int i=0;i<arrayValues.size();i++){
				array[i] = VarUtilities.getObj(arrayValues.get(i));
			}
			object = array;
		} else if (value instanceof PrimitiveValue){
			object = getPrimitiveObject(value);//TODO: All primitives values
		} else if (value instanceof StringReference){
			object = (String)value.toString();
		} else if (value instanceof ObjectReference){
			object = getObject((ObjectReference)value);
		} 
		return object;
	}
	

	private static Object getObject(ObjectReference value) {
		List<Field> fields = value.referenceType().allFields();
		Map<Field, Value> values = value.referenceType().getValues(fields);
		
		return null;
	}


	private static Object getPrimitiveObject(Value value) {
		Object object = null;
		if (value instanceof IntegerValue) object = ((IntegerValue) value).intValue();
		return object;
	}

}
