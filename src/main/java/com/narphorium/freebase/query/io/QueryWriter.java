package com.narphorium.freebase.query.io;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.narphorium.freebase.query.Query;

public class QueryWriter {

	private String indent;
	private String newline;
	
	private boolean alwaysShowOptional = false;
	private boolean alwaysShowLimit = false;
	private boolean shortenPropertyKeys = true;
	
	public QueryWriter(String indent, boolean multiline) {
		this.indent = indent;
		this.newline = multiline ? "\n" : "";
	}
	
	public String write(Query query) {
		return writeNode(query.getData(), "", false);
	}
	
	private String writeNode(Object root, String offset, boolean withholdStartOffset) {
		if (root instanceof List) {
			String result = (withholdStartOffset ? "" : offset) + "[" + newline;
			List<Object> list = (List<Object>)root;
			for (Iterator<Object> i = list.iterator(); i.hasNext();) {
				result += writeNode(i.next(), offset + indent, false);
				if (i.hasNext()) {
					result += ",";
				}
				result += newline;
			}
			result += offset + "]";
			return result;
		} else if (root instanceof Map) {
			String result = (withholdStartOffset ? "" : offset) + "{" + newline;
			Map<String, Object> map = (Map<String, Object>)root;
			for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
				String key = i.next();
				result += offset + indent + "\"" + key + "\" : " + writeNode(map.get(key), offset + indent, true);
				if (i.hasNext()) {
					result += ",";
				}
				result += newline;
			}
			result += offset + "}";
			return result;
		} else if (root == null) {
			return "null";
		} else if (root instanceof String) {
			String value = root.toString();
			return "\"" + value + "\"";
		} else if (root instanceof Integer || 
				   root instanceof Long || 
				   root instanceof Float || 
				   root instanceof Double || 
				   root instanceof Boolean)
		{
			return root.toString();
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		QueryParser parser = new QueryParser();
		Query query = parser.parse(new File("D:\\Freebase\\Queries\\mql\\unformatted.mql"));
		
		QueryWriter writer = new QueryWriter("  ", true);
		System.out.println(writer.write(query));

	}

}
