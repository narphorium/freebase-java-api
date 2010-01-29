package com.narphorium.freebase.results;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReconciliationResultSet implements Iterable<ReconciliationResult> {
	
	private List<ReconciliationResult> results = new ArrayList<ReconciliationResult>();

	public ReconciliationResultSet() {}
	
	public ReconciliationResultSet(List<ReconciliationResult> results) {
		this.results.addAll(results);
	}

	public Iterator<ReconciliationResult> iterator() {
		return results.iterator();
	}
}
