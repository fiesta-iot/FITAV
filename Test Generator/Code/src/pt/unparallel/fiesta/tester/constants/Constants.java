package pt.unparallel.fiesta.tester.constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {
	
	private static final Logger logger = LogManager.getLogger(Constants.class);

	public static final String TESBEDIDTEMPLATE = "http://api.unparallel.pt#UnparallelTester&ID";
	public static final String DEVICEIDTEMPLATE = "http://api.unparallel.pt/resource/device&ID";
	public static final String SENSORIDTEMPLATE = "http://api.unparallel.pt/resource/SENSOR&ID";

	public static final int VARIATIONPERCENTAGE = 50;
	public static final int VALUEMAXIMUM = 50;

	public static final String TESTBEDSFILENAME = "Testbeds.txt";
	public static final String DEVICESFILENAME = "Devices.txt";
	public static final String OBSERVATIONSFILENAME = "Observations.txt";
	public static final String OBSERVATIONS2COMPRATEFILENAME = "Observations2Compare.json";
	public static final String OBSERVATIONS2COMPRATEFILENAME2 = "Observations2Compare2.json";
	public static final String STATUSFILENAME = "TestsData.csv";
	
	public static final String TYPETOCOMPARE = "Temperature";
	public static final int VALUETOCOMPARE = 15;

	public static double LATITUDEMAXIMUM;
	public static double LATITUDEMINIMUM;
	public static double LONGITUDEMAXIMUM;
	public static double LONGITUDEMINIMUM;

	public static int TESTBEDMAXIMUM;

	public static final String VALUETEMPLATE = "&VALUE&^^http://www.w3.org/2001/XMLSchema#double";
	
	static {
		try {
			Properties props = new Properties();
			FileInputStream file;
			String path = "./config.properties";

			// load the file handle
			file = new FileInputStream(path);

			props.load(file);

			file.close();

			LATITUDEMAXIMUM = Double.parseDouble(props.getProperty("LatitudeMaximum"));
			LATITUDEMINIMUM = Double.parseDouble(props.getProperty("LatitudeMinimum"));
			LONGITUDEMAXIMUM = Double.parseDouble(props.getProperty("LongitudeMaximum"));
			LONGITUDEMINIMUM = Double.parseDouble(props.getProperty("LongitudeMinimum"));
			TESTBEDMAXIMUM = Integer.parseInt(props.getProperty("TestbedMaximum"));

		} catch (IOException e) {
			logger.error("Can't read properties file!! -> " + e.getMessage());
		}
	}

}
