package com.anddigital.telecom.server;

import com.anddigital.telecom.service.CustomerVerticle;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class VertxServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(1));
		vertx.deployVerticle(new CustomerVerticle() );
	}

}
