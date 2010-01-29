package com.narphorium.freebase.results;

import java.util.Date;
import java.util.List;

import com.narphorium.freebase.query.JsonPath;
import com.narphorium.freebase.query.Query;

public interface Result {
	public Object getObject(JsonPath path);
	public Object getObject(String variable);
	public String getString(String variable);
	public boolean getBoolean(String variable);
	public int getInteger(String variable);
	public float getFloat(String variable);
	public Date getDate(String variable);
	public List<Object> getCollection(String variable);
	public Query getQuery();
}
