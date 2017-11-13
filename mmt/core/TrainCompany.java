package mmt.core;

import mmt.core.categories.CategoryManager;
import mmt.core.categories.Category;
import mmt.core.NewParser;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import mmt.core.exceptions.ImportFileException;
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

/**
 * A train company has schedules (services) for its trains and passengers that
 * acquire itineraries based on those schedules.
 */
public class TrainCompany implements java.io.Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301010L;

	/**
	 * The passengers held by the train company indexed by unique identifier.
	 */
	private Map<Integer, Passenger> _passengersMap = new TreeMap<Integer, Passenger>();

	/**
	 * The services held by the train company indexed by unique identifier.
	 */
	private Map<Integer, Service> _servicesMap = new TreeMap<Integer, Service>();

	/**
	 * The different categories for a passenger.
	 */
	private CategoryManager _categories = new CategoryManager();

	/**
	 * Give each passenger a unique identifier.
	 */
	private int _nextPassengerId;

	/**
	 * Give each service a unique identifier.
	 */
	private int _nextServiceId;

	/**
	 * Constructor.
	 */
	public TrainCompany() {
		_nextPassengerId = -1;
	}

	/**
	 * Add passenger.
	 * 
	 * @param thepassenger to add.
	 * 
	 * @return the added passengers's id.
	 */
	public final int addPassenger(Passenger p) {
		int id = ++_nextPassengerId;
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
	private final Passenger getPassenger(int id) throws NoSuchPassengerIdException {
		return _passengersMap.get(id);
	}

	/**
	 * Check whether a passenger exists (given an identifier).
	 * 
	 * @param id the identifier to check.
	 * 
	 * @return true, if the passenger exists; false, otherwise.
	 */
	public final boolean passengerExists(int id) throws NoSuchPassengerIdException {
		return getPassenger(id) != null;
	}

	/**
	 * Changes a given passenger name.
	 * 
	 * @param id of the passenger.
	 * @param newname the new name to give the passenger.
	 * 
	 * @return the new passengers name.
	 */
	public String changePassengerName(int id, String newname) throws NoSuchPassengerIdException, InvalidPassengerNameException {
		if (passengerExists(id)) {
			getPassenger(id).setName(newname);
			return getPassenger(id).toString();
		} else {
			throw new NoSuchPassengerIdException(id);
		}
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
	
	public Category updateCategory(double value) {
		return _categories.getCategory(value);
	}

	public String getPassengerDescription(int id) throws NoSuchPassengerIdException {
		if (passengerExists(id)) {
			return getPassenger(id).toString();
		} else {
			throw new NoSuchPassengerIdException(id);
		}
	}

	/**
	 * Collection of all the passengers part of this trainCompany.
	 *
	 * @return the collection of passengers of this trainCompany ordered by ID.
	 */
	public Collection<Passenger> getPassengers() {
		Collection<Passenger> passengers = _passengersMap.values();
		return Collections.unmodifiableCollection(passengers);
	}

	/**
	 * Resetes the passengers list of this train company.
	 */
	public void deletePassengers() {
		_passengersMap.clear();
	}

	/**
	 * Add Service.
	 * 
	 * @param theservice to add.
	 * 
	 * @return the added service id.
	 */
	public final int addService(int id, Service s) {
		_servicesMap.put(id, s);
		return id;
	}

	/**
	 * Get a service given its identifier.
	 * 
	 * @param id the service's identifier.
	 * 
	 * @return the service with the given identifier, or null if the service does not
	 *         exist.
	 */
	public final Service getService(int id) throws NoSuchServiceIdException {
		return _servicesMap.get(id);
	}

	/**
	 * Check whether a service exists (given an identifier).
	 * 
	 * @param id the identifier to check.
	 * 
	 * @return true, if the service exists; false, otherwise.
	 */
	public final boolean serviceExists(int id) throws NoSuchServiceIdException {
		return ( getService(id) != null );
	}

	/**
	 * Collection of all the services part of this trainCompany.
	 *
	 * @return the collection of services of this trainCompany ordered by ID.
	 */
	public Collection<Service> getServices() {
		Collection<Service> services = _servicesMap.values();
		return Collections.unmodifiableCollection(services);
	}

	/**
	 * Resetes the services list of this train company.
	 */
	public void deleteServices() {
		_servicesMap.clear();
	}


}
