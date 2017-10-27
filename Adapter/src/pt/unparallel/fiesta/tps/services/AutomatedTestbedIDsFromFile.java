package pt.unparallel.fiesta.tps.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutomatedTestbedIDsFromFile {

	final static Logger logger = LoggerFactory.getLogger(AutomatedTestbedIDsFromFile.class);

	private LinkedList<String> testbedIds;

	public AutomatedTestbedIDsFromFile(String filePath) {

		setTestbedIds(new LinkedList<String>());

		try {
			for (String line : new ArrayList<String>(Files.readAllLines(Paths.get(filePath)))) {

				getTestbedIds().add(line.trim());
			}
			
		} catch (IOException e) {
			logger.error("[ERROR]: Failed to regist a testbed. " + e.getMessage());

		}

	}

	public LinkedList<String> getTestbedIds() {
		return testbedIds;
	}

	public void setTestbedIds(LinkedList<String> testbedIds) {
		this.testbedIds = testbedIds;
	}

}
