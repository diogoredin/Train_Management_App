package mmt.core;

import java.time.Duration;

/**
 * Class describing the itineraries created by the Passengers, representing "trips" between train stations.
 */

public class Itinerary implements java.io.Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301020L;

	/** the itinerary's associated id */
	private final int _id;

	/** the itinerary's associated cost */
	private final double _cost;

	/** String description of the itinerary */
	private final String _description;

	/** Duration of the itinerary */
	private final Duration _duration;

	Itinerary (int id, double cost, String description, Duration duration) {
		_id = id;
		_cost = cost;
		_description = description;
		_duration = duration;
	}

	int getId() {
		return _id;
	}

	double getCost() {
		return _cost;
	}

	Duration getDuration() {
		return _duration;
	}

	public String toString() {
		return _description;
	}
}