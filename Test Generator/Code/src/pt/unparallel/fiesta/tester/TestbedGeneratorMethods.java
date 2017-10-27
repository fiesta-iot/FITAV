package pt.unparallel.fiesta.tester;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.unparallel.fiesta.tester.constants.Constants;
import pt.unparallel.fiesta.tester.resources.Testbed;

public class TestbedGeneratorMethods {

	private static final Logger logger = LogManager.getLogger(TestbedGeneratorMethods.class);

	private static String fileName;
	private static String testbedIDTemplate;
	public static boolean testbedGenerator(LinkedList<Testbed> data, int testbedNumb, String pathToWriteFiles) {

		fileName = Constants.TESTBEDSFILENAME;
		testbedIDTemplate = Constants.TESBEDIDTEMPLATE;

		File filepath = new File(pathToWriteFiles);
		filepath.mkdir();
		File file = new File(filepath, fileName);

		String testbedID;
		Testbed aux;
		try {
			file.createNewFile();
			FileOutputStream oFile = new FileOutputStream(file, false);

			for (int i = 0; i < (testbedNumb); i++) {

				testbedID = testbedIDTemplate.replace("&ID", "_".concat(String.valueOf(i)));

				aux = new Testbed();
				aux.setId(testbedID);

				data.add(aux);
				oFile.write(testbedID.concat("\n").getBytes());
			}
			oFile.close();
		} catch (IOException e) {
			logger.error("Can't find or crete the file -> " + e.getMessage());
			return false;
		}
		return true;
	}
}
