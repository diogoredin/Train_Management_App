package mmt.core;

import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.Locale;
import java.util.Date;

import java.time.LocalTime;
import java.time.Duration;
import java.text.SimpleDateFormat;

import java.time.format.DateTimeParseException;
import java.text.ParseException;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;

/**
 * Class which builds the possible itineraries for the passengers.
 */

public class ItineraryBuilder implements Visitor {

	/** The TrainCompany associated to this ItineraryBuilder */
	private TrainCompany _company;

	/** Name of the starting station of the itinerary */
	private String _startStation;

	/** Name of the ending station of the itinerary */
	private String _endStation;

	/** Departure date for the itinerary */
	private Date _departureDate;

	/** Minimum departure time for the itinerary */
	private LocalTime _departureTime;

	/** List of starting Service candidates */
	private ArrayList<Service> _startingServices = new ArrayList<Service>();

	/** List of singular Services which can be itineraries by themselves */
	private ArrayList<Service> _singleServices = new ArrayList<Service>();

	/** Services that connect from a given station */
	private ArrayList<ArrayList<TrainStop>> _intersections = new ArrayList<ArrayList<TrainStop>>();

	/** TrainStops that serve as a possible start to the itinerary */
	private ArrayList<TrainStop> _start = new ArrayList<TrainStop>();

	/** Possible itineraries */
	private ArrayList<ArrayList<TrainStop>> _result = new ArrayList<ArrayList<TrainStop>>();

	/** Services the Itinerary passes through */
	private ArrayList<Service> _services = new ArrayList<Service>();

	/** Stations where the passenger gets on or off */
	private ArrayList<String> _switchStation = new ArrayList<String>();

	/** Built Itineraries to be returned. */
	private ArrayList<Itinerary> _itineraries = new ArrayList<Itinerary>();

	/** Current best composed itinerary */
	private Itinerary _composed;


	ItineraryBuilder(TrainCompany trainCompany) {
		_company = trainCompany;
	}

	ItineraryBuilder( String startStation, String endStation, String departureDate, String departureTime, TrainCompany trainCompany ) 
	throws BadTimeSpecificationException, BadDateSpecificationException {

		_startStation = startStation;
		_endStation = endStation;
		_company = trainCompany;

		try {
			_departureTime = LocalTime.parse(departureTime);
		} catch (DateTimeParseException e) {
			throw new BadTimeSpecificationException( departureTime );
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			_departureDate = format.parse(departureDate);
		} catch (ParseException e) {
			throw new BadDateSpecificationException (departureDate);
		}
		// Starts by making a list of services with the starting station, and those that also contain the end station 
		_company.getServices().forEach(( Service s )-> {
			s.accept(this);
		});

		_singleServices.forEach((Service single) -> {
			_itineraries.add(buildItinerary(_departureDate, single, _startStation, _endStation));
		});

		this.searchComposedItinerary();
		this.getItineraries();
	}

	public void visit ( Service service ) {

		Collection<TrainStop> startStops = service.getStartTrainStops();
		boolean isValidStart = false;
		LocalTime startTime = LocalTime.MIN;

		for ( TrainStop stop : startStops ) {

			// checks if the service has the start station
			if ( stop.getStation().getName().equals( _startStation ) && !stop.getTime().isBefore( _departureTime ) ) {

				_startingServices.add( service );
				isValidStart = true;
				startTime = stop.getTime();
				break;

			}
		}

		if (!isValidStart) return;

		Collection<TrainStop> endStops = service.getEndTrainStops();

		for ( TrainStop stop : endStops ) {

			// If the service has both the start and end station, it is a valid service.
			if ( stop.getStation().getName().equals( _endStation ) && startTime.isBefore(stop.getTime())) {
				_singleServices.add( service );
				_startingServices.remove( service );
				break;
			}
		}

	}

	/**
	 * Returns a list containing the TrainStops between and including the startStation and endStation
	 * TrainStops in the corresponding Service.
	 *
	 * @param service the service to be checked.
	 * @param startStation the station where the list starts.
	 * @param endStation the station where the list ends.
	 * @return the ArrayList containing the TrainStops between the start and end stations in this service.
	 */
	ArrayList<TrainStop> stopsInService(Service service, String startStation, String endStation) {

		Collection<TrainStop> startStops = service.getStartTrainStops();
		Collection<TrainStop> endStops = service.getEndTrainStops();

		ArrayList<TrainStop> itineraryStops = new ArrayList<TrainStop>();

		boolean firstStation = false;
		int j = 0;

		for ( TrainStop stop : startStops ) {
			if ( stop.getStation().getName().equals(startStation) ) {

				if ( j == 0 ) firstStation = true;

				itineraryStops.add(stop);
				break;
			}

			j++;
		}

		boolean hasStarted = false;

		for ( TrainStop stop : endStops ) {

			if (firstStation) hasStarted = true;

			if (!hasStarted && stop.getStation().getName().equals(startStation)) {
				 hasStarted = true;
			} else if (hasStarted) {
				itineraryStops.add(stop);

				if (stop.getStation().getName().equals(endStation)) break;
			}
		}

		return itineraryStops;
	}

