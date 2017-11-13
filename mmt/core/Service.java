package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;

import java.util.Iterator;
import java.util.ArrayList;

import mmt.core.exceptions.NoSegmentsException;
import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.MissingFileAssociationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import mmt.core.exceptions.NonUniquePassengerNameException;

public class Service {

	/** The id that identifies this service. */
	protected int _id;

	/** The station where this service starts. */
    protected Station _start;

	/** The station where this service ends. */
    protected Station _end;

	/** The cost of this service. */
    protected double _cost;

	/** The segments that compose this service. */
    protected ArrayList<Segment> _segments = new ArrayList<Segment>();

	/**
	 * Creates a service that is associated with an end and start station.
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
	 * Adds a segment to the service.
	 *
	 * @param segment to add to the service.
	 */
	public final void addSegment(Segment segment) {
		_segments.add(segment);
	}

	/**
	 * @return the station where this service starts.
	 */
    public final Station getStartStation() throws NoSegmentsException {

		if (_segments != null && !_segments.isEmpty()) {
			Segment segment = _segments.get(0);
			TrainStop startStop = segment.getStart();
			Station station = startStop.getStation();

			return station;
		} else {
			throw new NoSegmentsException();
		}

	}

	/**
	 * @return the station where this service ends.
	 */
    public final Station getEndStation() throws NoSegmentsException {

		if (_segments != null && !_segments.isEmpty()) {
			Segment segment = _segments.get(_segments.size()-1);
			TrainStop endStop = segment.getEnd();
			Station station = endStop.getStation();

			return station;
		} else {
			throw new NoSegmentsException();
		}

	}

	/**
	 * @return the total duration of a segment.
	 */
    public final Duration getTotalDuration() {

		Duration totalDuration = Duration.ofSeconds(0);

		for (Segment segment : this._segments ) {
			totalDuration.plus( segment.getDuration() );
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
		String cost = String.format("%.02f", getCost() );

		/* Stores all properties */
		String result = "Serviço #" + id + " @ " + cost;

		/* Adds segment information */
		for (Segment segment : _segments ) {

			TrainStop start = segment.getStart();
			LocalTime ltime = start.getTime();
			Station station = start.getStation();
			String name = station.getName();

			result = result + "\n" + ltime.toString() + " " + name;
		}

		return result;
	}
}