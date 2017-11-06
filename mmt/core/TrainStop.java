package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.time.Instant;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.MissingFileAssociationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import mmt.core.exceptions.NonUniquePassengerNameException;

public class TrainStop {

	/** Stores the station of this TrainStop event. */
    protected Station _station;
    
	/** Stores the instant of this TrainStop event. */
	protected Instant _time;

	/**
	* Creates a TrainStop, which is an event when a train stops at a certain time at a certain station.
	*
	* @param name identifies the station.
	*/
	public TrainStop(Station station, Instant time) {
        _station = station;
        _time = time;
	}

	/** @return the station of this TrainStop. */
	public final Station getStation() {
		return _station;
	}

	/** @return the time of this TrainStop event. */
    public final Instant getDepartureTime() { //FIXME rename to getTime
		return _time;
	}

}