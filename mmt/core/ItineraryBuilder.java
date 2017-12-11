package mmt.core;

import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.Locale;
import java.time.LocalDateTime;

import java.time.LocalTime;
import java.time.Duration;

import java.time.format.DateTimeParseException;

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
	private String _departureDate;

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
	private ArrayList<Stack<TrainStop>> _result = new ArrayList<Stack<TrainStop>>();

	/** Temporary holder of a itinerary solution */
	private Stack<TrainStop> _helper = new Stack<TrainStop>();

	/** Keeps track of visited stations */
	private TreeMap<String,Boolean> _visited = new TreeMap<String,Boolean>();

	/** Services the Itinerary passes through */
	private ArrayList<Service> _services = new ArrayList<Service>();

	/** Stations where the passenger gets on or off */
	private ArrayList<String> _switchStation = new ArrayList<String>();

	ItineraryBuilder( String startStation, String endStation, String departureDate, String departureTime, TrainCompany trainCompany ) throws BadTimeSpecificationException {

		_startStation = startStation;
		_endStation = endStation;
		_company = trainCompany;
		_departureDate = departureDate;

		try {
			_departureTime = LocalTime.parse(departureTime);
		} catch (DateTimeParseException e) {
			throw new BadTimeSpecificationException( departureTime );
		}

		// Starts by making a list of services with the starting station, and those that also contain the end station 
		_company.getServices().forEach(( Service s )-> {
			s.accept(this);
		});

		_singleServices.forEach(( Service single) -> {
			_company.addItineraryOption(new BuiltItinerary(true, single, _startStation, _endStation, _company, _departureDate));
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
				 !trainstop.getTime().isAfter(parent.getTime()) &&
				 !_visited.get( trainstop.getStation().getName() ) ) {

				for ( int i = 0; i < list.size() - 1; i++ ) {
					TrainStop nextStop = list.get(i).nextTrainStop();
					result.add(nextStop);
					_visited.put(trainstop.getStation().getName(), true);

				}

			}

		}

		return result;
	}

	/**
	* Searchs itinineararies
	*
	*/
	void searchComposedItinerary() {

		/* Train Stops */
		ArrayList<TrainStop> trainStops = _company.getTrainStops();

		/* Possible Starting Train Stops */
		ArrayList<TrainStop> startTrainStops = new ArrayList<TrainStop>();

		/* Build a table with the trainstops that intersect */
		for ( TrainStop trainstop_a : trainStops ) {

			/* TrainStops that intersect a */
			ArrayList<TrainStop> intersects = new ArrayList<TrainStop>();

			/* Add first one */
			intersects.add(trainstop_a);
		
			/* Check which intersect */
			for ( TrainStop trainstop_b : trainStops ) {

				/* Stations must match and the time should be after the departure we want */
				if (trainstop_a.getStation().getName().equals( trainstop_b.getStation().getName() ) && 
					!trainstop_a.getTime().isAfter( trainstop_b.getTime() )) {
					intersects.add(trainstop_b);
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

			/* Marks it as unvisited */
			_visited.put(trainstop_a.getStation().getName(), false);
		}

		/* Launches DFS on every TrainStop */
		for ( TrainStop trainstop : startTrainStops ) {

			/* Reset our help array */
			_helper = new Stack<TrainStop>();

			/* Aplies DFS starting there */
			_helper.push(trainstop);
			this.depthFirstSearch(trainstop);

			/* Resets our visited map */
			_visited.forEach((String str, Boolean logic)-> {
				_visited.put(str, false);
			});
		}

	}

	/* DEPTH FIRST ALGORITHM */
	void depthFirstSearch(TrainStop trainstop) {

		/* Grab the childs of the current trainstop */
		ArrayList<TrainStop> nextTrainStops = this.nextTrainStops(trainstop);

		/* Goes through every child */
		for ( TrainStop nextTrainStop : nextTrainStops ) {
			
			/* If we just found a solution we save it from our helper array */
			if (nextTrainStop.getStation().getName().equals(_endStation)) {

				_helper.push(nextTrainStop);

				Stack temporary = new Stack<TrainStop>();
				Stack helperCopy = new Stack<TrainStop>();
				helperCopy = (Stack<TrainStop>) _helper.clone();
				while (!helperCopy.isEmpty()) {
					TrainStop trainstopAlt = (TrainStop) helperCopy.pop();
					temporary.push(trainstopAlt);
					_visited.put(trainstopAlt.getStation().getName(), true);
				}
				_result.add(temporary);
				_helper.pop();

			/* Otherwise we keep searching */
			} else if (!_helper.contains(nextTrainStop)) {
				_helper.push(nextTrainStop);
				this.depthFirstSearch(nextTrainStop);
				_helper.pop();
			}

		}

	}

	/**
	* Grabs the possible itineraries.
	*
	* @return the possible itineraries.
	*/
	void getItineraries() {

		System.out.println(_result.size());
		for ( Stack<TrainStop> stack : _result ) {
			if (stack.size() == 1) {
				stack.clear();
			}
			TrainStop trainstop = stack.peek();
			_services.add(trainstop.getService());
			_switchStation.add(trainstop.getStation().getName());

			while ( !stack.isEmpty() ) {
				trainstop = stack.pop();
				if ( trainstop.getService().getId() != _services.get(_services.size()-1).getId() ) {
					_services.add(trainstop.getService());
					_switchStation.add(trainstop.getStation().getName());
				}
			}

			_switchStation.add(trainstop.getStation().getName());
			_company.addItineraryOption(new BuiltItinerary(_services, _switchStation, _departureDate, _company, 0));
			_switchStation.clear();
			_services.clear();
			System.out.println("idk");
		}

	}

}