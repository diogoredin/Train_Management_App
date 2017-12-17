package mmt.core;

import java.util.Locale;
import java.util.TreeMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import java.time.Duration;

import java.lang.InterruptedException;
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

	/** The passenger's TrainCompany. */
	private TrainCompany _trainCompany;

	/** The passenger's name. */
	private String _name;

	/** The passenger's category. */
	private Category _category;

	/** The passenger's associated itineraries */
	private TreeMap<Integer, Itinerary> _itineraries = new TreeMap<Integer, Itinerary>();

	/** The passenger's last 10 itinerary values */
	private ArrayBlockingQueue<Double> _lastValues = new ArrayBlockingQueue<Double>(10);

	/** The passenger's total spent money */
	private Double _totalSpent = 0.00;

	/**
	 * Creates a passenger that is associated with a trainCompany, id comes from the trainCompany
	 *
	 * @param id the passenger's unique id.
	 * @param name the passenger's name (non-null, must not be an empty String).
	 */
	Passenger(int id, String name, TrainCompany company) throws InvalidPassengerNameException {
		setName(name);
		_id = id;
		_trainCompany = company;
	}

	/** 
	 * Returns whether or not a name is valid for a Passenger.
	 *
	 * @return if a name is valid (non-null and not an empty string), returns true; else returns false.
	 */
	boolean validName(String name) {
		return name != null && !name.isEmpty();
	}

	/** 
	 * Returns the passenger's unique identifier.
	 * 
	 * @return the passenger's unique identifier.
	 */
	final int getId() {
		return _id;
	}

	/**
	 * Returns the passenger's name.
	 *
	 * @return the passenger's name.
	 */
	final String getName() {
		return _name;
	}

	/**
	 * Returns the passenger's associated number of itineraries.
	 *
	 * @return the passenger's associated number of itineraries.
	 */
	int getNumberOfItineraries() {
		return _itineraries.size();
	}

	/**
	 * Returns the value of the passenger's last 10 itineraries.
	 *
	 * @return the last 10 itinerary values.
	 */
	double getLastValues() {
		double result = 0;

		for ( double value : _lastValues ) {
			result += value;
		}

		return result;
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
	 * Returns a String description of a passenger.
	 * 
	 * @return String description of a passenger.
	 */
	public String toString() {

		/* Basic attributes */
		int id = getId();
		String name = getName();
		String catName = _category.getName();
		int itineraries = getNumberOfItineraries();
		String values = String.format(Locale.US, "%.2f", _totalSpent);

		/* Formatting time */
		Duration duration = getItineraryDuration();
		String formatHours = String.format("%02d", duration.toHours());
		duration = duration.minusHours(duration.toHours());
		String formatMinutes = String.format("%02d", duration.toMinutes());
		String time = formatHours +":"+ formatMinutes;

		/* Returns Passenger */
		return "" + id + "|" + name + "|" + catName + "|" + itineraries + "|" + values + "|" + time;
	}

	/**
	 * Adds a new itinerary bought by this passenger.
	 * 
	 * @param itinerary bought itinerary.
	 */
	void addItinerary(Itinerary itinerary) {

		/* Update Number of Itineraries */
		int n = getNumberOfItineraries();
		n++;
		itinerary.updateId(n);
		_itineraries.put(n, itinerary);

		/* Calculates the value paid for the itinerary and saves it */
		double value = itinerary.getCost();
		try {
			if (_lastValues.size() == 10) {
				_lastValues.take();
			}

			value = value * ((100 - _category.getDiscountPercentage()) / 100);
			_lastValues.add(value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/* Update Spent Money */
		_totalSpent = _totalSpent + value;

		/* Updates the Passenger Category */
		_category = _trainCompany.updateCategory(getLastValues());

	}

	/**
	 * Displays all the itineraries bought by this passenger.
	 * 
	 * @return a string with all the itineraries description.
	 */
	String showItineraries() {
		StringBuffer buf = new StringBuffer();
		_itineraries.forEach((Integer i, Itinerary it)-> {
			buf.append(it.toString() + "\n");
		});
		return buf.toString();
	}

	/**
	 * Displays all the itineraries bought by this passenger.
	 * 
	 * @return a string with all the itineraries description.
	 */
	Duration getItineraryDuration() {
		Duration duration = Duration.ZERO;
		for ( Map.Entry<Integer, Itinerary> entry : _itineraries.entrySet() ) {
			Itinerary it = entry.getValue();
			duration = duration.plus(it.getDuration());
		}
		return duration;
	}


}