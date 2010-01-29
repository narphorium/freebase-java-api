package com.narphorium.freebase.results;

import com.narphorium.freebase.query.Query;
import com.narphorium.freebase.services.ReadService;

public class DefaultResultSet extends AbstractResultSet {
	
	public DefaultResultSet(Query query, ReadService readService) {
		super(query, readService);
	}
	
}
