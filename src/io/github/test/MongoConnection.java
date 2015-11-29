package io.github.test;

import com.mongodb.*;
import java.util.*;
import io.github.sqlconnection.BaseConnection;

public class MongoConnection {
	
	/**
	 * Main method containing TF-IDF algorithm
	 */
	public static void main(String[] args) {		
		BaseConnection bc = new BaseConnection();
		bc.connect();
		
		bc.setDBAndCollection("cs336", "unlabel_review");
		DBCursor no_split = bc.showRecords();		
		ArrayList<Review> reviews = new ArrayList<Review>(); //ArrayList masterlist of reviews
		HashMap<String, Double> idfs = new HashMap<String, Double>(); //IDF's HashMap for all terms in all reviews
		
		//Iterate through each entry in unlabel review collection and update IDF value
		while(no_split.hasNext()){
			DBObject no_split_dbo = no_split.next();
			Review review = new Review((String) no_split_dbo.get("id"), (String) no_split_dbo.get("review"));
			reviews.add(review);
		}
		updateIDFs(reviews, idfs);
		printHashMap(idfs);
		
		
		//Forming R, a list of 6 random reviews and storing their TF values
		ArrayList<Review> R = new ArrayList<Review>();
		int count = 0;
		while (count < 6) {
			int rand = randInt(0, reviews.size() - 1);
			Review rand_review = reviews.get(rand);
			if (!R.contains(rand_review)) {
				String[] review_text = splitReview(rand_review);
				for (int i = 0; i < review_text.length; i++) {
					if (!rand_review.getTF().containsKey(review_text[i])) {
						rand_review.getTF().put(review_text[i], 1.0);
					}
					else {
						rand_review.getTF().put(review_text[i], 1.0 + rand_review.getTF().get(review_text[i]));
					}
				}
				
				//Updating TF's with log10-weighted values
				for (String word : rand_review.getTF().keySet()) {
					rand_review.getTF().put(word, 1.0 + Math.log10(rand_review.getTF().get(word)));
				}
				
				R.add(rand_review);
				//printHashMap(rand_review.getTF());
				count++;
			}
		}
		
		//Picking r* from R
		//int rand2 = randInt(0, R.size() - 1);
		//Review r_star = R.get(rand2);
		
		bc.close();
	}
	
	/**
	 * Returns a random integer between min and max (inclusive)
	 */
	public static int randInt(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	/**
	 * Returns a review's transcript in array form (parsed by words)
	 */
	public static String[] splitReview(Review r) {
		String review_text = r.getReview().toLowerCase();
		String[] split_review = review_text.split("\\W+");
		return split_review;
	}
	
	/**
	 * Updates the IDF values for each term in all reviews in masterlist
	 */
	public static void updateIDFs(ArrayList<Review> masterlist, HashMap<String, Double> idfs) {
		for (Review m_review : masterlist) {
			String[] review_words = splitReview(m_review);
			for (int j = 0; j < review_words.length; j++) {
				if (!idfs.containsKey(review_words[j])) {
					idfs.put(review_words[j], 1.0);
				}
				else {
					idfs.put(review_words[j], 1.0 + idfs.get(review_words[j]));
				}
			}
		}
		
		//Updates it with log10-weighted values
		for (String word : idfs.keySet()) {
			idfs.put(word, Math.log10(masterlist.size()/idfs.get(word)));
		}
	}
	
	/**
	 * Prints HashMap key-value pairs in the form: (key, value)
	 */
	public static void printHashMap(HashMap<String, Double> map) {
		for (String word : map.keySet()) {
			System.out.println("(" + word + ", " + map.get(word) + ")");
		}
	}
}
