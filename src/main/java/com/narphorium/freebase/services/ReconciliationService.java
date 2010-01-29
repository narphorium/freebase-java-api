package com.narphorium.freebase.services;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.narphorium.freebase.results.ReconciliationResult;
import com.narphorium.freebase.results.ReconciliationResultSet;

public class ReconciliationService extends AbstractFreebaseService {
	
	public ReconciliationService() {
		super();
	}
	
	public ReconciliationService(URL baseUrl) {
		super(baseUrl);
	}
	
	public ReconciliationResultSet reconcile(Map<String, Object> values) {
		try {
			URL url = new URL(getBaseUrl() + "/reconciliation/query");
			System.out.println("URL: " + url);
			
			String query = buildQuery(values);
			System.out.println("Query: " + query);
			
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("q", query);
			
			String response = postContent(url, parameters);
			System.out.println("Response: " + response);
			
			return new ReconciliationResultSet(parseResults(response));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ReconciliationResultSet();
	}
	
	private String buildQuery(Map<String, Object> values) {
		return generateJSON(values);
	}

	private List<ReconciliationResult> parseResults(String response) {
		List<ReconciliationResult> results = new ArrayList<ReconciliationResult>();
		try {
			List<Map<String, Object>> data = (List<Map<String, Object>>)parseJSON(response);
			for (Map<String, Object> entry : data) {
				String id = entry.get("id").toString();
				List<String> names = (List<String>)entry.get("name");
				List<String> types = (List<String>)entry.get("type");
				Double score = Double.parseDouble(entry.get("score").toString());
				boolean match = Boolean.parseBoolean(entry.get("match").toString());
				results.add(new ReconciliationResult(id, names, types, score, match));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}
}
