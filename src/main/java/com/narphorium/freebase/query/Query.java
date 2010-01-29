package com.narphorium.freebase.query;

import java.util.List;

import com.narphorium.freebase.results.ResultSet;
import com.narphorium.freebase.services.ReadService;

public interface Query {
	public Parameter getParameter(String name);
	public void setParameterValue(String name, Object value);
	public void parseParameterValue(String name, String value);
	public String getName();
	public void resetParameters();
	public Object getData();
	public List<Parameter> getParameters();
	public List<Parameter> getBlankFields();
	public ResultSet buildResultSet(ReadService readService);
	public ResultSet getResultSet();
	public void setResultSet(ResultSet resultSet);
	public String toJSON();
	public boolean hasParameter(String name);
}
