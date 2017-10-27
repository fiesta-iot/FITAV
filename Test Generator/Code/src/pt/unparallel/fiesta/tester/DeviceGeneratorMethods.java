package pt.unparallel.fiesta.tester;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import pt.unparallel.fiesta.tester.constants.Constants;
import pt.unparallel.fiesta.tester.resources.Device;
import pt.unparallel.fiesta.tester.resources.Location;
import pt.unparallel.fiesta.tester.resources.QuantityKind;
import pt.unparallel.fiesta.tester.resources.SensorType;
import pt.unparallel.fiesta.tester.resources.Testbed;
import pt.unparallel.fiesta.tester.resources.UnitOfMeasurement;

public class DeviceGeneratorMethods {

	private static final Logger logger = LogManager.getLogger(DeviceGeneratorMethods.class);

	private static String fileName;
	private static String deviceIDTemplate;

	private static Gson gson = new Gson();

	private static LinkedList<Testbed> data = TestingGenerator.getDataSet();

	private static int numberOfDevicesInLocation = 0;
	private static int numberOfTestbedsInLocation = 0;
	private static boolean haveDeviceInRange;

	public static int[] DeviceGenerator(int totalOfDevices, String pathToWriteFiles) {

		int randomType;
		int[] result = { -1, -1 };

		fileName = Constants.DEVICESFILENAME;
		deviceIDTemplate = Constants.DEVICEIDTEMPLATE;

		try {
			File filepath = new File(pathToWriteFiles);
			filepath.mkdir();
			File file = new File(filepath, fileName);
			file.createNewFile();
			FileOutputStream oFile = new FileOutputStream(file, false);

			String deviceID;
			Device aux;

			Location devLocation;

			long numberOfDevices = 0;
			int resourceNumbperTestbed = totalOfDevices / data.size();
			int testbedsLeft = totalOfDevices;

			for (Testbed tb : data) {
				tb.setEmptyDevicesList();

				haveDeviceInRange = false;

				if (tb.equals(data.getLast())) {
					resourceNumbperTestbed = testbedsLeft;
				}

				for (int i = 0; i < (resourceNumbperTestbed); i++) {

					deviceID = deviceIDTemplate.replace("&ID", "_".concat(String.valueOf(numberOfDevices++)));
					randomType = ThreadLocalRandom.current().nextInt(5);
					testbedsLeft--;

					aux = new Device();

					// ID
					aux.setId(deviceID);

					// location
					devLocation = LocationGeneratorMethods.LocationGenerator();

					if (LocationGeneratorMethods.isLocationInRange(devLocation)) {
						numberOfDevicesInLocation++;
						haveDeviceInRange = true;
					}
					aux.setLocation(devLocation);

					aux.setTestbed(tb.getId());
					aux.setQuantity_kind(QuantityKind.quantityKindFromReducedList(randomType));

					// unit of measurement
					aux.setUnit_of_measurement(UnitOfMeasurement.unitOfMeasurementFromReducedList(randomType));

					// sensor type
					aux.setType(SensorType.sensorTypeFromReducedList(randomType));

					tb.getDevices().add(aux);

					oFile.write(gson.toJson(aux).concat("\n").getBytes());
				}

				if (haveDeviceInRange)
					numberOfTestbedsInLocation++;
			}
			oFile.close();

		} catch (IOException e) {
			logger.error("Can't find or create the file -> " + e.getMessage());
			return result;
		}

		result[0] = numberOfDevicesInLocation;
		result[1] = numberOfTestbedsInLocation;
		return result;
	}
}