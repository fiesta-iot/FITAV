package pt.unparallel.fiesta.tester;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import pt.unparallel.fiesta.tester.constants.Constants;
import pt.unparallel.fiesta.tester.resources.Device;
import pt.unparallel.fiesta.tester.resources.Observation;
import pt.unparallel.fiesta.tester.resources.Testbed;

public class ObservationGeneratorMethods {

	private static final Logger logger = LogManager.getLogger(ObservationGeneratorMethods.class);

	private static double currentValue;

	private static String fileName;
	private static Gson gson = new Gson();

	private static LinkedList<Testbed> data = TestingGenerator.getDataSet();
	private static LinkedList<Observation> allObservations = new LinkedList<Observation>();

	private static double numberOfObservationsPerResource;
	
	public static LinkedList<Observation> ObservationGenerator(int observationPeriod, int timeInterval,
			String pathToWriteFiles) {

		fileName = Constants.OBSERVATIONSFILENAME;

		try {
			File filepath = new File(pathToWriteFiles);
			filepath.mkdir();
			File file = new File(filepath, fileName);

			file.createNewFile();

			FileOutputStream oFile = new FileOutputStream(file, false);

			Observation obs;
			Instant day = Instant.now().truncatedTo(ChronoUnit.DAYS);
			Instant obsTime;

			numberOfObservationsPerResource = observationPeriod * 60 / timeInterval;
			double timeBetweenEachObservation = observationPeriod / numberOfObservationsPerResource;
			long numberOfHours = (long) (timeBetweenEachObservation % 1);
			long numberOfMinutes = (long) ((timeBetweenEachObservation - numberOfHours) * 60);

			for (Testbed tb : data)
				for (Device d : tb.getDevices()) {
					obsTime = day;
					currentValue = ThreadLocalRandom.current().nextDouble(Constants.VALUEMAXIMUM);
					
					for (int ob = 0; ob < numberOfObservationsPerResource; ob++, obsTime = obsTime
							.plus(numberOfHours, ChronoUnit.HOURS).plus(numberOfMinutes, ChronoUnit.MINUTES)) {

						currentValue = variateValue(currentValue, Constants.VARIATIONPERCENTAGE);

						obs = new Observation(d.getId(), d.getLocation(), d.getQuantity_kind(),
								String.valueOf(currentValue), "double", d.getUnit_of_measurement(), obsTime.toString());
						
						allObservations.add(obs);
						
						oFile.write(gson.toJson(obs).concat("\n").getBytes());
					}
				}

			oFile.close();
		} catch (IOException e) {
			logger.error("Can't find or create the file -> " + e.getMessage());
			return null;
		}
		return allObservations;
	}

	private static double variateValue(double currentValue2, double variatiponPercentage) {

		double variation = (ThreadLocalRandom.current().nextDouble(variatiponPercentage)) / 100 * currentValue2;

		return (currentValue2 + (ThreadLocalRandom.current().nextDouble() > 0.5 ? variation : -variation));
	}
}
