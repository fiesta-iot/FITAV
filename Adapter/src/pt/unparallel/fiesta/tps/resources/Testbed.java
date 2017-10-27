package pt.unparallel.fiesta.tps.resources;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Testbed {

	@SerializedName("id")
	private String id;
	@SerializedName("devices")
	private List<Device> devices;
	
	public Testbed() {}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public List<Device> getDevices() {
		return devices;
	}
	
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

}
