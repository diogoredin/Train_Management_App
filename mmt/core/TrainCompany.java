package mmt.core;

import java.util.Comparator;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;

import mmt.core.NewParser;

import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.BadDateSpecificationException;

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

	/** The train stops held by the train company. */
	private ArrayList<TrainStop> _trainStops = new ArrayList<TrainStop>();

	/* The different categories for a passenger. */
	private CategoryManager _categories = new CategoryManager();

	/* Give each passenger a unique identifier. */
	private int _nextPassengerId;

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
	 * Returns the next passenger's assigned id.
	 *
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
	 * Returns the collection of passengers of this TrainCompany.
	 *
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
		_nextPassengerId = 0;
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
	 * Returns the collection of services of this TrainCompany.
	 *
	 * @return the collection of services of this trainCompany ordered by id.
	 */
	Collection<Service> getServices() {
		Collection<Service> services = _servicesMap.values();
		return Collections.unmodifiableCollection(services);
	}

	/**
	 * Looks up services with a given start station name.
	 *
	 * @param search the station name to look for.
	 * @return the services that have the search start station.
	 */
	Collection<Service> searchServiceWithStartStation( String search ) throws NoSuchStationNameException {

		if (checkStation(search)) {
			
			/* Converts the collection to an array to be sorted */
			ArrayList<Service> services = new ArrayList<Service>( getServices() );

			/* Service collection to be returned */
			Collection<Service> result = new ArrayList<Service>();

			/* Compares two services based on their start time */
			Comparator<Service> comparator = new Comparator<Service>() {
				@Override
				public int compare(Service left, Service right) {
					return left.getStartTime().toSecondOfDay() - right.getStartTime().toSecondOfDay();
				}
			};

			/* Sorts the collection */
			Collections.sort(services, comparator);

			/* Search for the service */
			services.forEach ((Service s)-> {
				if ( search.equals( s.getStartStation().getName() ) ) {
					result.add(s);
				}
			});

			return Collections.unmodifiableCollection(result);

		} else {
			throw new NoSuchStationNameException(search);
		}

	}

	/**
	 * Looks up services with a given end station name.
	 *
	 * @param search the station name to look for.
	 * @return the services that have the search end station.
	 */
	Collection<Service> searchServiceWithEndStation( String search ) throws NoSuchStationNameException {

		if (checkStation(search)) {

			/* Converts the collection to an array to be sorted */
			ArrayList<Service> services = new ArrayList<Service>( getServices() );

			/* Service collection to be returned */
			Collection<Service> result = new ArrayList<Service>();

			/* Compares two services based on their start time */
			Comparator<Service> comparator = new Comparator<Service>() {
				@Override
				public int compare(Service left, Service right) {
					return left.getEndTime().toSecondOfDay() - right.getEndTime().toSecondOfDay();
				}
			};

			/* Sorts the collection */
			Collections.sort(services, comparator);

			/* Search for the service */
			services.forEach((Service s)-> {
				if ( search.equals( s.getEndStation().getName() ) ) {
					result.add(s);
				}
			});

			return Collections.unmodifiableCollection(result);

		} else {
			throw new NoSuchStationNameException(search);
		}
	
	}

	/**
	 * Add TrainStop.
	 * 
	 * @param trainstop the trainstop to add.
	 */
	void addTrainStop(TrainStop trainstop) {
		_trainStops.add(trainstop);
	}

	/**
	 * Get TrainStops.
	 * 
	 * @return the trainstops of the company.
	 */
	ArrayList<TrainStop> getTrainStops() {
		return _trainStops;
	}

	/**
	 * Looks up all possible itineraries for a given passenger.
	 *
	 * @param passengerId id of the passenger that is looking for the itinerary.
	 * @param departureStation the station where the passenger wants to start his trip.
	 * @param arrivalStation the date when the trip should start.
	 * @param departureDate the time when the trip should start.
	 * @return the possible itineraries for the passenger to buy.
	 * @throws NoSuchStationNameException if station name does not exist.
	 * @throws NoSuchPassengerIdException if the passenger id does not exist.
	 * @throws BadTimeSpecificationException if the time isn't well formatted.
	 * @throws BadDateSpecificationException if the date isn't well formatted.
	 */
	ArrayList<Itinerary> searchItineraries(int passengerId, String departureStation, String arrivalStation, String departureDate,
			String departureTime) 
			throws NoSuchPassengerIdException, BadTimeSpecificationException, NoSuchStationNameException, BadDateSpecificationException {

		if ( !passengerExists( passengerId ) ) {
			throw new NoSuchPassengerIdException( passengerId );
		}

		if ( !checkStation( departureStation ) ) {
			throw new NoSuchStationNameException( departureStation );

		} else if ( !checkStation( arrivalStation ) ) {
			throw new NoSuchStationNameException( arrivalStation );
		}

		ItineraryBuilder builder = new ItineraryBuilder(departureStation, arrivalStation, departureDate, departureTime, this);
		return builder.getItineraryOptions();
	}

	/**
	 * Adds an itinerary to a passenger.
	 *
	 * @param id the id of the passenger who bought the itinerary. 
	 * @param itinerary the chosen itinerary.
	 * @throws NoSuchPassengerIdException if the passenger id does not exist.
	 */
	public void commitItinerary(int passengerId, Itinerary itinerary) throws NoSuchPassengerIdException {
		getPassenger(passengerId).addItinerary(itinerary);
	}
	
	/**
	 * Displays the itineraries bought by a given passenger.
	 *
	 * @param id passenger id to whom the itinaries belong.
	 * @return the itineraries as a string.
	 * @throws NoSuchPassengerIdException if the passenger id does not exist.
	 */
	String showPassengerItineraries(int id) throws NoSuchPassengerIdException {
		StringBuffer buf = new StringBuffer();
		String name = getPassenger(id).getName();
		buf.append("== Passageiro " + id +": " + name + " ==\n");
		buf.append(getPassenger(id).showItineraries());
		return buf.toString();
	}

	/**
	 * Let's us know if a given passenger has purchased itineraries.
	 *
	 * @param id the id of the passenger.
	 * @return true if the passenger has itineraries, false otherwise.
	 * @throws NoSuchPassengerIdException if the passenger id does not exist.
	 */
	boolean passengerHasItineraries(int id) throws NoSuchPassengerIdException {
		return getPassenger(id).getNumberOfItineraries() > 0;
	}

	/**
	 * Looks up all itineraries in the TrainCompany.
	 *
	 * @return all itineraries in the TrainCompany.
	 */
	String showAllItineraries() {
		StringBuffer buf = new StringBuffer();
		_passengersMap.forEach((Integer i, Passenger p) -> {
			if ( p.getNumberOfItineraries() > 0 ) {
				buf.append("== Passageiro " + i +": " + p.getName() + " ==\n");
				buf.append(p.showItineraries());
			}
		});
		return buf.toString();
	}

	/**
	 * Updates all itinerary id's by one.
	 */
	public void updateListId(ArrayList<Itinerary> itineraryOptions) {
		for (int i = 0; i < itineraryOptions.size(); i++) {
			itineraryOptions.get(i).updateId(i + 1);
		}
	}
}