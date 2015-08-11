package com.api.constants;

public final class ApplicationConstants {
	//URL Param Constants
	public static String VERSION = "version";
	public static String FETCH_SIZE = "fetch_size"; 
	public static String START_INDEX = "start_index"; 
	public static String DATA_SOURCE_ID = "dataSourceId"; 
	public static String DATA_METHOD_ID = "dataMethodId";
	
	//HTTP Related Constants
	public static String HREF = "href"; 
	public static String RELATION = "rel";
	public static String GET_METHOD = "GET"; 
	public static String METHOD = "method"; 
	public static String LINK = "link"; 
	public static String NEXT = "next";
	public static String CONTENT_TYPE = "content_type";
	public static String PLAIN_TEXT = "text/plain";
	
	//Application Logic Constants
	public static int DEFAULT_FETCH_SIZE = 1000;
	public static String PARAM_PREFIX = "param_";
	public static String PARAM_NAME_PREFIX = "param_name_";
	public static String PARAM_TYPE_PREFIX = "param_type_";

	//Vertx Addresses
	public static String PING_ADDRESS = "ping_address";
	public static String DATA_ADDRESS = "data_address";
	public static String CREATE_DS_ADDRESS = "create_ds_address";
	public static String CREATE_DM_ADDRESS = "create_dm_address";
	
	//Mongo Table Names
	public static String DATA_SOURCE_TYPES = "data_source_types";
	public static String DATA_SOURCES = "data_sources";
	public static String DATA_METHODS = "data_methods";

}
