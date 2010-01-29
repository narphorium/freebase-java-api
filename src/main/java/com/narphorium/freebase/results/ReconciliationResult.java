package com.narphorium.freebase.results;

import java.util.ArrayList;
import java.util.List;

public class ReconciliationResult  {

	private String id;
	private List<String> names = new ArrayList<String>();
	private List<String> aliases = new ArrayList<String>();
	private List<String> types = new ArrayList<String>();
	private double score;
	private boolean match;
	
	public ReconciliationResult(String id, List<String> names, List<String> types, Double score, boolean match) {
		this.id = id;
		this.names = names;
		this.types = types;
		this.score = score;
		this.match = match;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public boolean isMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public List<String> getNames() {
		return names;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public List<String> getTypes() {
		return types;
	}

}
