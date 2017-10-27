package pt.unparallel.fiesta.tps.resources;

import org.codehaus.jackson.annotate.JsonProperty;

import com.google.gson.annotations.SerializedName;

public class Observation {
	
	@JsonProperty("observed_by")
	@SerializedName("observed_by")
	private String observer;
	@SerializedName("location")
	private Location location;
	@JsonProperty("quantity_kind")
	@SerializedName("quantity_kind")
	private String quantity_kind;
	@SerializedName("value")
	private String value;
	@SerializedName("format")
	private String format;
	@JsonProperty("unit")
	@SerializedName("unit")
	private String unit;
	@SerializedName("timestamp")
	private String timestamp;
	
	public Observation() {
		
		this.observer = "";
		this.location = new Location();
		this.quantity_kind = "";
		this.value = "";
		this.format = "";
		this.unit = "";
		this.timestamp = "";
	}
	
	public Observation(String observer, Location location, String quantity_kind, String value, String format, String unit, String timestamp) {
		
		this.observer = observer;
		this.location = location;
		this.quantity_kind = quantity_kind;
		this.value = value;
		this.format = format;
		this.unit = unit;
		this.timestamp = timestamp;
	}
	
	public Observation(String observer, String latitude, String longitude, String quantity_kind, String value, String format, String unit, String timestamp) {
		
		this.observer = observer;
		this.location = new Location(latitude, longitude);
		this.quantity_kind = quantity_kind;
		this.value = value;
		this.format = format;
		this.unit = unit;
		this.timestamp = timestamp;
	}

	public String getObserver() {
		return observer;
	}

	public void setObserver(String observer) {
		this.observer = observer;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getQuantity_kind() {
		return quantity_kind;
	}

	public void setQuantity_kind(String quantity_kind) {
		this.quantity_kind = quantity_kind;
	}

	public String getValue() {
		return value;
	}
	
	public double getValueAsDouble() {
		return Double.parseDouble(value);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
