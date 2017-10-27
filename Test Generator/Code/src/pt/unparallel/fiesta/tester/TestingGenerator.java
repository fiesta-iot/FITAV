package pt.unparallel.fiesta.tester;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.unparallel.fiesta.tester.constants.Constants;
import pt.unparallel.fiesta.tester.resources.Observation;
import pt.unparallel.fiesta.tester.resources.Observation2Compare;
import pt.unparallel.fiesta.tester.resources.Testbed;

public class TestingGenerator {

	// logger
	private static final Logger logger = LogManager.getLogger(TestingGenerator.class);

	// variables of control
	private static int resourceMax;
	private static int resourceMin;
	private static int observationPeriod;
	private static int timeInterval;
	private static int testbedNumb;
	private static int totalOfResources;
	private static long totalOFObservation;

	// status variables
	private static long numberOfTestbedsInRange;
	private static long numberOfDevicesInRange;
	private static long numberOfObservationsInRange;

	// path to write the data
	private static String pathToCreateData;

	// data structure generated
	private static LinkedList<Testbed> testingData = new LinkedList<Testbed>();

	private static void clearRandomTestVariables() {
		resourceMax = -1;
		resourceMin = -1;
		observationPeriod = -1;
		timeInterval = -1;
		testbedNumb = -1;
		pathToCreateData = "";
		totalOfResources = -1;
		totalOFObservation = -1;

		testingData.clear();
	}

	public static void main(String[] args) {

		clearRandomTestVariables();
		
		int aux = -1;

		master: switch (args[0]) {

		case "random":
		case "Random":
		case "RANDOM":

			clearRandomTestVariables();

			// initialize variables with input arguments
			if (args.length % 2 != 0) {
				logger.error("Error when defining the parameters, the template is \"-<option> <value>\"");
				break;
			}

			logger.info("Initializing random Unparallel tester!");

			for (int i = 2; i < args.length; i += 2) {

				aux = -1;

				switch (args[i]) {

				case "-rM":
				case "-ResourceMaximum":

					aux = Integer.parseInt(args[i + 1]);

					if (resourceMin > 0)
						if (aux < resourceMin) {
							logger.error("Resource maximum must be higher than minimum");
							break master;
						}

					if (aux <= 0) {
						logger.error("Resource maximum must be higher than 0");
						break master;
					}
					resourceMax = aux;
					break;

				case "-rm":
				case "-ResourceMinimum":

					aux = Integer.parseInt(args[i + 1]);

					if (resourceMax >= 0)
						if (aux > resourceMax) {
							logger.error("Resource minimum must be lower than maximum");
							break master;
						}

					if (aux <= 0) {
						logger.error("Resource minimum must be higher or equals to 0");
						break master;
					}
					resourceMin = aux;
					break;

				case "-oP":
				case "-ObservationsPeriod":
					aux = Integer.parseInt(args[i + 1]);

					if (aux > 0) {
						observationPeriod = aux;
					} else {
						logger.error("Observations period must be higher than 0");
						break master;
					}
					break;

				case "-tI":
				case "-TimeInterval":
					aux = Integer.parseInt(args[i + 1]);

					if (aux > 0) {
						timeInterval = aux;
					} else {
						logger.error("Time interval must be higher than 0");
						break master;
					}
					break;

				case "-pTD":
				case "-PathToData":
					pathToCreateData = args[i + 1];
					break;
				default:
					logger.error("Unrecognized option! -> " + args[i]);
				}
			}

			// generate random values to testbed, resources and observations quantity.
			generateRandoms();

			// create the testbeds IDS
			if (TestbedGeneratorMethods.testbedGenerator(testingData, testbedNumb, pathToCreateData))
				logger.info("Testbeds file created");
			else
				logger.error("Error creating testbeds file");

			// create the devices
			int[] result = DeviceGeneratorMethods.DeviceGenerator(totalOfResources, pathToCreateData);
			if (result[0] != -1 && result[1] != -1) {
				logger.info("Devices file created");
				numberOfDevicesInRange = result[0];
				numberOfTestbedsInRange = result[1];
			} else
				logger.error("Error creating devices file");

			// create the Observations
			LinkedList<Observation> observationsResult = ObservationGeneratorMethods
					.ObservationGenerator(observationPeriod, timeInterval, pathToCreateData);
			if (observationsResult != null) {
				logger.info("Observations file created");
			} else
				logger.error("Error creating observations file");

			writeStatusVariablesToFile(pathToCreateData);

			createObservationsFileToCompare(pathToCreateData, observationsResult);
			createObservationsFileToCompare2(pathToCreateData, observationsResult);			
			break;

		default:
			logger.error("Not a valid option! -> " + args[0]);
			break;
		}
	}

