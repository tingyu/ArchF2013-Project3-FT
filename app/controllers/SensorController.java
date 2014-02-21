/*
 * Copyright (c) 2013 Carnegie Mellon University Silicon Valley. 
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available
 * under the terms of dual licensing(GPL V2 for Research/Education
 * purposes). GNU Public License v2.0 which accompanies this distribution
 * is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 * Please contact http://www.cmu.edu/silicon-valley/ if you have any 
 * questions.
 * 
 * */
package controllers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.metadata.Device;
import models.metadata.Sensor;
import models.metadata.SensorType;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

public class SensorController extends Controller {

	final static Form<Sensor> sensorForm = Form.form(Sensor.class);

	public static Result sensors() {
		return ok(sensors.render(Sensor.all(), sensorForm));
	}

	public static Result newSensor() {
		Form<Sensor> dc = sensorForm.bindFromRequest();

		ObjectNode jsonData = Json.newObject();
		jsonData.put("id", UUID.randomUUID().toString());
		jsonData.put("sensorName", dc.field("sensorName").value());
		jsonData.put("sensorType", dc.field("sensorType").value());

		System.out.println(dc.field("uri").value());
		jsonData.put("uri", dc.field("uri").value());

		jsonData.put("user_defined_fields", new java.sql.Timestamp(
				new java.util.Date().getTime()).toString());

		// create the item by calling the API
		JsonNode response = Sensor.create(jsonData);

		// flash the response message
		Application.flashMsg(response);
		
		return ok(sensors.render(Sensor.all(), sensorForm));
	}

	public static Result deleteSensor() {
		DynamicForm df = DynamicForm.form().bindFromRequest();
		String sensorName = df.field("idHolder").value();

		// return a text message

		// Call the delete() method
		JsonNode response = Sensor.delete(sensorName);
		
		// flash the response message
		Application.flashMsg(response);

		return ok(sensors.render(Sensor.all(), sensorForm));
	}
}
