package mmt.core;

import java.util.Comparator;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Collections;

import java.time.LocalTime;
import java.time.LocalDate;

import mmt.core.NewParser;

import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;

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

	/** The station names held by the train company. */
	private ArrayList<String> _stationsList = new ArrayList<String>();

	/* The different categories for a passenger. */
	private CategoryManager _categories = new CategoryManager();

	/* Give each passenger a unique identifier. */
	private int _nextPassengerId;

	/* Give each service a unique identifier. */
	private int _nextServiceId;

	/**
	 * Constructor.
	 */
	TrainCompany() {
		_nextPassengerId = 0;
	}

	/**
	 * Add passenger.
	 * 
	 * @param p the passenger to add.
	 */
	void addPassenger(Passenger p) {
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
	Passenger getPassenger(int id) throws NoSuchPassengerIdException {
		if (passengerExists(id)) {
			return _passengersMap.get(id);
		} else {
			throw new NoSuchPassengerIdException (id);
		}
	}

	/**
	 * @return the next passenger's assigned id.
	 */
	int getNextPassengerId() {
		return _nextPassengerId;
	}

	/**
	 * Check whether a passenger exists (given an identifier).
	 * 
	 * @param id the identifier to check.
	 * 
	 * @return true, if the passenger exists; false, otherwise.
	 */
	boolean passengerExists(int id) {
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
	Category updateCategory(double value) {
		return _categories.getCategory(value);
	}

	/**
	 * Returns a passenger's String description, given it's id.
	 *
	 * @param id the passenger's id.
	 * @return passenger String description.
	 */
	String getPassengerDescription(int id) throws NoSuchPassengerIdException {
		if (passengerExists(id)) {
			return getPassenger(id).toString();
		} else {
			throw new NoSuchPassengerIdException(id);
		}
	}

	/**
	 * @return the collection of passengers of this trainCompany ordered by id.
	 */
	Collection<Passenger> getPassengers() {
		Collection<Passenger> passengers = _passengersMap.values();
		return Collections.unmodifiableCollection(passengers);
	}

	/**
	 * Resets the passengers list of this train company.
	 */
	void deletePassengers() {
		_passengersMap.clear();
	}

	/**
	 * Add station.
	 * 
	 * @param s the station to add.
	 */
	void addStation(String s) {
		_stationsList.add(s);
	}

	/**
	 * Checks if a station exists.
	 * 
	 * @param s the station to check.
	 * @return true if the station exists.
	 */
	boolean checkStation(String s) {
		return _stationsList.contains(s);
	}

	/**
	 * Add Service.
	 * 
	 * @param id the service's id.
	 * @param service the service to add.
	 */
	void addService(int id, Service service) {
		_servicesMap.put(id, service);
	}

	/**
	 * Get a service given its identifier.
	 * 
	 * @param id the service's identifier.
	 * @return the service with the given identifier.
	 */
	Service getService(int id) throws NoSuchServiceIdException {
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
	boolean serviceExists(int id) {
		return ( _servicesMap.get(id) != null );
	}

	/**
	 * @return the collection of services of this trainCompany ordered by id.
	 */
	Collection<Service> getServices() {
		Collection<Service> services = _servicesMap.values();
		return Collections.unmodifiableCollection(services);
	}

	/**
	 * Looks up a service with a given start station name.
	 *
	 * @param search the station name to look for.
	 * @return the service that has the search start station.
	 */
	String searchServiceWithStartStation( String search ) throws NoSuchStationNameException {

		if (checkStation(search)) {
			
			/* Converts the collection to an array to be sorted */
			ArrayList<Service> services = new ArrayList<Service>( getServices() );

			/* Compares two services based on their start time */
			Comparator<Service> comparator = new Comparator<Service>() {
				@Override
				public int compare(Service left, Service right) {
					return left.getStartTime().toSecondOfDay() - right.getStartTime().toSecondOfDay();
				}
			};

			/* StringBuffer for efficient appending of service descriptions */
			StringBuffer buf = new StringBuffer();

			/* Sorts the collection */
			Collections.sort(services, comparator);

			/* Search for the service */
			for ( Service s : services ) {

				if ( search.equals( s.getStartStation().getName() ) ) {
					buf.append ( s.toString() + '\n' );
				}

			}

			/* Converting the buffer to a String */
			String result = buf.toString();

			return result;

		} else {
			throw new NoSuchStationNameException(search);
		}

	}

	/**
	 * Looks up a service with a given end station name.
	 *
	 * @param search the station name to look for.
	 * @return the service that has the search end station.
	 */
	String searchServiceWithEndStation( String search ) throws NoSuchStationNameException {

		if (checkStation(search)) {

			/* Converts the collection to an array to be sorted */
			ArrayList<Service> services = new ArrayList<Service>( getServices() );

			/* Compares two services based on their start time */
			Comparator<Service> comparator = new Comparator<Service>() {
				@Override
				public int compare(Service left, Service right) {
					return left.getEndTime().toSecondOfDay() - right.getEndTime().toSecondOfDay();
				}
			};

			/* StringBuffer for efficient appending of service descriptions */
			StringBuffer buf = new StringBuffer();

			/* Sorts the collection */
			Collections.sort(services, comparator);

			/* Search for the service */
			for ( Service s : services ) {

				if ( search.equals( s.getEndStation().getName() ) ) {
					buf.append( s.toString() + '\n' );
				}

			}

			/* Converting the buffer to a String */
			String result = buf.toString();

			return result;

		} else {
			throw new NoSuchStationNameException(search);
		}
	
	}

}