	/** 
	 * Creates a new Itinerary using the service it passes through, and the start and end statio names.
	 * <p>
	 * Only creates simple Itineraries.
	 *
	 * @param departureDate the date of the itinerary.
	 * @param service the single service the itinerary travels through.
	 * @param startStation the name of the station where the itinerary starts.
	 * @param endStation the name of the station where the itinerary ends.
	 * @return the simple itinerary built from the service.
	 */
	Itinerary buildItinerary(Date departureDate, Service service, String startStation, String endStation) {

		ArrayList<TrainStop> itineraryStops = new ArrayList<TrainStop>();
		ArrayList<TrainStop> serviceStops = this.stopsInService(service, startStation, endStation);
		itineraryStops.addAll(serviceStops);

		return new Itinerary (departureDate, itineraryStops);
	}

	/**
	 * Creates a new Itinerary from the list of Services it passes through, 
	 * and the name of the Stations it switches service.
	 * <p>
	 * May create simple or composed itineraries. Using a single service and two switch stations 
	 * (the start and end stations) will result in an equivalent simple itinerary as if one had used
	 * the buildItinerary(4) method with equivalent parameters.
	 * 
	 * @param departureDate the date of the itinerary.
	 * @param services the list of the services this itinerary passes through.
	 * @param switchStations the name of the stations where the passenger enters or leaves a service.
	 * @return the itinerary built from the parameters.
	 */
	Itinerary buildItinerary(Date departureDate, ArrayList<Service> services, ArrayList<String> switchStations) {

		ArrayList<TrainStop> itineraryStops = new ArrayList<TrainStop>();

		for (int i = 0; i < services.size(); i++) {
			Service service = services.get(i);
			String startStation = switchStations.get(i);
			String endStation = switchStations.get(i + 1);

			ArrayList<TrainStop> serviceStops = this.stopsInService(service, startStation, endStation);
			itineraryStops.addAll(serviceStops);
		}

		return new Itinerary (departureDate, itineraryStops);
	}

	/**
	 * Creates a new Itinerary from the list of Services it passes through, 
	 * and the name of the Stations it switches service.
	 * <p>
	 * May create simple or composed itineraries. Using a single service and two switch stations 
	 * (the start and end stations) will result in an equivalent simple itinerary as if one had used
	 * the buildItinerary(4) method with equivalent parameters.
	 * 
	 * @param departureDate the date of the itinerary.
	 * @param services the list of the services this itinerary passes through.
	 * @param switchStations the name of the stations where the passenger enters or leaves a service.
	 * @return the itinerary built from the parameters.
	 */
	Itinerary buildItinerary(Date departureDate, ArrayList<Service> services, ArrayList<String> switchStations, int passengerId) {
		Itinerary result = this.buildItinerary(departureDate, services, switchStations);
		result.setPassengerId(passengerId);
		return result;
	}
 	

	/**
	 * Grabs the next trainstops.
	 *
	 * @return the next trainstops.
	 */
	ArrayList<TrainStop> nextTrainStops(TrainStop trainstop) {

		/* The childs of the inputted trainstop */
		ArrayList<TrainStop> result = new ArrayList<TrainStop>();

		/* Goes through our intersections */
		for ( ArrayList<TrainStop> list : _intersections ) {

			/* Get the first element */
			TrainStop parent = list.get(0);

			/* When the trainstops match we can get our childs */
			if ( trainstop.getStation().getName().equals(parent.getStation().getName()) &&
				 trainstop.getTime().equals(parent.getTime()) &&
				 trainstop.getService().getId() == parent.getService().getId()) {

				for ( int i = 1; i < list.size(); i++ ) {
					result.add(list.get(i));
				}
			}

		}

		return result;

	}

