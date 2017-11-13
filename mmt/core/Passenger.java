package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mmt.core.categories.*;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.MissingFileAssociationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import mmt.core.exceptions.NonUniquePassengerNameException;
import mmt.core.exceptions.InvalidPassengerNameException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Class which describes a passenger associated with a train company.
 *
 * Passengers have a name and a unique identifier. They can also have associated itineraries,
 * and depending on their cost, be part of different categories. A Passenger's name may be changed,
 * but other attributes cannot be changed manually.
 *
 * @author Grupo 22
 * @version 2.0
 */
public class Passenger implements java.io.Serializable {

	/** The TrainCompany to which this passenger travels on. */
	private TrainCompany _trainCompany;

	/** The passenger's unique identifier. */
	protected int _id;

	/** The passenger's name. */
	protected String _name;

	/** The passenger's category. */
	private Category _category;

	/** The passenger's last 10 Itinerary values (FIXME: define itineraries) */
	private double _lastValues = 0;

	/** The passenger's number of itineraries (FIXME: define itineraries) */
	private int _numberOfItineraries = 0;

	/**
	 * Creates a passenger that isn't associated with any TrainCompany.
	 *
	 * @param name the passenger's name (non-null, must not be an empty String).
	 */
	private Passenger(String name) throws InvalidPassengerNameException {
		if (name != null && !name.isEmpty()){
			_name = name;
		} else {
			throw new InvalidPassengerNameException (name);
		}
	}

	/**
	 * Creates a passenger that is associated with a TrainCompany.
	 *
	 * @param trainCompany the train company the passenger travels on.
	 * @param name the passenger's name (non-null, must not be an empty String).
	 */
	public Passenger(TrainCompany trainCompany, String name) throws InvalidPassengerNameException {
		this (name);
		_trainCompany = trainCompany;
		_id = _trainCompany.addPassenger(this);
		updateCategory(_lastValues);
	}

	/** 
	 * @return the passenger's unique identifier.
	 */
	public final int getId() {
		return _id;
	}

	/**
	 * @return the passenger's name.
	 */
	public final String getName() {
		return _name;
	}

	/**
	 * Updates the passenger's category based on the value of their last 10 itineraries.
	 *
	 * @param value the value of the passenger's last 10 itineraries.
	 */
	public void updateCategory(double value) {
		_category = _trainCompany.updateCategory(value);
	}

	/**
	 * @return the passenger's associated number of itineraries.
	 */
	public int getNumberOfItineraries() {
		return _numberOfItineraries;
	}

	/**
	 * @return String description of a passenger.
	 */
	public String toString() {

		DecimalFormat df = new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale.US));

		int id = getId();
		String name = getName();
		String catName = _category.getName();
		int itineraries = getNumberOfItineraries();
		String values = df.format(getLastValues());
		String time = "00:00";

		return "" + id + "|" + name + "|" + catName + "|" + itineraries + "|" + values + "|" + time;
	}

	/**
	 * @return the last 10 itinerary values.
	 */
	public double getLastValues() {
		return _lastValues;
	}

	/**
	 * Changes a passenger's name.
	 *
	 * @param newname the new name of the passenger (non-null, must not be an empty string).
	 */
	public void setName(String newname) throws InvalidPassengerNameException {
		if (newname != null || !newname.isEmpty()) {
			_name = newname;
		} else {
			throw new InvalidPassengerNameException (newname);
		}
	}

}