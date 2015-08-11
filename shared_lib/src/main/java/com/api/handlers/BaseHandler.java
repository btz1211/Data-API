package com.api.handlers;

import org.vertx.java.core.Vertx;
import org.vertx.java.platform.Container;

public abstract class BaseHandler{
	protected Vertx vertx;
	protected Container container;

	protected BaseHandler(Vertx vertx, Container container){
		this.vertx = vertx;
		this.container = container;
	}
}
