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

import pt.unparallel.fiesta.tps.resources.Device;
import pt.unparallel.fiesta.tps.resources.Testbed;

public class AutomatedDevicesFromFile {

	final static Logger logger = LoggerFactory.getLogger(AutomatedDevicesFromFile.class);

	private Gson gson;

	private LinkedList<Testbed> tesbedToRegis;
	private LinkedList<Device> devices;
	private LinkedList<String> testbedIDs;

	private Testbed auxTb;
	private String auxTestbedID;

	public AutomatedDevicesFromFile(String filePath) {

		gson = new GsonBuilder().create();
		
		setTesbedToRegis(new LinkedList<Testbed>());
		devices = new LinkedList<Device>();
		testbedIDs = new LinkedList<String>();

		try {
			for (String line : new ArrayList<String>(Files.readAllLines(Paths.get(filePath)))) {

				devices.add(gson.fromJson(line, Device.class));

			}

			for (Device d : devices) {

				auxTestbedID = d.getTestbed();

				if (testbedIDs.contains(auxTestbedID)) {
					
					auxTb = getTesbedToRegis().get(testbedIDs.indexOf(auxTestbedID));
					d.setTestbed(null);
					auxTb.getDevices().add(d);
						
				} else {
					auxTb = new Testbed();
					auxTb.setId(auxTestbedID);
					auxTb.setDevices(new LinkedList<Device>());
					
					d.setTestbed(null);
					auxTb.getDevices().add(d);
					
					testbedIDs.add(auxTestbedID);
					getTesbedToRegis().add(auxTb);
				}
			}
			
		} catch (IOException e) {
			logger.error("[ERROR]: Failed to regist a testbed. " + e.getMessage());

		}

	}

	public LinkedList<Testbed> getTesbedToRegis() {
		return tesbedToRegis;
	}

	public void setTesbedToRegis(LinkedList<Testbed> tesbedToRegis) {
		this.tesbedToRegis = tesbedToRegis;
	}

}
