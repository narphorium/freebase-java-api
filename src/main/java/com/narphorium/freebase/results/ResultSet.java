package com.narphorium.freebase.results;

import com.narphorium.freebase.query.Query;
import com.narphorium.freebase.services.ReadService;
import com.narphorium.freebase.services.exceptions.FreebaseServiceException;

public interface ResultSet {
	public int size() throws FreebaseServiceException;
	public boolean isEmpty();
	public boolean hasNext() throws FreebaseServiceException;
	public Result next() throws FreebaseServiceException;
	public Result current();
	public void reset();
	public ReadService getReadService();
	public Query getQuery();
}
