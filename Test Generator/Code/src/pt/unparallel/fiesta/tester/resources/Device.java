package pt.unparallel.fiesta.tester.resources;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Device {
	
	@SerializedName("id")
	private String id;
	@SerializedName("testbed")
	private String testbed;
	@SerializedName("location")
	private Location location;
	@SerializedName("sensing_devices")
	private List<Device> sensingDevices;
	@SerializedName("type")
	private String type;
	@SerializedName("qk")
	private String quantityKind;
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
	
	public void setQuantity_kind(String quantity_kind) {
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
	
	public void setUnit_of_measurement(String unit_of_measurement) {
			this.unitOfMeasurement = unit_of_measurement;
	}
}
