

import java.util.Map;

public class ObjectInfo {

	private String className;
	private Map<String,Object> fields;
	private long id;
	
	public ObjectInfo(String className,Map<String,Object> values,long id){
		this.className = className;
		this.fields = values;
		this.id = id;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the fields
	 */
	public Map<String,Object> getFields() {
		return fields;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Map<String,Object>  fields) {
		this.fields = fields;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
}
