package models.metadata;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import util.APICall;

public class Sensor {
	private String id;
	private String sensorName;
	private SensorType sensorType;
	private Device device;

	public Sensor() {

	}

	public Sensor(String id, String sensorName, SensorType sensorType,
			Device device) {
		super();
		this.id = id;
		this.sensorName = sensorName;
		this.sensorType = sensorType;
		this.device = device;
	}

	// http://einstein.sv.cmu.edu/get_sensors/json
	private static final String GET_SENSORS_CALL = util.Constants.API_URL
			+ util.Constants.GET_SENSORS + util.Constants.FORMAT;
	private static final String DELETE_SENSOR_CALL = util.Constants.API_URL
			+ util.Constants.DELETE_SENSOR;
	private static final String ADD_SENSOR_CALL = util.Constants.API_URL
			+ util.Constants.ADD_SENSOR;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public SensorType getSensorType() {
		return sensorType;
	}

	public void setSensorType(SensorType sensorType) {
		this.sensorType = sensorType;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	// NEED TO CALL DEVICE AND SENSORTYPE API!
	public static List<Sensor> all() {
		List<Sensor> allSensors = new ArrayList<Sensor>();

		// Call the API to get the json string
		JsonNode sensorsNode = APICall.callAPI(GET_SENSORS_CALL);

		// if no value is returned
		if (sensorsNode == null) {
			return null;
		}

		// if sensor node is not json array
		if (!sensorsNode.isArray()) {
			return null;
		}
		// parse the json string into object
		for (int i = 0; i < sensorsNode.size(); i++) {
			JsonNode json = sensorsNode.path(i);
			Sensor newSensor = new Sensor();

			newSensor.setId(json.findPath("sensor_id").asText());
			newSensor.setSensorName(json.findPath("sensor_name").asText());

			String sensor_type_id = json.findPath("sensor_type").asText();
			newSensor.setSensorType(SensorType.find(sensor_type_id));

			String device_id = json.findPath("device_id").asText();
			newSensor.setDevice(Device.find(device_id));

			allSensors.add(newSensor);
		}

		return allSensors;

	}

	/**
	 * Method to call the API to add a new sensor
	 * 
	 * @param jsonData
	 * @return the response json from the API server
	 */
	public static JsonNode create(JsonNode jsonData) {
		return APICall.postAPI(ADD_SENSOR_CALL, jsonData);
	}

	/**
	 * Method to call the API to delete a sensor with its id
	 * 
	 * @param id
	 * @return the response json from the API server
	 */
	public static JsonNode delete(String id) {
		JsonNode responseResult = APICall.callAPI(DELETE_SENSOR_CALL + id);
		return responseResult;
	}

}
