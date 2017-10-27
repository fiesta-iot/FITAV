package pt.unparallel.fiesta.tps.rest;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pt.unparallel.fiesta.tps.resources.Observation;
import pt.unparallel.fiesta.tps.resources.Testbed;
import pt.unparallel.fiesta.tps.services.AnnotatorAsAService;
import pt.unparallel.fiesta.tps.services.AutomatedDevicesFromFile;
import pt.unparallel.fiesta.tps.services.AutomatedObservationsFromFile;
import pt.unparallel.fiesta.tps.services.AutomatedTestbedIDsFromFile;
import pt.unparallel.fiesta.tps.services.BackdoorRequests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
@Path("/")
public class TpiApiTestbedProviderServices {
	
	/**
	 * The logger's initialization
	 */
	final static Logger logger = LoggerFactory.getLogger(TpiApiTestbedProviderServices.class);

	//info to present in welcome page
	private static final ArrayList<String> services = new ArrayList<String>(Arrays.asList(
			"/AutomatedInsert/Testbeds",
			"/AutomatedInsert/Devices",
			"/AutomatedInsert/Observations"
			));
	public static final HashMap<String, HashMap<String,String>> methods = new HashMap<String, HashMap<String,String>>();
	static{
		
		HashMap<String,String> auxHM = new HashMap<String,String>();
		
		//Automated insert Testbeds in fiesta 	
		auxHM.put("type", "POST");
		auxHM.put("input", "\n\t\t\t <String> /path_to_file/Testbeds.txt");
		auxHM.put("state", "implemented");
		methods.put("/AutomatedInsert/Testbeds", (HashMap<String, String>) auxHM.clone());
		auxHM.clear();
		
		//Automated insert Devices in fiesta 	
		auxHM.put("type", "POST");
		auxHM.put("input", "\n\t\t\t <String> /path_to_file/Devices.txt");
		auxHM.put("state", "implemented");
		methods.put("/AutomatedInsert/Devices", (HashMap<String, String>) auxHM.clone());
		auxHM.clear();
		
		//Automated insert Observations in fiesta 	
		auxHM.put("type", "POST");
		auxHM.put("input", "\n\t\t\t <String> /path_to_file/Observations.txt");
		auxHM.put("state", "implemented");
		methods.put("/AutomatedInsert/Observations", (HashMap<String, String>) auxHM.clone());
		auxHM.clear();
		
	}
	
	/**
	 * @return a message that should be shown a welcome message
	 *
	 **/
	@GET
	@Path("")
	@Produces("text/plain")
	public String welcomeMessage() {

		String welcomeText = "Welcome to Fiesta Adaptor Service Interface \nCurrent Time\n"
				+ String.format("%tT", System.currentTimeMillis())
				+ "\n=============================================================\n\n"
				+ "\n\t The Adaptor services are:\n\n\n\t\t\t\t\t\tImplemented:\n\n";
		
		String inProgress ="\n\n\n\t\t\t\t\t\tUnder Implementation\n\n";
		String endNote = "";

		HashMap<String, String> aux = new HashMap<String, String>();
		
		for(String service : services) {

			if(methods.containsKey(service)) {
				
				aux = methods.get(service);

				if(aux.get("state").equals("implemented")) {
					welcomeText = welcomeText.concat(""
							+ "\n\n\t"+aux.get("type")+"\t"+service+" "
							+ "\n\t\t   input: "+aux.get("input"));
				}else {
					inProgress = inProgress.concat(""
							+ "\n\n\t"+aux.get("type")+"\t"+service+" "
							+ "\n\t\t   input: "+aux.get("input"));
				}
			}			
		}
		
		return welcomeText.concat(inProgress).concat(endNote);	
	}
	
	@POST
	@Path("/AutomatedInsert/Testbeds")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response automatedRegistTestbeds(String data) {

		if (data != null) {

			BackdoorRequests bdr = new BackdoorRequests();
			bdr.authenticate();

			AutomatedTestbedIDsFromFile atiff = new AutomatedTestbedIDsFromFile(data);
			
			LinkedList<String> responses = new LinkedList<String>();
			Response rsp;
			
			for(String s : atiff.getTestbedIds()) {
				rsp = bdr.registNewTestbed(s);
				
				if(rsp.getStatus()>300)
					return rsp;
				else
					responses.add(rsp.readEntity(String.class));
			}
			return Response.ok(responses).build();
		}

		String result = "error in the input format!";

		return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(result).build();
	}
	
	@POST
	@Path("/AutomatedInsert/Devices")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response automatedRegistDevices(String data) {

		if (data != null) {
			LinkedList<String> responses = new LinkedList<String>();
			Response rsp, rsp2;

			BackdoorRequests bdr = new BackdoorRequests();
			bdr.authenticate();			
			
			//Create list of devices from given file 
			AutomatedDevicesFromFile adff = new AutomatedDevicesFromFile(data);
			
			//Annotate testbeds
			AnnotatorAsAService aaas = new AnnotatorAsAService();
			aaas.setAuthenticationKey(bdr.getAuthenticationKey());
			
			for(Testbed tb : adff.getTesbedToRegis()) {
				rsp = aaas.getAnnotatedTestbed(tb);
				
				if(rsp.getStatus()>300)
					return rsp;
				else {
					
					//submit to fiesta
					rsp2 = bdr.registNewDevice(rsp.readEntity(String.class));
					
					if(rsp2.getStatus() > 300)
						return rsp2;
					else
						responses.add(rsp2.readEntity(String.class));
				}
					
			}
			return Response.ok(responses).build();
		}

		String result = "error in the input format!";

		return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(result).build();
	}
	
	
	@POST
	@Path("/AutomatedInsert/Observations")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response automatedRegistObservations(String data) {

		if (data != null) {
			LinkedList<String> responses = new LinkedList<String>();
			Response rsp, rsp2;

			BackdoorRequests bdr = new BackdoorRequests();
			bdr.authenticate();			
			
			//Create list of devices from given file 
			AutomatedObservationsFromFile aoff = new AutomatedObservationsFromFile(data);
			
			//Annotate observation
			AnnotatorAsAService aaas = new AnnotatorAsAService();
			aaas.setAuthenticationKey(bdr.getAuthenticationKey());
			
			for(Observation ob : aoff.getObservationsToRegis()) {
				rsp = aaas.getAnnotatedObservation(ob);
				
				if(rsp.getStatus()>300)
					return rsp;
				else {
					
					//submit to fiesta
					rsp2 = bdr.registNewObservation(rsp.readEntity(String.class));
					
					if(rsp2.getStatus() > 300)
						return rsp2;
					else
						responses.add(rsp2.readEntity(String.class));
				}					
			}
			return Response.ok(responses).build();
		}

		String result = "error in the input format!";

		return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(result).build();
	}
}
