package mmt.core;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Locale;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;

import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;

/**
 * Class describing a service.
 *
 * Services have associated train stops, total cost and total duration of the service.
 */
public class Service implements java.io.Serializable {

	/** Serial number for serialization. */
	private static long serialVersionUID = 201708301012L;

	/** The id that identifies the service. */
	private int _id;

	/** The cost of this service. */
    private double _cost;

	/** The departure train stops that compose this service. */
    private ArrayList<TrainStop> _startTrainStops = new ArrayList<TrainStop>();

	/** The arrival train stops that compose this service. */
    private ArrayList<TrainStop> _endTrainStops = new ArrayList<TrainStop>();

	/**
	 * Creates a service that is associated with an id and cost.
	 *
	 * @param id the new service's unique identifier.
	 * @param cost the new service's cost.
	 */
	Service(int id, double cost) {
		_id = id;
		_cost = cost;
	}

	/** 
	 * @return the service's unique identifier.
	 */
	int getId() {
		return _id;
	}

	/**
	 * @return the service's cost.
	 */
	double getCost() {
		return _cost;
	}

	/**
	 * Adds a start train stop to the service.
	 *
	 * @param TrainStop to add to the service.
	 */
	void addStart(TrainStop trainstop) {
		_startTrainStops.add(trainstop);
	}

	/**
	 * Adds an end train stop to the service.
	 *
	 * @param TrainStop to add to the service.
	 */
	void addEnd(TrainStop trainstop) {
		_endTrainStops.add(trainstop);
	}

	/**
	 * @return the station where this service starts.
	 */
    Station getStartStation() {

		TrainStop startStop = _startTrainStops.get(0);
		Station station = startStop.getStation();

		return station;
	}

	/**
	 * @return the station where this service ends.
	 */
    Station getEndStation() {

		TrainStop endStop = _endTrainStops.get(_endTrainStops.size()-1);
		Station station = endStop.getStation();

		return station;
	}

	/**
	 * @return the total duration of a service.
	 */
    Duration getTotalDuration() {

		Duration totalDuration = Duration.ofSeconds(0);

		for (TrainStop trainstop : this._startTrainStops ) {
			totalDuration = totalDuration.plus( trainstop.getSegment().getDuration() );
		}

		return totalDuration;
	}

	/**
	 * @return String description of a service.
	 */
	public String toString() {

		/* Basic properties */
		int id = getId();
		String cost = String.format(Locale.US, "%.2f", getCost());

		/* Stores all properties */
		String result = "Serviço #" + id + " @ " + cost;

		/* Adds last train stop temporarly */
		_startTrainStops.add( _endTrainStops.get(_endTrainStops.size()-1) );

		/* Adds segment information */
		for (TrainStop start : _startTrainStops ) {

			LocalTime time = start.getTime();
			Station station = start.getStation();
			String name = station.getName();

			result = result + "\n" + time.toString() + " " + name;
		}

		/* Removes last train stop added temporarly */
		_startTrainStops.remove( _startTrainStops.size()-1 );

		return result;
	}
}