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
	
	public void updateTF() {
		String[] review_text = review.toLowerCase().split("\\W+");
		for (int i = 0; i < review_text.length; i++) {
			if (!tf.containsKey(review_text[i])) {
				tf.put(review_text[i], 1.0);
			}
			else {
				tf.put(review_text[i], 1.0 + tf.get(review_text[i]));
			}
		}
		
		//Updating TF's with log10-weighted values
		for (String word : tf.keySet()) {
			tf.put(word, 1.0 + Math.log10(tf.get(word)));
		}
	}
	
	/**
	 * Returns Review in json format
	 */
	public String toString() {
		return "{id: " + id + ", review: " + review + ", category: " + "\"" + "}";
	}
}
