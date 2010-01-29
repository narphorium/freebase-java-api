package com.narphorium.freebase.results;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.narphorium.freebase.query.DefaultQuery;
import com.narphorium.freebase.query.JsonPath;
import com.narphorium.freebase.query.Parameter;
import com.narphorium.freebase.query.Query;

public class DefaultResult implements Result {

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
	
	private Query query;
	private Object jsonData;

	public DefaultResult(Query query, Object jsonData) {
		this.query = new DefaultQuery(query);
		this.jsonData = jsonData;
	}
	
	public Object getObject(JsonPath path) {
		return (Object)path.getValue(jsonData);
	}

	public Object getObject(String variable) {
		Parameter parameter = query.getParameter(variable);
		if (parameter == null) {
			System.out.println("ERROR: Parameter \"" + variable + "\" does not exist.");
			return null;
		}
		return getObject(parameter.getPath());
	}
	
	public List<Object> getCollection(String variable) {
		return (List<Object>)getObject(variable);
	}

	public boolean getBoolean(String variable) {
		return (Boolean)getObject(variable);
	}

	public Date getDate(String variable) {
		try {
			return dateFormat.parse(getString(variable));
		} catch (ParseException e) {}
		return null;
	}

	public float getFloat(String variable) {
		return (Float)getObject(variable);
	}

	public int getInteger(String variable) {
		return (Integer)getObject(variable);
	}

	public String getString(String variable) {
		return (String)getObject(variable);
	}

	public Query getQuery() {
		return query;
	}
}
