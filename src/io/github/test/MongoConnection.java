package io.github.test;

import com.mongodb.*;
import java.util.*;
import java.io.*;

import io.github.sqlconnection.BaseConnection;

public class MongoConnection {
	
	/**
	 * Main method containing review analysis algorithm
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException{		
		BaseConnection bc = new BaseConnection();
		bc.connect();
		
		//Cursor for unlabel review json file
		bc.setDBAndCollection("cs336", "unlabel_review");
		DBCursor no_split = bc.showRecords();		
		
		//Reviews arraylist
		ArrayList<Review> reviews = new ArrayList<Review>();
		
		//Iterate through each entry unlabel review
		while(no_split.hasNext()){
			DBObject no_split_dbo = no_split.next();
			Review review = new Review((String) no_split_dbo.get("id"), (String) no_split_dbo.get("review"));
			reviews.add(review); //add review to reviews list
		}
		
		//Forming R, a list of 6 random reviews
		ArrayList<Review> R = new ArrayList<Review>();
		int count = 0;
		while (count < 6) {
			int rand = (int)(Math.random() * 500);
			Review rand_review = reviews.get(rand);
			if (!R.contains(rand_review)) {
				R.add(rand_review);
				count++;
			}
		}
		
		//Picking r* from R
		int rand2 = (int)(Math.random() * 6);
		Review r_star = R.get(rand2);
		
		bc.close();
	}
	
	/**
	 * Method to create the json file containing categorized reviews
	 */
	public static void writeFile(ArrayList<Review> reviews) throws FileNotFoundException, UnsupportedEncodingException {
		Writer writer = null;
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("reviews.json"), "utf-8"));
		    for(Review review : reviews){
				writer.write(review.toString() + "\n");
			}
		}
		catch (IOException ex) {}
		finally {
		   try {
			   writer.close();
		   } 
		   catch (Exception ex) {}
		}
	}
}
