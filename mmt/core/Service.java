package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Locale;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.MissingFileAssociationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import mmt.core.exceptions.NonUniquePassengerNameException;

public class Service implements java.io.Serializable {

	/** The id that identifies the service. */
	private int _id;

	/** The cost of this service. */
    private double _cost;

	/** The train stops that compose this service. */
    private ArrayList<TrainStop> _startTrainStops = new ArrayList<TrainStop>();

	/** The train stops that compose this service. */
    private ArrayList<TrainStop> _endTrainStops = new ArrayList<TrainStop>();

	/**
	 * Creates a service that is associated with an id and cost.
	 *
	 * @param id identifies uniquely this service.
	 */
	public Service(int id, double cost) {
		_id = id;
		_cost = cost;
	}

	/** 
	 * @return the service unique identifier.
	 */
	public final int getId() {
		return _id;
	}

	/**
	 * @return service cost.
	 */
	public final double getCost() {
		return _cost;
	}

	/**
	 * Adds a start train stop to the service.
	 *
	 * @param TrainStop to add to the service.
	 */
	public final void addStart(TrainStop trainstop) {
		_startTrainStops.add(trainstop);
	}

	/**
	 * Adds an end train stop to the service.
	 *
	 * @param TrainStop to add to the service.
	 */
	public final void addEnd(TrainStop trainstop) {
		_endTrainStops.add(trainstop);
	}

	/**
	 * @return the station where this service starts.
	 */
    public final Station getStartStation() {

		TrainStop startStop = _startTrainStops.get(0);
		Station station = startStop.getStation();

		return station;
	}

	/**
	 * @return the station where this service ends.
	 */
    public final Station getEndStation() {

		TrainStop endStop = _endTrainStops.get(_endTrainStops.size()-1);
		Station station = endStop.getStation();

		return station;
	}

	/**
	 * @return the total duration of a service.
	 */
    public final Duration getTotalDuration() {

		Duration totalDuration = Duration.ofSeconds(0);

		for (TrainStop trainstop : this._startTrainStops ) {
			totalDuration.plus( trainstop.getSegment().getDuration() );
		}

		return totalDuration;
	}

	/**
	 * @return the id that identifies this service.
	 */
    public final int requestServiceID() {
		return _id;
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

			LocalTime ltime = start.getTime();
			Station station = start.getStation();
			String name = station.getName();

			result = result + "\n" + ltime.toString() + " " + name;
		}

		/* Removes last train stop added temporarly */
		_startTrainStops.remove( _startTrainStops.size()-1 );

		return result;
	}
}