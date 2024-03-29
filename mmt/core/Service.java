package mmt.core;

import java.util.Collection;
import java.util.Collections;
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
public class Service implements java.io.Serializable, Visitable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301012L;

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
	 * Returns the service's unique identifier.
	 *
	 * @return the service's unique identifier.
	 */
	int getId() {
		return _id;
	}

	/**
	 * Returns the service's associated cost.
	 *
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
	 * Returns the time when this service starts.
	 *
	 * @return the time when this service starts.
	 */
	LocalTime getStartTime() {

		TrainStop startStop = _startTrainStops.get(0);
		LocalTime time = startStop.getTime();

		return time;
	}

	/**
	 * Returns the time when this service ends.
	 *
	 * @return the time when this service ends.
	 */
	LocalTime getEndTime() {

		TrainStop endStop = _endTrainStops.get(_endTrainStops.size()-1);
		LocalTime time = endStop.getTime();

		return time;
	}

	/**
	 * Returns the Station where this service starts.
	 *
	 * @return the station where this service starts.
	 */
	Station getStartStation() {

		TrainStop startStop = _startTrainStops.get(0);
		Station station = startStop.getStation();

		return station;
	}

	/**
	 * Returns the station where this service ends.
	 *
	 * @return the station where this service ends.
	 */
	Station getEndStation() {

		TrainStop endStop = _endTrainStops.get(_endTrainStops.size()-1);
		Station station = endStop.getStation();

		return station;
	}

	/**
	 * Returns the total duration of this service.
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
	 * Returns a String description of this service.
	 *
	 * @return String description of this service.
	 */
	public String toString() {

		/* Basic properties */
		int id = getId();
		String cost = String.format(Locale.US, "%.2f", getCost());

		/* Stores all properties */
		String serviceDescription = "Serviço #" + id + " @ " + cost;

		/* Creates StringBuffer for efficient appending of new strings */
		StringBuffer buf = new StringBuffer();
		buf.append(serviceDescription);

		/* Adds last train stop temporarly */
		_startTrainStops.add( _endTrainStops.get(_endTrainStops.size()-1) );

		/* Adds segment information */
		_startTrainStops.forEach ((TrainStop start)-> {

			LocalTime time = start.getTime();
			Station station = start.getStation();
			String name = station.getName();

			buf.append("\n" + time.toString() + " " + name);
		});

		/* Removes last train stop added temporarly */
		_startTrainStops.remove( _startTrainStops.size()-1 );

		/* Converts buffer into a String */
		String result = buf.toString();

		return result;
	}

	/**
	 * Returns the Starting TrainStops of this service.
	 *
	 * @return a collection of the trainstops that start in this service.
	 */
	Collection<TrainStop> getStartTrainStops() {
		return Collections.unmodifiableCollection(_startTrainStops);
	}

	/**
	 * Returns the Ending TrainStops of this service.
	 *
	 * @return a collection of the trainstops that end in this service.
	 */
	Collection<TrainStop> getEndTrainStops() {
		return Collections.unmodifiableCollection(_endTrainStops);
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}