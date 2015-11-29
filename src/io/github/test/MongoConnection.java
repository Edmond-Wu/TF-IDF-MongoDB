package io.github.test;

import com.mongodb.*;
import java.util.*;
import io.github.sqlconnection.BaseConnection;

public class MongoConnection {
	
	/**
	 * Main method containing cosine similarity comparison algorithm
	 */
	public static void main(String[] args) {		
		BaseConnection bc = new BaseConnection();
		bc.connect();
		
		bc.setDBAndCollection("cs336", "unlabel_review");
		DBCursor no_split = bc.showRecords();		
		ArrayList<Review> reviews = new ArrayList<Review>(); //ArrayList masterlist of reviews
		HashMap<String, Double> idfs = new HashMap<String, Double>(); //IDF's HashMap for all terms in all reviews
		
		while(no_split.hasNext()){
			DBObject no_split_dbo = no_split.next();
			Review review = new Review((String) no_split_dbo.get("id"), (String) no_split_dbo.get("review"));
			review.updateTF();
			reviews.add(review);
		}
		updateIDFs(reviews, idfs);
		for (Review r : reviews) {
			updateTFIDF(r, idfs);
		}
		
		//Forming R, a list of 6 random reviews and storing their TF information
		ArrayList<Review> R = new ArrayList<Review>();
		int count = 0;
		while (count < 6) {
			int rand = randInt(0, reviews.size() - 1);
			Review rand_review = reviews.get(rand);
			if (!R.contains(rand_review)) {
				R.add(rand_review);
				count++;
			}
		}
		
		//Picking r* from R
		int rand2 = randInt(0, R.size() - 1);
		Review r_star = R.get(rand2);
		
		//Cosine similarity between a query of 2 random words in r* and R
		Review query = makeQuery(r_star);
		query.updateTF();
		updateTFIDF(query, idfs);
		cosineSimilarity(query, R);
		
		//Cosine similarity between r* and the rest of R
		R.remove(r_star);
		cosineSimilarity(r_star, R); //comparing r* to R
		
		bc.close();
	}
	
	/**
	 * Returns a random integer between min and max (inclusive)
	 */
	public static int randInt(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	/**
	 * Updates the IDF values for each term in all reviews in masterlist
	 */
	public static void updateIDFs(ArrayList<Review> masterlist, HashMap<String, Double> idfs) {
		for (Review m_review : masterlist) {
			String[] review_words = m_review.getReview().toLowerCase().split("\\W+");
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
	 * Sets the TFIDF values for a review
	 */
	public static void updateTFIDF(Review r, HashMap<String, Double> idfs) {
		for (String word : r.getTF().keySet()) {
			r.getTFIDF().put(word, r.getTF().get(word) * idfs.get(word));
		}
	}
	
	/**
	 * Returns a query in the form of a review (picks 2 random words from r)
	 */
	public static Review makeQuery(Review r) {
		String[] review_words = r.getReview().toLowerCase().split("\\W+");
		int word_index1 = randInt(0, review_words.length - 1);
		int word_index2 = randInt(0, review_words.length - 1);
		while (word_index1 == word_index2) {
			word_index2 = randInt(0, review_words.length - 1);
		}
		
		//Forms review with arbitrary ID number and a "review" of 2 words in the review
		Review query = new Review("69", review_words[word_index1] + " " + review_words[word_index2]);
		return query;
	}
	
	/**
	 * Compares a random review to a set of reviews and calculates the cosine value
	 */
	public static void cosineSimilarity(Review random, ArrayList<Review> r) {
		double cosine_value = 0.0;
		for (Review review : r) {
			double random_total = 0.0, review_total = 0.0, rev_ran_total = 0.0;
			
			ArrayList<String> union = new ArrayList<String>();
			union.addAll(review.getTFIDF().keySet());
			union.addAll(random.getTFIDF().keySet());
			
			for (String word : union) {
				double tfidf_review = 0.0;
				double tfidf_random = 0.0;
				if (review.getTFIDF().containsKey(word)) {
					tfidf_review = review.getTFIDF().get(word);
				}
				if (random.getTFIDF().containsKey(word)) {
					tfidf_random = random.getTFIDF().get(word);
				}
				
				rev_ran_total += (tfidf_review * tfidf_random);
				random_total += Math.pow(tfidf_random, 2);
				review_total += Math.pow(tfidf_review, 2);
			}
			random_total = Math.sqrt(random_total);
			review_total = Math.sqrt(review_total);
			cosine_value = rev_ran_total / (random_total * review_total);
			System.out.println(random.toString());
			System.out.println(review.toString());
			System.out.println("Cosine value: " + cosine_value);
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
