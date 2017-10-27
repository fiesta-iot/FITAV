package pt.unparallel.fiesta.tps.services;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.util.HttpHeaderNames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.unparallel.fiesta.tps.payloads.AuthenticatePayload;
import pt.unparallel.fiesta.tps.resources.Device;
import pt.unparallel.fiesta.tps.resources.Observation;
import pt.unparallel.fiesta.tps.resources.Testbed;

public class AnnotatorAsAService {

	Client client;

	private String authKey;
	
	private String serverPath = "_$_FIESTA_URL_$_";
	private String authenticateURL = serverPath.concat("/openam/json/authenticate");
	private String testbedAnnotator = serverPath.concat("/iot-registry/api/utils/annotator/testbed");
	private String deviceAnnotator = serverPath.concat("/iot-registry/api/utils/annotator/device");
	private String observationAnnotator = serverPath.concat("/iot-registry/api/utils/annotator/observation");

	private String USERNAME = "_$_USERNAME_$_";
	private String PASSWORD = "_$_PASSWORD_$_";
	
	private Gson gson;
	
	public AnnotatorAsAService() {
		
		client = new ResteasyClientBuilder().build();		
		gson = new GsonBuilder().create();
	}
	
	public void setAuthenticationKey(String authKey) {
		this.authKey = authKey;
	}
	
	public boolean authenticate() {
		
		Response resp = client.target(authenticateURL).request()
				.header(HttpHeaderNames.CONTENT_TYPE, "application/json")
				.header("X-OpenAM-Username", USERNAME)
				.header("X-OpenAM-Password", PASSWORD)
				.post(Entity.json(""));
		
		if(resp != null) {
			this.authKey = resp.readEntity(AuthenticatePayload.class).getTokenId();
			return true;
		}		
		return false;
	}

	public Response getAnnotatedTestbed(Testbed tb) {
		
		String body = gson.toJson(tb);
		
		Response rsp = client.target(testbedAnnotator).request().header(HttpHeaderNames.CONTENT_TYPE, "application/json")
				.header(HttpHeaderNames.ACCEPT, "application/ld+json").header("iPlanetDirectoryPro", this.authKey)
				.post(Entity.json(body));

		return rsp;
	}
	
	public Response getAnnotatedDevice(Device dv) {
		
		String body = gson.toJson(dv);
		
		Response rsp = client.target(deviceAnnotator).request().header(HttpHeaderNames.CONTENT_TYPE, "application/json")
				.header(HttpHeaderNames.ACCEPT, "application/ld+json").header("iPlanetDirectoryPro", this.authKey)
				.post(Entity.json(body));

		return rsp;
	}
	
	public Response getAnnotatedObservation(Observation ob) {
		
		String body = gson.toJson(ob);
		
		Response rsp = client.target(observationAnnotator).request().header(HttpHeaderNames.CONTENT_TYPE, "application/json")
				.header(HttpHeaderNames.ACCEPT, "application/ld+json").header("iPlanetDirectoryPro", this.authKey)
				.post(Entity.json(body));

		return rsp;
	}

}
