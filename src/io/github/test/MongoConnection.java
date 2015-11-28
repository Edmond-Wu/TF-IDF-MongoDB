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
