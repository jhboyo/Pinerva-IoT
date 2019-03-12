package com.iot.mongodb.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class Collection {
	
	private final static MongoClient mongoClient = new MongoClient("localhost", 27017);

	public Collection() {

	}
	
	public static MongoDatabase connect() {
		return mongoClient.getDatabase("heoacademy");
	}
	
	public void createCollection(String collectionName) {
		connect().createCollection(collectionName);
	}
	
	public void showCollections() {
		
		MongoIterable<String> names =  connect().listCollectionNames();
		for (String name : names) {
			System.out.println(name);
		}
	}
	
	public static void main(String[] args) {
		//new Collection().createCollection("sensor_comp1");
		new Collection().showCollections();
		
	}
}
