package pt.unparallel.fiesta.tester;

import java.util.concurrent.ThreadLocalRandom;

import pt.unparallel.fiesta.tester.constants.Constants;
import pt.unparallel.fiesta.tester.resources.Location;

public class LocationGeneratorMethods {

	public static Location LocationGenerator() {

		double latitude = ThreadLocalRandom.current().nextDouble(-90.0, 91.0);
		double longitude = ThreadLocalRandom.current().nextDouble(-180.0, 181.0);

		return new Location(latitude, longitude);
	}

	public static boolean isLocationInRange(Location location) {

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		if ((latitude <= Constants.LATITUDEMAXIMUM && latitude >= Constants.LATITUDEMINIMUM
				&& longitude <= Constants.LONGITUDEMAXIMUM && longitude >= Constants.LONGITUDEMINIMUM))
			return true;
		return false;
	}

}
