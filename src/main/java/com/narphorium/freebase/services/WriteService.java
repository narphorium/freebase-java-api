package com.narphorium.freebase.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.narphorium.freebase.query.Query;
import com.narphorium.freebase.services.exceptions.FreebaseServiceException;

public class WriteService extends AbstractFreebaseService {
	
	public WriteService() {
		super();
	}
	
	public WriteService(URL baseUrl) {
		super(baseUrl);
	}
	
	public boolean authenticate(String username, String password) throws FreebaseServiceException {
		try {
			URL url = new URL(getBaseUrl() + "/account/login");
			Map<String, String> content = new HashMap<String, String>();
			content.put("username", username);
			content.put("password", password);
			String result = postContent(url, content);			
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String write(Query query) throws FreebaseServiceException {
		List<Query> queries = new ArrayList<Query>();
		queries.add(query);
		return write(queries);
	}

	public String write(List<Query> queries) throws FreebaseServiceException {
		try {
			URL url = new URL(getBaseUrl() + "/service/mqlwrite");
			String envelope = buildWriteQueryEnvelope(queries);
			Map<String, String> content = new HashMap<String, String>();
			content.put("queries", envelope);
			return postContent(url, content);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected String buildWriteQueryEnvelope(List<Query> queries) {
		String envelope = "{";
		Iterator<Query> i = queries.iterator();
		while (i.hasNext()) {
			Query query = i.next();
			envelope += "\"" + query.getName() + "\":{";
			envelope += "\"query\":" + query.toJSON();
			envelope += "}";
			if (i.hasNext()) {
				envelope += ",";
			}
		}
		envelope += "}";
		return envelope;
	}
}
