package com.narphorium.freebase.query;

import java.util.HashMap;
import java.util.Map;


public class Parameter {
	
	public static interface TypeConverter {
		public Object convert(String data);
	}
	
	private static Map<String, TypeConverter> convertersByType = new HashMap<String, TypeConverter>();
	static {
		convertersByType.put("/type/text", new TypeConverter(){
			public Object convert(String data) {
				return data;
			}});
		/*convertersByType.put("/type/datetime", new Converter(){
			private DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
			public Object convert(String data) {
				try {
					return dateFormat.parse(data);
				} catch (ParseException e) {}
				return null;
			}});*/
		convertersByType.put("/type/float", new TypeConverter(){
			public Object convert(String data) {
				return Double.parseDouble(data);
			}});
		convertersByType.put("/type/integer", new TypeConverter(){
			public Object convert(String data) {
				return Long.parseLong(data);
			}});
		convertersByType.put("/type/boolean", new TypeConverter(){
			public Object convert(String data) {
				return Boolean.parseBoolean(data);
			}});
	}
	
	private String name;
	private String id;
	private String expectedType;
	private TypeConverter valueConverter;
	private Object defaultValue;
	private JsonPath path;
	//private List<JsonPath> paths = new ArrayList<JsonPath>();
	
	public Parameter(String name, String id, Object defaultValue) {
		this.name = name;
		this.id = id;
		this.defaultValue = defaultValue;
	}

	public Parameter(Parameter parameter) {
		this.name = parameter.name;
		this.id = parameter.id;
		this.expectedType = parameter.expectedType;
		this.valueConverter = parameter.valueConverter;
		this.defaultValue = parameter.defaultValue;
		//this.paths.addAll(parameter.getPaths());
		this.path = parameter.path;
	}

	public synchronized String getName() {
		return name;
	}

	/*public List<JsonPath> getPaths() {
		return paths;
	}*/
	
	public JsonPath getPath() {
		return path;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public synchronized String getId() {
		return id;
	}
	
	public synchronized String getKey() {
		return name + ":" + id;
	}

	public Object parseValue(String data) {
		return valueConverter.convert(data);
	}

	public synchronized String getExpectedType() {
		return expectedType;
	}
	
	/*public void addPath(JsonPath path) {
		this.paths.add(path);
	}*/
	
	public void setPath(JsonPath path) {
		this.path = path;
	}

	public synchronized void setExpectedType(String expectedType) {
		this.expectedType = expectedType;
		this.valueConverter = convertersByType.get(this.expectedType);
		if (this.valueConverter == null) {
			this.valueConverter = new TypeConverter() {
				public Object convert(String data) {
					return data;
				}
			};
		}
	}
}
