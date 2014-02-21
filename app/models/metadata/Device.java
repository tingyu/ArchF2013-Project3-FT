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
package models.metadata;

import java.util.*;

import scala.reflect.internal.Trees.This;
import util.APICall;

//import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.*;

public class Device {

	private String id;
	private String deviceUri;
	// private DeviceType deviceType;
	private double longitude;
	private double latitude;
	private double altitude;

	// newly added
	private String deviceTypeName;
	private List<String> sensorTypeNames = new ArrayList<String>();
	private List<String> sensorNames = new ArrayList<String>();

	// http://einstein.sv.cmu.edu/get_devices/json
	private static final String API_CALL = util.Constants.NEW_API_URL
			+ util.Constants.NEW_GET_DEVICES + util.Constants.FORMAT;
	private static final String ADD_DEVICE_CALL = util.Constants.API_URL
			+ util.Constants.ADD_DEVICE;
	private static final String DELETE_DEVICE_CALL = util.Constants.API_URL
			+ util.Constants.DELETE_DEVICE;
	private static List<Device> deviceFoundList = new ArrayList<Device>();

	// Constructors

	public Device() {
	}

	public String getId() {
		return id;
	}

	public String getDeviceUri() {
		return deviceUri;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDeviceUri(String uri) {
		this.deviceUri = uri;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}

	public List<String> getSensorTypeNames() {
		return sensorTypeNames;
	}

	public void addSensorTypeName(String sensorTypeName) {
		this.sensorTypeNames.add(sensorTypeName);
	}

	public void deleteSensorTypeName(String sensorTypeName) {
		for (int i = 0; i < this.sensorTypeNames.size(); i++) {
			if (sensorTypeName.equals(this.sensorTypeNames.get(i))) {
				this.sensorTypeNames.remove(i);
			}
		}
	}

	public List<String> getSensorNames() {
		return sensorNames;
	}

	public void addSensorNames(String sensorName) {
		this.sensorNames.add(sensorName);
	}

	public void deleteSensorName(String sensorName) {
		for (int i = 0; i < this.sensorNames.size(); i++) {
			if (sensorName.equals(this.sensorNames.get(i))) {
				this.sensorNames.remove(i);
			}
		}
	}

	/**
	 * Method to call the API to add a new device
	 * 
	 * @param jsonData
	 * @return
	 */
	public static JsonNode create(JsonNode jsonData) {
		return APICall.postAPI(ADD_DEVICE_CALL, jsonData);
	}

	/**
	 * Method to call the API to delete a device
	 * 
	 * @param id
	 * @return
	 */
	public static JsonNode delete(String id) {

		return APICall.callAPI(DELETE_DEVICE_CALL + id);
	}

	/**
	 * Method to display all devices
	 * 
	 * @return List<Device> List of all devices
	 */
	public static List<Device> all() {
		List<Device> allDevices = new ArrayList<Device>();

		// API Call: http://einstein.sv.cmu.edu/get_devices/json
		final JsonNode devicesNode = APICall.callAPI(API_CALL);

		// If no value is returned
		if (devicesNode == null || devicesNode.has("error")
				|| !devicesNode.isArray()) {
			return allDevices;
		}

		for (int i = 0; i < devicesNode.size(); i++) {
			JsonNode json = devicesNode.path(i);
			Device newDevice = new Device();

			newDevice.setId(UUID.randomUUID().toString());

			newDevice.setDeviceUri(json.findPath("uri").asText());
			newDevice.setDeviceTypeName(json.findPath("deviceTypeName")
					.asText());

			JsonNode sensorNamesJson = json.findPath("sensorNames");
			for (int j = 0; j < sensorNamesJson.size(); j++) {
				newDevice.addSensorNames(sensorNamesJson.get(j).asText());
			}

			JsonNode sensorTypeNamesJson = json.findPath("sensorTypeNames");
			for (int j = 0; j < sensorTypeNamesJson.size(); j++) {
				newDevice
						.addSensorTypeName(sensorTypeNamesJson.get(j).asText());
			}

			allDevices.add(newDevice);
		}

		// update deviceFoundList
		updateDeviceFoundList(allDevices);

		return allDevices;
	}

	public static void updateDeviceFoundList(List<Device> newList) {
		deviceFoundList.clear();
		for (Device element : newList) {
			deviceFoundList.add(element);
		}
	}

	/**
	 * Method to find a device by its id
	 * 
	 * @param id
	 * @return
	 */
	public static Device find(String id) {
		// if find() is called the first time
		if (deviceFoundList.size() == 0)
			deviceFoundList = Device.all();
		for (Device d : deviceFoundList) {
			if (d.getId().equals(id)) {
				return d;
			}
		}
		// if not found, return device_id as the uri
		Device d = new Device();
		// d.setUri(id);
		return d;
	}

	/**
	 * Method to display all devices' name with id
	 * 
	 * @return a list of all devices' name
	 */
	public static Map<String, String> allDeviceIdWithUri() {
		List<Device> allList = all();
		Map<String, String> resultMap = new HashMap<String, String>();
		for (Device element : allList) {
			String elementUri = element.getDeviceUri();
			String elementId = element.getId();
			if (elementId != null && elementUri != null && elementId != ""
					&& elementUri != "")
				resultMap.put(elementId, elementUri);
		}
		return resultMap;
	}
}
