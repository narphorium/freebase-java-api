package com.narphorium.freebase.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonPath {
	
	private List<Object> elements = new ArrayList<Object>();
	
	public JsonPath() {}
	
	// TODO Add parsing constructor
	/*public JsonPath(String text) {
		
	}*/
	
	public JsonPath(JsonPath path) {
		this.elements.addAll(path.elements);
	}

	public void addElement(Object element) {
		elements.add(element);
	}
	
	public Object getValue(Object data) {
		List<Object> parameterResults = new ArrayList<Object>();

			boolean found = true;
			for (Object key : elements) {
				if (key instanceof String && data instanceof Map) {
					data = ((Map<String, Object>)data).get((String)key);
				} else if (key instanceof Integer && data instanceof List) {
					data = ((List<Object>)data).get((Integer)key);
				} else {
					found = false;
				}
				//System.out.println("PARAM " + name + " : " + key + " => " + resultData);
			}
			if (found) {
				parameterResults.add(data);
			}

		return parameterResults.size() == 1 ? parameterResults.get(0) : parameterResults;
	}
	
	public void setValue(Object data, Object value) {
		for (int k = 0; k < elements.size(); k++) {
			Object key = elements.get(k);
			if (key instanceof String) {
				if (k < elements.size() - 1) {
					data = ((Map<String, Object>)data).get((String)key);
				} else {
					((Map<String, Object>)data).put((String)key, value);
				}
			} else if (key instanceof Integer) {
				data = ((List<Object>)data).get((Integer)key);
			}
			//System.out.println("PARAM " + name + " : " + key + " => " + currentData);
		}
	}
	
	public String toString() {
		String path = "";
		for (Object element : elements) {
			if (element instanceof Integer) {
				path += "[" + element.toString() + "]";
			} else {
				if (path.length() > 0) {
					path += ".";
				}
				path += element.toString();
			}
		}
		return path;
	}
}
