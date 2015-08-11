package com.api.util;

import gs.dataApi.constants.ApplicationConstants;
import gs.dataApi.constants.JsonKeyConstants;
import gs.dataApi.enums.ErrorEnum;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.vertx.java.core.MultiMap;
import org.vertx.java.core.eventbus.ReplyException;
import org.vertx.java.core.eventbus.ReplyFailure;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;



public class ProcessUtil {

	public static Map getMapFromMultiMap(MultiMap map){
		Map flatMap = new HashMap();
		for(String key: map.names()){
			flatMap.put(key, map.get(key));
		}
		return flatMap;
	}
	
	public static JsonObject getJsonObjectFromMultiMap(MultiMap map){
		JsonObject json = new JsonObject();

		for(String key: map.names()){
			json.putString(key, map.get(key));
		}

		return json;
	}


	public static ProcessingReport validateJson(JsonObject inputJson, JsonObject schemaJson)throws  ProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();

		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		JsonNode input = mapper.readTree(inputJson.toString());
		JsonSchema schema = factory.getJsonSchema(mapper.readTree(schemaJson.toString()));

		ProcessingReport report = schema.validate(input);
		return report;
	}

	public static JsonObject validateDataMethodParameters(JsonObject request, JsonObject schemaJson)throws  ProcessingException, IOException{
		JsonObject validationResults = new JsonObject();
		JsonObject paramValidationErrors = new JsonObject();
		JsonArray params = new JsonArray();
		ProcessingReport report = null;

		JsonObject param;
		String paramName;
		String paramType;
		for(int i = 1; i<=Integer.parseInt(request.getString(JsonKeyConstants.PARAM_NUMBER)); ++i){
			param = new JsonObject();
			paramName = request.getString(ApplicationConstants.PARAM_NAME_PREFIX + i);
			paramType = request.getString(ApplicationConstants.PARAM_TYPE_PREFIX + i);

			if(paramName != null){
				param.putString(JsonKeyConstants.NAME, paramName);
			}
			if(paramType!= null){
				param.putString(JsonKeyConstants.TYPE, paramType);
			}
			report = ProcessUtil.validateJson(param, schemaJson);
			if(report.isSuccess()){
				params.add(param);
			}else{
				paramValidationErrors.putArray(ApplicationConstants.PARAM_PREFIX + i, ProcessUtil.processSchemaValidationResponse(report));
			}
		}
		
		if(paramValidationErrors.size() == 0){
			validationResults.putBoolean("valid", true);
			validationResults.putArray(JsonKeyConstants.PARAMS, params);
		}else{
			validationResults.putBoolean("valid", false);
			validationResults.putObject("errors", paramValidationErrors);
		}
		return paramValidationErrors;
	}

	//generate a JsonArray from the schema validation process report
	public static JsonArray processSchemaValidationResponse(ProcessingReport report){
		ProcessingMessage errorMessage;
		JsonArray errorMessages = new JsonArray();
		Iterator<ProcessingMessage> messageIterator = report.iterator();
		while(messageIterator.hasNext()){
			errorMessage = messageIterator.next();
			errorMessages.add(errorMessage.getMessage());
		}

		return errorMessages;
	}

	//get a json response from exception
	public static JsonObject getErrorResponse(ReplyException exception){
		JsonObject response = new JsonObject();

		switch(exception.failureType()){
		case NO_HANDLERS:
			response = getErrorResponse(ErrorEnum.NO_HANDLERS.name(), "Handler for the request not found");
			break;
		case TIMEOUT:
			response = getErrorResponse(ErrorEnum.TIMEOUT.name(), "Service timed out");
			break;

		default:
			//not Vertx failure type, an application error
			ErrorEnum errorType = ErrorEnum.getEnum(exception.failureCode());
			response = getErrorResponse(errorType.name(), exception.getMessage());
		}

		return response;
	}

	//get a json response from error code and message
	public static JsonObject getErrorResponse(String errorCode, String errorMessage){
		JsonObject response = new JsonObject();
		JsonObject error = new JsonObject();
		error.putString(JsonKeyConstants.ERROR_CODE,  errorCode);
		error.putString(JsonKeyConstants.ERROR_MESSAGE,  errorMessage);
		response.putString("outcome", "failed");
		response.putObject("error", error);
		return response;
	}
}
