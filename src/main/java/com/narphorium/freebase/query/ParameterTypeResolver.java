package com.narphorium.freebase.query;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.stringtree.json.JSONReader;

import com.narphorium.freebase.query.io.QueryParser;
import com.narphorium.freebase.results.Result;
import com.narphorium.freebase.results.ResultSet;
import com.narphorium.freebase.services.ReadService;
import com.narphorium.freebase.services.exceptions.FreebaseServiceException;

public class ParameterTypeResolver {

	private static Pattern parameterNamePattern = Pattern.compile("([\\d\\w_]+):([\\d\\w_]+)");
	private static Matcher parameterNameMatcher = parameterNamePattern.matcher("");
	
	private static Map<String, String> objectProperties = new HashMap<String, String>();
	static {
		objectProperties.put("id", "/type/id");
		objectProperties.put("guid", "/type/id");
		objectProperties.put("type", "/type/type");
		objectProperties.put("name", "/type/text");
		objectProperties.put("key", "/type/key");
		objectProperties.put("timestamp", "/type/datetime");
		objectProperties.put("permission", "/type/permission");
		objectProperties.put("creator", "/type/user");
		objectProperties.put("attribution", "/type/attribution");
	}
	
	private static Set<String> mqlReservedWords = new HashSet<String>();
	static {
		mqlReservedWords.add("return");
		mqlReservedWords.add("count");
		mqlReservedWords.add("connect");
		mqlReservedWords.add("create");
		mqlReservedWords.add("delete");
		mqlReservedWords.add("limit");
		mqlReservedWords.add("sort");
		mqlReservedWords.add("value");
		mqlReservedWords.add("lang");
		mqlReservedWords.add("namespace");
		mqlReservedWords.add("timestamp");
		mqlReservedWords.add("cursor");
	}
	
	private static Map<String, String> expectedTypeByProperty = new HashMap<String, String>();
	private Query expectedTypeQuery;
	
	private ReadService readService;
	private QueryParser queryParser;
	private JSONReader reader = new JSONReader();
	
	public ParameterTypeResolver(ReadService readService) {
		this.readService = readService;
		this.queryParser = new QueryParser();
		this.expectedTypeQuery = this.queryParser.parse("q1","{\"property_id:id\":null,\"type\":\"/type/property\",\"expected_type:expected_type\":null}");
	}
	
	public void process(Query query) {
		processData(query, query.getData(), "/type/object");
	}

	private void processData(Query query, Object data, String expectedType) {
		
		if (data == null) return;
		if (data instanceof List) {
			for (Object element : (List<Object>)data) {
				processData(query, element, expectedType);
			}
		} else if (data instanceof Map) {
			Map<String, Object> mapData = (Map<String, Object>)data;
			if (mapData.containsKey("type")) {
				Object type = mapData.get("type");
				if (type instanceof String) {
					expectedType = (String)type;
				} else if (type instanceof Map) {
					expectedType = ((Map<String,String>)type).get("id");
				}
			}
			for (String key : mapData.keySet()){
				Object value = mapData.get(key);
				String childExpectedType = "/type/object";

				parameterNameMatcher.reset(key);
				if (parameterNameMatcher.matches()) {
					String name = parameterNameMatcher.group(1);
					String id = parameterNameMatcher.group(2);
					childExpectedType = lookupExpectedType(id, expectedType);
					Parameter parameter = query.getParameter(name);
					if (parameter != null) {
						 parameter.setExpectedType(childExpectedType);
					}
				} else if (!mqlReservedWords.contains(key)) {
					childExpectedType = lookupExpectedType(key, expectedType);
				}
				processData(query, value, childExpectedType);
			}
		}
	}

	private String lookupExpectedType(String id, String parentType) {
		if (objectProperties.keySet().contains(id)) {
			return objectProperties.get(id);
		} else if (id.equals("value")) {
			return parentType;
		}
		String property = id;
		if (!property.matches("/[\\w\\d_]+/[\\w\\d_]+/[\\w\\d_]+")) {
			property = parentType + "/" + id;
		}
		String expectedType = expectedTypeByProperty.get(property);
		if (expectedType == null) {
			expectedTypeQuery.setParameterValue("property_id", property);
			try {
				ResultSet results = readService.read(expectedTypeQuery);
				Result result = results.next();
				if (result != null) {
					expectedType = result.getString("expected_type");
					expectedTypeByProperty.put(property, expectedType);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FreebaseServiceException e) {
				e.printStackTrace();
			}
		}
		return expectedType;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		QueryParser parser = new QueryParser();
		Query q1 = parser.parse(new File("D:\\Freebase\\Data\\Olympic Athletes\\add_athlete_details.mql"));
		
		ReadService readService = new ReadService();
		ParameterTypeResolver typeResolver = new ParameterTypeResolver(readService);
		typeResolver.process(q1);

		for (Parameter parameter : q1.getParameters()) {
			System.out.println(parameter.getName() + " = " + parameter.getExpectedType());
		}
		
		q1.parseParameterValue("country", "/en/canada");
		q1.parseParameterValue("height", "10");
		q1.parseParameterValue("weight", "20.5");
		q1.parseParameterValue("date_of_birth", "1995-04-17");
		
		System.out.println(q1.toString());
	}

}
