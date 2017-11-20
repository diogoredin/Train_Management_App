package mmt.core;

import java.util.Locale;

import java.time.Duration;

import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.InvalidPassengerNameException;


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

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301011L;

	/** The passenger's unique identifier. */
	private final int _id;

	/** The passenger's name. */
	private String _name;

	/** The passenger's category. */
	private Category _category;

	/** The passenger's last 10 Itinerary values (FIXME: define itineraries) */
	private double _lastValues = 0;

	/** The passenger's number of itineraries (FIXME: define itineraries) */
	private int _numberOfItineraries = 0;

	/**
	 * Creates a passenger that is associated with a trainCompany, id comes from the trainCompany
	 *
	 * @param id the passenger's unique id.
	 * @param name the passenger's name (non-null, must not be an empty String).
	 */
	Passenger(int id, String name) throws InvalidPassengerNameException {
		setName(name);
		_id = id;
	}

	/** 
	 * @return if a name is valid (non-null and not an empty string), returns true; else false.
	 */
	boolean validName(String name) {
		return name != null && !name.isEmpty();
	}

	/** 
	 * @return the passenger's unique identifier.
	 */
	final int getId() {
		return _id;
	}

	/**
	 * @return the passenger's name.
	 */
	final String getName() {
		return _name;
	}

	/**
	 * @return the passenger's associated number of itineraries.
	 */
	int getNumberOfItineraries() {
		return _numberOfItineraries;
	}

	/**
	 * @return the last 10 itinerary values.
	 */
	double getLastValues() {
		return _lastValues;
	}

	/**
	 * Sets a passenger's name.
	 *
	 * @param newname the new name of the passenger (non-null, must not be an empty string).
	 */
	void setName(String newname) throws InvalidPassengerNameException {
		if (validName(newname)) {
			_name = newname;
		} else {
			throw new InvalidPassengerNameException (newname);
		}
	}

	/**
	 * Sets a passenger's category.
	 *
	 * @param category the passenger's new category.
	 */
	void setCategory(Category category) {
		_category = category;
	}

	/**
	 * @return String description of a passenger.
	 */
	public String toString() {

		// Basic attributes
		int id = getId();
		String name = getName();
		String catName = _category.getName();
		int itineraries = getNumberOfItineraries();
		String values = String.format(Locale.US, "%.2f", getLastValues());

		// Formatting time
		Duration duration = Duration.ZERO; // FIXME: Itineraries

		String formatHours = String.format("%02d", duration.toHours());
		duration = duration.minusHours(duration.toHours());
		String formatMinutes = String.format("%02d", duration.toMinutes());
		String time = formatHours +":"+ formatMinutes;


		return "" + id + "|" + name + "|" + catName + "|" + itineraries + "|" + values + "|" + time;
	}
}