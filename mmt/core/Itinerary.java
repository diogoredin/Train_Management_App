package mmt.core;

/**
 * Class describing the itineraries created by the Passengers, representing "trips" between train stations.
 */

public class Itinerary implements java.io.Serializable {

	/** the itinerary's associated id */
	private final int _id;

	/** the itinerary's associated cost */
	private final double _cost;

	/** String description of the itinerary */
	private final String _description;

	Itinerary (int id, double cost, String description) {
		_id = id;
		_cost = cost;
		_description = description;
	}

	int getId() {
		return _id;
	}

	double getCost() {
		return _cost;
	}

	public String toString() {
		return _description;
	}
}