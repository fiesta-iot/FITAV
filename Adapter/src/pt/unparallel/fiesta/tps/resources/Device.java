package pt.unparallel.fiesta.tps.resources;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.google.gson.annotations.SerializedName;

public class Device {
	
	@SerializedName("id")
	private String id;
	@SerializedName("testbed")
	private String testbed;
	@SerializedName("location")
	private Location location;
	@JsonProperty("sensing_devices")
	@SerializedName("sensing_devices")
	private List<Device> sensingDevices;
	@SerializedName("type")
	private String type;
	@JsonProperty("qk")
	@SerializedName("qk")
	private String quantityKind;
	@JsonProperty("uom")
	@SerializedName("uom")
	private String unitOfMeasurement;
	
	public Device(){	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTestbed() {
		return testbed;
	}
	
	public void setTestbed(String testbed) {
		this.testbed = testbed;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public List<Device> getSensingDevices() {
		return sensingDevices;
	}
	
	public void setSensingDevices(List<Device> sensingDevices) {
		this.sensingDevices = sensingDevices;
	}
	public String getQuantity_kind() {
		return quantityKind;
	}
	
	public void setQuantity_kind(String quantity_kind) throws Exception {
		this.quantityKind = quantity_kind;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUnit_of_measurement() {
		return unitOfMeasurement;
	}
	
	public void setUnit_of_measurement(String unit_of_measurement) throws Exception {
			this.unitOfMeasurement = unit_of_measurement;
	}
}
