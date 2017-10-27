package pt.unparallel.fiesta.tester.resources;


import com.google.gson.annotations.SerializedName;

public class Location {
	
	@SerializedName("lat")
	private double latitude;
	@SerializedName("lon")
	private double longitude;
	
	public Location() {
		
		this.latitude = -95;
		this.longitude = -185;
	}
	
	public Location(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Location(String latitude, String longitude){
		this.latitude = Double.parseDouble(latitude);
		this.longitude = Double.parseDouble(longitude);
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public boolean validPosition() {
		
		if(this.latitude<-90 || this.latitude>90 || this.longitude<-180 || this.longitude>180)
			return false;
		
		return true;
	}
}
