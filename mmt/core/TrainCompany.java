package mmt.core;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadEntryException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchDepartureException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import mmt.core.exceptions.NonUniquePassengerNameException;

//FIXME import other classes if necessary

/**
 * A train company has schedules (services) for its trains and passengers that
 * acquire itineraries based on those schedules.
 */
public class TrainCompany implements java.io.Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301010L;

	/**
	 * The passengers held by the editor indexed by unique identifier.
	 */
	private Map<Integer, Passenger> _passengersMap = new TreeMap<Integer, Passenger>();

	/**
	 * Give each passenger a unique identifier.
	 */
	private int _nextId;

	void importFile(String filename) {
	}		

	/**
	 * Add passenger.
	 * 
	 * @param thepassenger to add.
	 * 
	 * @return the added passengers's id.
	 */
	public final int addPassenger(Passenger p) {
		int id = ++_nextId;
		_passengersMap.put(id, p);
		return id;
	}

	/**
	 * Get a passenger given its identifier.
	 * 
	 * @param id the passenger's identifier.
	 * 
	 * @return the passenger with the given identifier, or null if the passenger does not
	 *         exist.
	 */
	private final Passenger getPassenger(int id) {
		return _passengersMap.get(id);
	}

	/**
	 * Check whether a passenger exists (given an identifier).
	 * 
	 * @param id the identifier to check.
	 * 
	 * @return true, if the passenger exists; false, otherwise.
	 */
	public final boolean passengerExists(int id) {
		return getPassenger(id) != null;
	}

	/**
	 * Number of passengers held by the editor.
	 *
	 * @return the number of passengers held by the editor.
	 */
	public int numberPassengers() {
		return _passengersMap.size();
	}

	/**
	 * Remove a given passenger (by id).
	 * 
	 * @param id the identifier of the passenger to be removed.
	 * @return true if the editor contains a passenger with the specified identifier, false otherwise
	 */
	public boolean remove(int id) {
		if (_passengersMap.remove(id) == null)
			return false;
		return true;
	}
}
