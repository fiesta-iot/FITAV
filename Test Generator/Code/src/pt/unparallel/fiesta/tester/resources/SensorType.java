package pt.unparallel.fiesta.tester.resources;

import java.util.Arrays;
import java.util.LinkedList;

public class SensorType {

	private static LinkedList<String> sensorTypeTest = new LinkedList<String>(Arrays.asList(
			"Thermometer","Sound sensor","Speed sensor","Pressure sensor","Humidity sensor"
	));
	
	public static String sensorTypeFromReducedList(int index) {
		return sensorTypeTest.get(index);
	}
}
