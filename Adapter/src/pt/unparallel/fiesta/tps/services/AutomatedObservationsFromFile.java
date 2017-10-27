package pt.unparallel.fiesta.tps.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.unparallel.fiesta.tps.resources.Observation;

public class AutomatedObservationsFromFile {

	final static Logger logger = LoggerFactory.getLogger(AutomatedObservationsFromFile.class);

	private Gson gson;

	private LinkedList<Observation> observationsToRegis;
	
	public AutomatedObservationsFromFile(String filePath) {

		gson = new GsonBuilder().create();

		setObservationsToRegis(new LinkedList<Observation>());

		try {
			for (String line : new ArrayList<String>(Files.readAllLines(Paths.get(filePath)))) {

				getObservationsToRegis().add(gson.fromJson(line, Observation.class));

			}

		} catch (IOException e) {
			logger.error("[ERROR]: Failed to regist a testbed. " + e.getMessage());

		}
	}

	public LinkedList<Observation> getObservationsToRegis() {
		return observationsToRegis;
	}

	public void setObservationsToRegis(LinkedList<Observation> observationsToRegis) {
		this.observationsToRegis = observationsToRegis;
	}
}
