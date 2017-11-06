package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

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

	/** The segments that compose this service. */
    protected ArrayList<Segment> _segments;

	/**
	 * Creates a service that is associated with an end and start station.
	 *
	 * @param id identifies this service
	 * @param start the station where this service starts.
	 * @param end the station where this service ends.
	 */
	public Service(int id, Station start, Station end) {
		_id = id;
        _start = start;
        _end = end;
	}

	/**
	 * @return service with it's id and segments.
	 */
	public final String showService() {

        String serviceTxt = "Service ( " + _id + ") : ";

        for (Segment segment : _segments ) {
			serviceTxt = serviceTxt + segment.showSegment();
		}

		return serviceTxt;
	}

	/**
	 * @return the station where this service starts.
	 */
    public final Station getStartStation() {
		return _start;
	}

	/**
	 * @return the station where this service ends.
	 */
    public final Station getEndStation() {
		return _end;
	}

	/**
	 * @return the total cost of this service (sum of the cost of all segments that compose the service).
	 */
    public final double getTotalCost() {

        double totalCost = 0;

        for (Segment segment : _segments ) {
			totalCost = totalCost + segment.getFractionCost();
		}

		return totalCost;
	}

    /* public final Double getTotalDuration() {

        Double totalDuration = 0;

        for (Segement segment : this._segments ) {
			totalDuration = totalDuration + segment.getTotalDuration();
		}

		return totalDuration;
	} */

	/**
	 * @return the id that identifies this service.
	 */
    public final int requestServiceID() {
		return _id;
	}

}