	/**
	 * Searches possible composed itineraries.
	 */
	void searchComposedItinerary() {

		/* Train Stops */
		ArrayList<TrainStop> trainStops = _company.getTrainStops();

		/* Possible Starting Train Stops */
		ArrayList<TrainStop> startTrainStops = new ArrayList<TrainStop>();

		/* Build a table with the trainstops that intersect */
		for ( TrainStop trainstop_a : trainStops ) {
			ArrayList<TrainStop> intersects = new ArrayList<TrainStop>();

			/* Add first one */
			intersects.add(trainstop_a);
		
			/* Check which intersect */
			for ( TrainStop trainstop_b : trainStops ) {

				if (trainstop_a.getStation().getName().equals(trainstop_a.getService().getStartStation().getName())) {
					intersects.add(trainstop_a.nextTrainStop());
					break;
				} else if (trainstop_a.getStation().getName().equals( trainstop_b.getStation().getName() ) && 
					!trainstop_a.getTime().isAfter( trainstop_b.getTime())) {

					if (trainstop_a.getService().getId() == trainstop_b.getService().getId()) {
						intersects.add(trainstop_b.nextTrainStop());
					} else {
						intersects.add(trainstop_b);
					}
				}

			}

			/* Stations must match and the time should be after the departure we want */
			if (_startStation.equals( trainstop_a.getStation().getName()) && 
				!_departureTime.isAfter( trainstop_a.getTime()) &&
				_startingServices.contains(trainstop_a.getService()) ) {
				startTrainStops.add(trainstop_a);
			}

			/* Finally add it to intersections */
			if ( intersects.size() > 1 ) { _intersections.add(intersects); }

		}

		/* Launches DFS on every TrainStop */
		for ( TrainStop trainStop : startTrainStops ) {
			/*

			for ( ArrayList<TrainStop> list : _intersections ) {
				System.out.println("---------");
				for ( TrainStop stop : list ) {
					System.out.println(stop.getTime()+"|"+stop.getStation().getName()+"|"+stop.getService().getId());
				}
			}

			//*/
			/* Applies DFS starting there */
			this.depthFirstSearch( trainStop );
		}

	}

	/* DEPTH FIRST ALGORITHM */
	void depthFirstSearch(TrainStop trainstop) {

		ArrayList<TrainStop> newPath = new ArrayList<TrainStop>();
		nextSegment(trainstop, newPath);


	}

	void nextSegment(TrainStop trainstop, ArrayList<TrainStop> path) {
		ArrayList<TrainStop> nextStops =  this.nextTrainStops(trainstop);

		if ( trainstop.getStation().getName().equals(_endStation)) {
			ArrayList<TrainStop> nextPath = new ArrayList<TrainStop>(path);
			nextPath.add( trainstop );
			_result.add( nextPath );
			return;
		}

		for ( TrainStop next : nextStops) {
			boolean nextValid = true;
			for ( int i = 0; i < path.size() - 1; i++ ) {
				if ( path.get(i).getStation().getName().equals(next.getStation().getName())) {
					nextValid = false;
					break;
				}
			}
			if ( nextValid && validService(next, path)) {
				ArrayList<TrainStop> nextPath = new ArrayList<TrainStop>(path);
				nextPath.add( trainstop );
				nextSegment(next, nextPath);
			}
			
		}
	}

	boolean validService(TrainStop trainstop, ArrayList<TrainStop> path) {
		if (path.size() == 0) {
			return true;
		}
		int lastId = path.get(path.size() -1).getService().getId();
		if ( lastId == trainstop.getService().getId()) {
			return true;
		} else {
			for ( int i = 0; i < path.size() - 1; i++) {
				if ( path.get(i).getService().getId() == trainstop.getService().getId()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Grabs the possible itineraries.
	 */
	void getItineraries() {

		/* Parses all obtained paths, and chooses the best one */
		_result.forEach((ArrayList<TrainStop> list)-> { 
			this.addComposedItinerary(new Itinerary(_departureDate, list));
		});
		
		this.commitComposedItinerary();
	}

	ArrayList<Itinerary> getItineraryOptions() {
		return _itineraries;
	}

	boolean hasComposedItinerary() {
		return _composed != null;
	}

	void addComposedItinerary(Itinerary newItinerary) {
		if (!this.hasComposedItinerary()) {
			_composed = newItinerary;
		} else if (_composed.getDuration() == newItinerary.getDuration()) {
			if ( _composed.getCost() > newItinerary.getCost()) {
				_composed = newItinerary;
			}
		} else if (_composed.getDuration().compareTo(newItinerary.getDuration()) > 0) {
			_composed = newItinerary;
		} else if (_composed.getCost() > newItinerary.getCost()) {
			_composed = newItinerary;
		}
	}

	void commitComposedItinerary() {
		if (this.hasComposedItinerary()) {
			_itineraries.add( _composed );
		}
		_composed = null;
	}

}