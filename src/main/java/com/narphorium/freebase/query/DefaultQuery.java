package com.narphorium.freebase.query;

import java.io.File;
import java.util.List;

import com.narphorium.freebase.query.io.QueryParser;
import com.narphorium.freebase.results.DefaultResultSet;
import com.narphorium.freebase.results.ResultSet;
import com.narphorium.freebase.services.ReadService;

public class DefaultQuery extends AbstractQuery implements Query {
	
	public DefaultQuery(String name, Object data, List<Parameter> parameters, List<Parameter> blankFields) {
		super(name, data, parameters, blankFields);
		
		/*for (Parameter parameter : parameters) {
			System.out.print(parameter.getName());
			for (Object[] path : parameter.getPaths()) {
				System.out.print(" - ");
				for (Object e : path) {
					System.out.print("/" + e);
				}
				System.out.println();
			}
		}*/
	}

	public DefaultQuery(Query query) {
		super(query);
	}
	
	public void parseParameterValue(String name, String rawValue) {
		Parameter parameter = parametersByName.get(name);
		if (parameter == null) {
			System.out.println("ERROR: Parameter \"" + name + "\" does not exist.");
			return;
		}
		Object value = null;
		if (rawValue.length() > 0) {
			value = parameter.parseValue(rawValue);
		}
		setParameterValue(name, value);
	}
	
	public ResultSet buildResultSet(ReadService readService) {
		return new DefaultResultSet(this, readService);
	}

	public static void main(String args[]) {
		QueryParser parser = new QueryParser();

		//Query q1 = parser.parse(new File("D:\\Freebase\\Data\\Olympic Athletes\\add_olympic_affiliation.mql"));
		//q1.setParameterValue("country", "/en/canada");
		
		Query q1 = parser.parse(new File("D:\\Freebase\\Queries\\create_nndb_person_key.mql"));
		q1.setParameterValue("person", "/en/bill_gates");
		q1.setParameterValue("nndb_key", "101/12345");
		
		System.out.println(q1.toString());
	}
}
