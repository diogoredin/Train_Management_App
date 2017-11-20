package mmt.core;

import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Collections;

import mmt.core.categories.CategoryManager;
import mmt.core.categories.Category;
import mmt.core.NewParser;

import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;

/**
 * A train company has schedules (services) for its trains and passengers that
 * acquire itineraries based on those schedules.
 */
public class TrainCompany implements java.io.Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301010L;

	/** The passengers held by the train company indexed by unique identifier. */
	private Map<Integer, Passenger> _passengersMap = new TreeMap<Integer, Passenger>();

	/** The services held by the train company indexed by unique identifier. */
	private Map<Integer, Service> _servicesMap = new TreeMap<Integer, Service>();

	/* The different categories for a passenger. */
	private CategoryManager _categories = new CategoryManager();

	/* Give each passenger a unique identifier. */
	private int _nextPassengerId;

	/* Give each service a unique identifier. */
	private int _nextServiceId;

	/**
	 * Constructor.
	 */
	public TrainCompany() {
		_nextPassengerId = 0;
	}

	/**
	 * Add passenger.
	 * 
	 * @param p the passenger to add.
	 */
	public final void addPassenger(Passenger p) {
		int id = _nextPassengerId++;
		_passengersMap.put(id, p);
		p.setCategory ( updateCategory( p.getLastValues() ) );

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
		if (passengerExists(id)) {
			return _passengersMap.get(id);
		} else {
			throw new NoSuchPassengerIdException (id);
		}
	}

	/**
	 * @return the next passenger's assigned id.
	 */
	public int getNextPassengerId() {
		return _nextPassengerId;
	}

	/**
	 * Check whether a passenger exists (given an identifier).
	 * 
	 * @param id the identifier to check.
	 * 
	 * @return true, if the passenger exists; false, otherwise.
	 */
	public final boolean passengerExists(int id) {
		return _passengersMap.get(id) != null;
	}

	/**
	 * Changes a given passenger name.
	 * 
	 * @param id of the passenger.
	 * @param newname the new name to give the passenger (non-null, must not be an empty String).
	 */
	void changePassengerName(int id, String newname) throws NoSuchPassengerIdException, InvalidPassengerNameException {
		if (passengerExists(id)) {
			getPassenger(id).setName(newname);
		} else {
			throw new NoSuchPassengerIdException(id);
		}
	}

	/**
	 * Returns the category associated with the minimum value.
	 *
	 * @return associated category.
	 */
	public Category updateCategory(double value) {
		return _categories.getCategory(value);
	}

	/**
	 * Returns a passenger's String description, given it's id.
	 *
	 * @param id the passenger's id.
	 * @return passenger String description.
	 */
	public String getPassengerDescription(int id) throws NoSuchPassengerIdException {
		if (passengerExists(id)) {
			return getPassenger(id).toString();
		} else {
			throw new NoSuchPassengerIdException(id);
		}
	}

	/**
	 * @return the collection of passengers of this trainCompany ordered by id.
	 */
	public Collection<Passenger> getPassengers() {
		Collection<Passenger> passengers = _passengersMap.values();
		return Collections.unmodifiableCollection(passengers);
	}

	/**
	 * Resets the passengers list of this train company.
	 */
	public void deletePassengers() {
		_passengersMap.clear();
	}

	/**
	 * Add Service.
	 * 
	 * @param id the service's id.
	 * @param service the service to add.
	 */
	public final void addService(int id, Service service) {
		_servicesMap.put(id, service);
	}

	/**
	 * Get a service given its identifier.
	 * 
	 * @param id the service's identifier.
	 * @return the service with the given identifier.
	 */
	public final Service getService(int id) throws NoSuchServiceIdException {
		if (serviceExists(id)) {
			return _servicesMap.get(id);
		} else {
			throw new NoSuchServiceIdException(id);
		}
	}

	/**
	 * Check whether a service exists (given an identifier).
	 * 
	 * @param id the identifier to check.
	 * @return true, if the service exists; false, otherwise.
	 */
	public final boolean serviceExists(int id) {
		return ( _servicesMap.get(id) != null );
	}

	/**
	 * @return the collection of services of this trainCompany ordered by id.
	 */
	public Collection<Service> getServices() {
		Collection<Service> services = _servicesMap.values();
		return Collections.unmodifiableCollection(services);
	}
}
