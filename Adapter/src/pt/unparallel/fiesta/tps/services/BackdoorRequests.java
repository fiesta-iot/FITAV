package pt.unparallel.fiesta.tps.services;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.util.HttpHeaderNames;

import pt.unparallel.fiesta.tps.payloads.AuthenticatePayload;

public class BackdoorRequests {
	Client client;

	private String authKey;

	private String serverPath = "_$_FIESTA_URL_$_";
	
	private String authenticateURL = serverPath.concat("/openam/json/authenticate");
	private String testbedRegistator = serverPath.concat("/iot-registry/api/testbeds");
	private String deviceRegistator = serverPath.concat("/iot-registry/api/resources");
	private String observationRegistator = serverPath.concat("/iot-registry/api/observations");

	private String USERNAME = "_$_USERNAME_$_";
	private String PASSWORD = "_$_PASSWORD_$_";

	public BackdoorRequests() {

		client = new ResteasyClientBuilder().build();
	}

	public void setAuthenticationKey(String authKey) {
		this.authKey = authKey;
	}

	public String getAuthenticationKey() {
		return this.authKey;
	}

	public boolean authenticate() {

		Response resp = client.target(authenticateURL).request()
				.header(HttpHeaderNames.CONTENT_TYPE, "application/json").header("X-OpenAM-Username", USERNAME)
				.header("X-OpenAM-Password", PASSWORD).post(Entity.json(""));

		if (resp != null) {
			this.authKey = resp.readEntity(AuthenticatePayload.class).getTokenId();
			return true;
		}
		return false;
	}

	public Response registNewTestbed(String tb) {

		Response rsp = client.target(testbedRegistator).request().header(HttpHeaderNames.CONTENT_TYPE, "text/plain")
				.header("iPlanetDirectoryPro", this.authKey).post(Entity.text(tb));

		return rsp;
	}

	public Response registNewDevice(String dv) {

		Response rsp = client.target(deviceRegistator).request()
				.header(HttpHeaderNames.CONTENT_TYPE, "application/ld+json").header("iPlanetDirectoryPro", this.authKey)
				.post(Entity.entity(dv, "application/ld+json"));

		return rsp;
	}

	public Response registNewObservation(String ob) {

		Response rsp = client.target(observationRegistator).request()
				.header(HttpHeaderNames.CONTENT_TYPE, "application/ld+json").header("iPlanetDirectoryPro", this.authKey)
				.post(Entity.entity(ob, "application/ld+json"));

		return rsp;
	}
}
