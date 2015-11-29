package io.github.test;

public class Review {
	
	private String id;
	private String review;
	
	public Review(String id, String review) {
		this.id = id;
		this.review = review;
	}
	
	public String getID() {
		return id;
	}
	
	public String getReview() {
		return review;
	}
	
	/**
	 * Returns Review in json format
	 */
	public String toString() {
		return "{id: " + id + ", review: " + review + ", category: " + "\"" + "}";
	}
}
