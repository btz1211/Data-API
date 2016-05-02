package com.demo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Server extends AbstractVerticle{

	@Override
	public void start(){
		vertx.createHttpServer().requestHandler(req ->{
			req.response()
			   .putHeader("content-type", "text/html")
			   .end("hello world");
				
		}).listen(config().getJsonObject("server").getInteger("port", 8002));
	}
	
	public static void main(String[] args){
		Vertx vertx = Vertx.vertx();
		try{
			JsonObject configJson = new JsonObject();
			JsonObject serverConfig = new JsonObject();
			serverConfig.put("host", "localhost");
			serverConfig.put("port", 8002);
			configJson.put("server", serverConfig);
			DeploymentOptions options = new DeploymentOptions().setConfig(configJson);
			vertx.deployVerticle(Server.class.getName(), options);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
