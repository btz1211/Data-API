package com.api.util;

import java.util.Map;

import gs.dataApi.constants.ApplicationConstants;
import gs.dataApi.constants.JsonKeyConstants;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public final class DBUtil {
	public static DataSource getDataSource(String driver, String url, String username, String password){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setInitialSize(5);
		return ds;
	}
	
	//get data source type from Mongo
	public static JsonObject getDataSourceType(DB db, String typeId){
		JsonObject dataSourceType = null;
		DBObject where = new BasicDBObject();
		where.put(JsonKeyConstants.ID, typeId);
		DBObject dataSourceTypeMongo = db.getCollection(ApplicationConstants.DATA_SOURCE_TYPES).findOne(where);
		
		if(dataSourceTypeMongo != null){
			dataSourceType = new JsonObject(dataSourceTypeMongo.toString());
		}
		
		return dataSourceType;
	}
	
	//get data source from Mongo
	public static JsonObject getDataSource(DB db, String dataSourceId){
		JsonObject dataSource = null;
		DBObject where = new BasicDBObject();
		where.put(JsonKeyConstants.ID, dataSourceId);
		DBObject dataSourceMongo = db.getCollection(ApplicationConstants.DATA_SOURCES).findOne(where);
		
		if(dataSourceMongo != null){
			dataSource = new JsonObject(dataSourceMongo.toString());
		}
		
		return dataSource;
	}
	
	//get data method from Mongo
	public static JsonObject getDataMethod(DB db, String dataMethodId){
		JsonObject dataMethod = null;
		DBObject where = new BasicDBObject();
		where.put(JsonKeyConstants.ID, dataMethodId);
		DBObject dataMethodMongo = db.getCollection(ApplicationConstants.DATA_METHODS).findOne(where);
		
		if(dataMethodMongo != null){
			dataMethod = new JsonObject(dataMethodMongo.toString());
		}
		
		return dataMethod;
	}
	
	//generic method for querying Mongo with a map of params
	public static JsonArray query(DB db, Map params, String collection){
		JsonArray results = new JsonArray();
		
		//no param, return empty
		if(params.size() == 0){
			return results;
		}
		
		DBObject where = new BasicDBObject();
		where.putAll(params);
		DBCursor resultSet = db.getCollection(collection).find(where);
		while(resultSet.hasNext()){
			results.add(new JsonObject(resultSet.next().toString()));
		}
		return results;
	}
}