	private static void generateRandoms() {

		totalOfResources = ThreadLocalRandom.current().nextInt(resourceMin, resourceMax + 1);

		if (totalOfResources < Constants.TESTBEDMAXIMUM)
			testbedNumb = ThreadLocalRandom.current().nextInt(1, totalOfResources + 1);
		else
			testbedNumb = ThreadLocalRandom.current().nextInt(1, Constants.TESTBEDMAXIMUM + 1);
	}

	public static LinkedList<Testbed> getDataSet() {
		return TestingGenerator.testingData;
	}

	public static boolean writeStatusVariablesToFile(String pathToWriteFiles) {

		int lineToWriteInfo = 0;
		int indexOfLastSlash;
		
		pathToWriteFiles = pathToWriteFiles.substring(0, pathToWriteFiles.lastIndexOf("/"));
		indexOfLastSlash = pathToWriteFiles.lastIndexOf("/")+5;
		lineToWriteInfo = Integer.parseInt(pathToWriteFiles.substring(indexOfLastSlash));
		
		pathToWriteFiles = pathToWriteFiles.substring(0, pathToWriteFiles.lastIndexOf("/"));

		File filepath = new File(pathToWriteFiles);
		filepath.mkdir();
		File file = new File(filepath, Constants.STATUSFILENAME);

		try {
			file.createNewFile();
			FileOutputStream oFile;
			if(lineToWriteInfo==1) {			
				oFile = new FileOutputStream(file, false);
				oFile.write("Numb of testbeds;Numb of Testbeds in Range;Numb of Devices;Numb of Devices in Range;Numb of Observations;Numb of Observations in Range".getBytes());			
			}else {
				oFile = new FileOutputStream(file, true);
			}
			totalOFObservation = totalOfResources * observationPeriod * 60 / timeInterval;
			numberOfObservationsInRange = totalOFObservation / totalOfResources * numberOfDevicesInRange;

			StringBuilder sb = new StringBuilder();
			sb.append("\n").append(testbedNumb).append(';').append(numberOfTestbedsInRange).append(';')
					.append(totalOfResources).append(';').append(numberOfDevicesInRange).append(';')
					.append(totalOFObservation).append(';').append(numberOfObservationsInRange);
			oFile.write(sb.toString().getBytes());
			oFile.close();
		} catch (IOException e) {
			logger.error("Error writing in the status file" + e.getMessage());
			return false;
		}
		return true;
	}

	public static boolean createObservationsFileToCompare(String pathToWriteFiles,
			LinkedList<Observation> observations) {

		File filepath = new File(pathToWriteFiles);
		filepath.mkdir();
		File file = new File(filepath, Constants.OBSERVATIONS2COMPRATEFILENAME);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		//filter observations to location restriction
		LinkedList<Observation> filteredObs = new LinkedList<Observation>();
		for(Observation ob : observations) {
			if(LocationGeneratorMethods.isLocationInRange(ob.getLocation()))
				filteredObs.add(ob);
		}		
		
		//sort list of observations
		Double[] obsValues = new Double[filteredObs.size()];
		Observation2Compare otc = new Observation2Compare();
		for (int i = 0; i < filteredObs.size(); i++) {
			
			obsValues[i] = filteredObs.get(i).getValueAsDouble();
		}
		Arrays.sort(obsValues);

		for (Double d : obsValues)
			otc.addItem(d);
		try {
			file.createNewFile();
			FileOutputStream oFile = new FileOutputStream(file, false);
			oFile.write(gson.toJson(otc).getBytes());
			oFile.close();
		} catch (IOException e) {
			logger.error("Error writing in the status file" + e.getMessage());
			return false;
		}
		return true;
	}
	
	public static boolean createObservationsFileToCompare2(String pathToWriteFiles,
			LinkedList<Observation> observations) {

		File filepath = new File(pathToWriteFiles);
		filepath.mkdir();
		File file = new File(filepath, Constants.OBSERVATIONS2COMPRATEFILENAME2);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		//filter observations to type and value
		LinkedList<Observation> filteredObs = new LinkedList<Observation>();
		for(Observation ob : observations) {
			if(ob.getQuantity_kind().equals(Constants.TYPETOCOMPARE) && ob.getValueAsDouble()>Constants.VALUETOCOMPARE)
				filteredObs.add(ob);
		}		
		
		//sort list of observations
		Double[] obsValues = new Double[filteredObs.size()];
		Observation2Compare otc = new Observation2Compare();
		for (int i = 0; i < filteredObs.size(); i++) {
			
			obsValues[i] = filteredObs.get(i).getValueAsDouble();
		}
		Arrays.sort(obsValues);

		for (Double d : obsValues)
			otc.addItem(d);
		try {
			file.createNewFile();
			FileOutputStream oFile = new FileOutputStream(file, false);
			oFile.write(gson.toJson(otc).getBytes());
			oFile.close();
		} catch (IOException e) {
			logger.error("Error writing in the status file" + e.getMessage());
			return false;
		}
		return true;
	}

}
