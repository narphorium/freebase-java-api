package com.narphorium.freebase.services;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.narphorium.freebase.query.Query;
import com.narphorium.freebase.query.io.QueryParser;
import com.narphorium.freebase.results.ResultSet;
import com.narphorium.freebase.services.exceptions.FreebaseServiceException;
import com.narphorium.freebase.services.exceptions.FreebaseServiceTimeoutException;

public class ReadService extends AbstractFreebaseService {
	
	private QueryParser queryParser = new QueryParser();
	
	public ReadService() {
		super();
	}
	
	public ReadService(URL baseUrl) {
		super(baseUrl);
	}
	
	public Map<String, Object> readRaw(Query query, Object cursor) throws IOException, FreebaseServiceException {
		List<Query> queries = new ArrayList<Query>();
		queries.add(query);
		
		List<Object> cursors = new ArrayList<Object>();
		cursors.add(cursor);
		
		String envelope = buildReadQueryEnvelope(queries, cursors);
		String url = getBaseUrl() + "/service/mqlread?queries=" + URLEncoder.encode(envelope, "UTF-8");
		
		String response = fetchPage(url);
		Map<String, Object> data = (Map<String, Object>)parseJSON(response);
		Map<String, Object> result = (Map<String, Object>)data.get(query.getName());
		parseServiceErrors(query, result);
		return result;
	}
	
	public Map<String, Object> readRaw(Query query) throws IOException, FreebaseServiceException {
		return readRaw(query, true);
	}
	
	public ResultSet read(Query query) throws IOException {
		return read(query, null);
	}
	
	public ResultSet read(Query query, String cursor) throws IOException {
		return query.buildResultSet(this);
	}
	
	protected String buildReadQueryEnvelope(List<Query> queries, List<Object> cursors) {
		String envelope = "{";
		Iterator<Query> i = queries.iterator();
		Iterator<Object> j = cursors.iterator();
		while (i.hasNext() && j.hasNext()) {
			Query query = i.next();
			Object cursor = j.next();
			envelope += "\"" + query.getName() + "\":{";
			envelope += "\"query\":" + query.toJSON();
			envelope += ",\"cursor\":";
			if (cursor instanceof Boolean) {
				envelope += cursor.toString();
			} else {
				envelope += "\"" + cursor + "\"";
			}
			envelope += "}";
			if (i.hasNext()) {
				envelope += ",";
			}
		}
		envelope += "}";
		return envelope;
	}
	
	public void parseServiceErrors(Query query, Map<String, Object> data) throws FreebaseServiceException {
		//Map<String, Object> responseData = (Map<String, Object>)response;
		//Map<String, Object> queryData = responseData; //(Map<String, Object>)responseData.get(query.getName());
		String responseCode = data.get("code").toString();
		if (responseCode.equals("/api/status/error")) {
			List<Map<String, Object>> messages = (List<Map<String, Object>>)data.get("messages");
			Map<String, Object> message = messages.get(0);
			String code = message.get("code").toString();
			String description = message.get("message").toString();
			Map<String, Object> info = (Map<String, Object>)message.get("info");
			String host = null; //info.get("host").toString();
			int port = 0; //Integer.parseInt(info.get("port").toString());
			double timeout = 0; //Double.parseDouble(info.get("timeout").toString());
			if (code.equals(FreebaseServiceTimeoutException.ERROR_CODE)) {
				throw new FreebaseServiceTimeoutException(description, host, port, timeout);
			} else {
				throw new FreebaseServiceException(code, description, host, port, timeout);
			}
		}
	}

}
