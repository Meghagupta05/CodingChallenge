package com.anddigital.telecom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class CustomerVerticle extends AbstractVerticle {

	private List<JsonObject> customers = new ArrayList<>();

	@Override
	public void start() {

		setUpInitialData();
		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());
		router.get("/allcustomers").handler(this::handleGetAllCustomers);
		router.get("/allcontacts").handler(this::handleGetAllCustContacts);
		router.get("/contacts/:custname").handler(this::hanldeGetContacts);
		router.put("/activate/contact/:contactno").handler(this::handleActivateMobile);

		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}

	private void hanldeGetContacts(RoutingContext routingContext) {
		String custname = routingContext.request().getParam("custname");

		HttpServerResponse response = routingContext.response();
		JsonArray arr = new JsonArray();
		JsonArray arrDiactivatedNo = new JsonArray();
		customers.forEach((v) -> {
			if (null != custname && custname.equals(v.getString("name"))) {
				arr.addAll(v.getJsonArray("contacts"));
				arrDiactivatedNo.addAll(v.getJsonArray("diactivated"));
			}

		});

		JsonObject respData = new JsonObject();
		respData.put("Active Numbers", arr.getList());
		respData.put("Not Activated Numbers", arrDiactivatedNo.getList());
		response.putHeader("content-type", "application/json").end(respData.encodePrettily());

	}

	private void handleGetAllCustContacts(RoutingContext routingContext) {
		JsonArray arr = new JsonArray();

		customers.forEach((v) -> {
			arr.addAll(v.getJsonArray("contacts"));
			if (v.getJsonArray("diactivated").size() > 0)
				arr.addAll(v.getJsonArray("diactivated"));
		});
		routingContext.response().putHeader("content-type", "application/json").end((arr.encodePrettily()));
	}

	private void handleActivateMobile(RoutingContext routingContext) {
		String contactno = routingContext.request().getParam("contactno");
		HttpServerResponse response = routingContext.response();
		if (contactno == null || contactno.length() <= 0) {
			sendError(400, response);
		} else {
			Boolean flag = true;
			for (JsonObject v : customers) {

				if (v.getJsonArray("diactivated").contains(Long.parseLong(contactno))) {
					v.getJsonArray("contacts").add(Long.parseLong(contactno));
					v.getJsonArray("diactivated").remove(Long.parseLong(contactno));
					response.end("Successfully Activated number " + contactno);
					flag = false;
					break;
				} else if (v.getJsonArray("contacts").contains(Long.parseLong(contactno))) {
					response.end("Number " + contactno + " is already Activated ");
					flag = false;
					break;
				}

			}
			if(flag)
				response.end("Number " + contactno + " is not assign to any Customer");

		}
	}

	private void handleGetAllCustomers(RoutingContext routingContext) {
		JsonArray arr = new JsonArray();
		routingContext.response().putHeader("content-type", "application/json").end(customers.toString());
	}

	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}

	private void setUpInitialData() {
		addCustmor(
				new JsonObject().put("id", "01").put("name", "Evan").put("contacts", new JsonArray().add(2672364531l))
						.put("diactivated", new JsonArray().add(4672364531l).add(4672364533l).add(4672364534l)));
		addCustmor(new JsonObject().put("id", "02").put("name", "Jon").put("contacts", new JsonArray().add(6672364531l))
				.put("diactivated", new JsonArray().add(4672364541l).add(4672364551l).add(4672364561l)));
		addCustmor(new JsonObject().put("id", "03").put("name", "Sandy").put("contacts", new JsonArray())
				.put("diactivated", new JsonArray().add(5672364531l)));
	}

	private void addCustmor(JsonObject customer) {
		customers.add(customer);
	}
}
