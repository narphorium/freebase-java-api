package com.narphorium.freebase.results;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.narphorium.freebase.query.DefaultQuery;
import com.narphorium.freebase.query.Query;
import com.narphorium.freebase.services.ReadService;
import com.narphorium.freebase.services.exceptions.FreebaseServiceException;

public abstract class AbstractResultSet implements ResultSet {

	protected ReadService readService;
	protected Query query;
	protected List<Result> results = new ArrayList<Result>();
	protected int currentResult;
	protected Object cursor;
	protected int numPages;
	protected boolean fetchedFirstPage = false;

	public AbstractResultSet(Query query, ReadService readService) {
		this.readService = readService;
		this.query = new DefaultQuery(query);
		reset();
	}
	
	public Query getQuery() {
		return query;
	}

	public Result current() {
		Result resultData = currentResult >= 0 && currentResult < results.size() ? results.get(currentResult) : null;
		return resultData;
	}
	
	public void reset() {
		currentResult = -1;
		cursor = true;
	}
	
	public int size() throws FreebaseServiceException {
		if (!fetchedFirstPage) {
			fetchNextPage();
		}
		return results.size();
	}
	
	public boolean isEmpty() {
		return results.isEmpty();
	}

	public Result next() throws FreebaseServiceException {
		currentResult++;
		if (currentResult >= results.size() && 
			((cursor instanceof Boolean && (Boolean)cursor == true) || (cursor instanceof String)))
		{
			fetchNextPage();
		}
		return current();
	}
	
	public boolean hasNext() throws FreebaseServiceException {
		if (!fetchedFirstPage) {
			fetchNextPage();
		}
		return currentResult < results.size() - 1;
	}

	protected void fetchNextPage() throws FreebaseServiceException {
		try {
			//String response = readService.readRaw(query, cursor);
			//System.out.println(jsonData);
			//Map<String, Object> data = (Map<String, Object>)parser.read(response);
			//readService.parseServiceErrors(data);
			Map<String, Object> q = (Map<String, Object>)readService.readRaw(query, cursor);
			
			//Map<String, Object> q = (Map<String, Object>)res.get(query.getName());
			if (q.get("result") instanceof List) {
				List<Object> r = (List<Object>)q.get("result");
				for (Object obj : r) {
					results.add(new DefaultResult(query, obj));
				}
			} else {
				Object obj = q.get("result");
				results.add(new DefaultResult(query, obj));
			}
			numPages++;
			Object c = q.get("cursor");
			if (c != null) { 
				cursor = c;
				//System.out.println("CURSOR = " + cursor);
			}
			fetchedFirstPage = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ReadService getReadService() {
		return readService;
	}

}