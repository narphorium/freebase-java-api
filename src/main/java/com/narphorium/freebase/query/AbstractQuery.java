package com.narphorium.freebase.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stringtree.json.JSONWriter;

import com.narphorium.freebase.results.ResultSet;

public abstract class AbstractQuery implements Query {

	protected String name;
	protected List<Parameter> parameters = new ArrayList<Parameter>();
	protected List<Parameter> blankFields = new ArrayList<Parameter>();
	protected Map<String, Parameter> parametersByName = new HashMap<String, Parameter>();
	protected Object data;
	private ResultSet resultSet;

	public AbstractQuery(String name, Object data, List<Parameter> parameters, List<Parameter> blankFields) {
		this.name = name;
		this.data = data;
		for (Parameter parameter : parameters) {
			this.parameters.add(parameter);
			this.parametersByName.put(parameter.getName(), parameter);
		}
		this.blankFields.addAll(blankFields);
	}

	public AbstractQuery(Query query) {
		this.name = query.getName();
		this.data = copyData(query.getData());
		for (Parameter parameter : query.getParameters()) {
			this.parameters.add(parameter);
			this.parametersByName.put(parameter.getName(), new Parameter(parameter));
		}
		this.blankFields.addAll(query.getBlankFields());
	}

	protected Object copyData(Object data) {
		if (data instanceof Map) {
			Map<String, Object> mapData = (Map<String, Object>)data;
			Map<String, Object> map = new HashMap<String, Object>();
			for (String key : mapData.keySet()) {
				map.put(key, copyData(mapData.get(key)));
			}
			return map;
		} else if (data instanceof List) {
			List<Object> listData = (List<Object>)data;
			List<Object> list = new ArrayList<Object>();
			for (Object element : listData) {
				list.add(copyData(element));
			}
			return list;
		} else {
			return data;
		}
	}

	public String getName() {
		return name;
	}

	public Object getData() {
		return data;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}
	
	public boolean hasParameter(String name) {
		return parametersByName.containsKey(name);
	}
	
	public Parameter getParameter(String name) {
		return parametersByName.get(name);
	}
	
	public void resetParameters() {
		for (Parameter parameter : parameters) {
			setParameterValue(parameter.getName(), parameter.getDefaultValue());
		}
	}
	
	public void setParameterValue(String name, Object value) {
		Parameter parameter = parametersByName.get(name);
		if (parameter == null) {
			System.out.println("ERROR: Parameter \"" + name + "\" does not exist.");
			return;
		}
		Object topData = data;
		if (topData instanceof List) {
			topData = ((List<Object>)topData).get(0);
		}
		parameter.getPath().setValue(topData, value);
		/*for (JsonPath path : parameter.getPaths()) {
			path.setValue(topData, value);
		}*/
	}
	
	public List<Parameter> getBlankFields() {
		return blankFields;
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
	public String toJSON() {
		JSONWriter writer = new JSONWriter();
		String query = writer.write(data);
		query = query.replaceAll("\\\\/", "/");
		return query;
	}

}