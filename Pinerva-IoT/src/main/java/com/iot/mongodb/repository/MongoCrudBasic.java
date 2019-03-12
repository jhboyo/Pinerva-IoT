package com.iot.mongodb.repository;


import static com.mongodb.client.model.Filters.eq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;
import org.bson.types.BSONTimestamp;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import lombok.extern.java.Log;
/**
 * @author jookim
 *
 */

@Log
public class MongoCrudBasic {


	private final static MongoClient mongoClient = new MongoClient("localhost", 27017);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	public MongoCrudBasic() {

	}
	public static MongoDatabase connect() {

		return mongoClient.getDatabase("heoacademy");
	}



	public void find() {

		MongoCollection<Document> collection = connect().getCollection("sensor_comp1");

		//find first document
		Document document = collection.find().first();
		//System.out.println(document.toJson());

		//find all document
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}

		/**** Find query ****/
		/*BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("username", "joonho");

		document = collection.find(searchQuery).first();
		System.out.println(document.toJson());


		Block<Document> printBlock = new Block<Document>() {
		     @Override
		     public void apply(final Document document) {
		         System.out.println(document.toJson());
		     }
		};

		collection.find(eq("username", "heojoon")).forEach(printBlock);*/

	}

	public void insert() {


		Document document = new Document("distance", 5.01)
				.append("timestamp", sdf.format(new Date()));

		connect().getCollection("sensor_comp1").insertOne(document);
	}

	public void insert(String distance) {


		Document document = new Document("distance", distance)
				.append("timestamp", sdf.format(new Date()));

		connect().getCollection("sensor_comp1").insertOne(document);
	}


	public Long count() {
		return connect().getCollection("users").count();

	}

	public static void main(String[] args) {

		new MongoCrudBasic().find();
		//new CrudBasic().insert();
	}

}
