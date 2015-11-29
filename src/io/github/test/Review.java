package io.github.test;

import java.util.*;

public class Review {
	
	private String id;
	private String review;
	private HashMap<String, Double> tf;
	private HashMap<String, Double> tfidf;
	
	public Review(String id, String review) {
		this.id = id;
		this.review = review;
		tf = new HashMap<String, Double>();
		tfidf = new HashMap<String, Double>();
	}
	
	public String getID() {
		return id;
	}
	
	public String getReview() {
		return review;
	}
	
	public HashMap<String, Double> getTF() {
		return tf;
	}
	
	public HashMap<String, Double> getTFIDF() {
		return tfidf;
	}
	
	/**
	 * Returns Review in json format
	 */
	public String toString() {
		return "{id: " + id + ", review: " + review + ", category: " + "\"" + "}";
	}
}
