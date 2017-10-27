package pt.unparallel.fiesta.tester.resources;

import java.util.Arrays;
import java.util.LinkedList;

public class UnitOfMeasurement {
	
	private static LinkedList<String> units = new LinkedList<String>(Arrays.asList(
			
			"Altitude",
            "Ampere (A)",
            "Microampere (uA)",
            "Milliampere (mA)",
            "Bar",
            "Centibar",
            "Millibar",
            "BeatPerMinute",
            "Candela",
            "Coulomb",
            "Day",
            "Decibel (db)",
            "Degree",
            "Degree angle",
            "Degree celsius",
            "Degree fahrenheit",
            "Dimensionless",
            "EAQI",
            "Farad",
            "Gauss",
            "Gram (g)",
            "Kilogram (kg)",
            "Microgram (ug)",
            "Milligram (mg)",
            "GramPerCubicMetre",
            "KilogramPerCubicMetre, KilogramPerCubicMeter",
            "MicrogramPerCubicMetre, MicrogramPerCubicMeter",
            "MilligramPerCubicMetre, MilligramPerCubicMeter",
            "GramPerLiter",
            "Hertz",
            "Hour",
            "Inch",
            "Index",
            "Kelvin",
            "Kilo",
            "KiloWattHour",
            "KilobitsPerSecond",
            "Latitude",
            "Liter",
            "Liter, Litre",
            "Millilitre",
            "LitrePer100Kilometres",
            "Longitude",
            "Lumen",
            "Lux",
            "Meter",
            "Centimetre, Centimeter",
            "Kilometre, Kilometer",
            "Millimetre, millimeter",
            "MeterPerSecond",
            "Meter Per Second (m/s)",
            "Kilometer Per Hour",
            "MeterPerSecondSquare",
            "Miles",
            "MilligramPerSquareMetre",
            "MillimeterPerHour",
            "Millisecond",
            "MillivoltPerMeter",
            "MinuteAngle",
            "MinuteTime",
            "MmHg",
            "MmolPerLiter",
            "Ohm",
            "Okta",
            "Pascal",
            "Percent",
            "Pound",
            "PPM",
            "Radian",
            "RadianPerSecond",
            "RevolutionsPerMinute",
            "Scale",
            "SecondAngle",
            "SecondTime",
            "Tesla",
            "Time",
            "Tonne",
            "VehiclesPerMinute",
            "Volt",
            "Microvolt (uV)",
            "Millivolt (mV)",
            "VoltAmpereReactive",
            "Wout",
            "Watt (W)",
            "Microwatt (uW)",
            "Milliwatt (mW)",
            "WattPerMeterSquare",
            "WattPerSquareMeter",
            "Year",
            "Others"
            ));
	
	private static LinkedList<String> unitsToTest = new LinkedList<String>(Arrays.asList(
				"Degree celsius","Decibel (db)","Kilometer Per Hour","Bar","Percent"
			));
	
	public static boolean validUnitOfMeasurement(String unitOfMeasurement) {
		
		return units.contains(unitOfMeasurement);
	}
	
	public static String unitOfMeasurementFromReducedList(int index) {
		return unitsToTest.get(index);
	}
}
