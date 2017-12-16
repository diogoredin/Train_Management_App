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
	private ArrayList<ArrayList<TrainStop>> _result = new ArrayList<ArrayList<TrainStop>>();

	/** Services the Itinerary passes through */
	private ArrayList<Service> _services = new ArrayList<Service>();

	/** Stations where the passenger gets on or off */
	private ArrayList<String> _switchStation = new ArrayList<String>();

	private boolean found = false;


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
				 trainstop.getTime().equals(parent.getTime()) &&
				 trainstop.getService().getId() == parent.getService().getId()) {

				for ( int i = 1; i < list.size(); i++ ) {
					if ( list.get(i).hasNextTrainStop() ) {
						result.add(list.get(i).nextTrainStop());
					}
				}

			}

		}
		return result;

	}
	/*
	{class Station:
	def __init__(self, name, trainStopArray):
		self.name = name
		self.trainStopArray = trainStopArray

class TrainStop:
	def __init__(self, station, segment, service):
		self.station = station
		self.segment = segment
		self.service = service

def find_segmentos(stationDeparture, stationArrival):
	res = []
	find_segmentos_aux(stationDeparture, stationArrival, [], res)
	return res

def find_segmentos_aux(stationDeparture, stationArrival, array, res):
	for trainStop in stationDeparture.trainStopArray:
		if trainStop in array:
			continue
		array = array[:].append(trainStop.station) # Creates a copy of the array
		if trainStop.station == stationArrival:
			res.append(array)
			continue
		find_segmentos_aux(trainStop.station, stationArrival, array, res)}
*/
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
			ArrayList<TrainStop> intersects = new ArrayList<TrainStop>();

			/* Add first one */
			intersects.add(trainstop_a);
		
			/* Check which intersect */
			for ( TrainStop trainstop_b : trainStops ) {

				if (trainstop_a.getStation().getName().equals(trainstop_a.getService().getStartStation().getName())) {
					intersects.add(trainstop_a);
					break;
				} else if (trainstop_a.getStation().getName().equals( trainstop_b.getStation().getName() ) && 
					!trainstop_a.getTime().isAfter( trainstop_b.getTime())) {
					if (trainstop_b.hasNextTrainStop()) {
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

			/*for ( ArrayList<TrainStop> list : _intersections) {
				System.out.println( "----");
				for ( TrainStop stop : list) {
					System.out.print( stop.getTime() + "|" + stop.getStation().getName() );
					if ( stop.hasNextTrainStop()) {

						System.out.print( "->" +stop.nextTrainStop().getTime() + "|" + stop.nextTrainStop().getStation().getName());

					}
					System.out.print("\n");
				}

			}
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
		boolean hey = false;
		ArrayList<TrainStop> nextStops =  this.nextTrainStops(trainstop);

		if ( trainstop.getStation().getName().equals(_endStation)) {
			ArrayList<TrainStop> nextPath = new ArrayList<TrainStop>(path);
			nextPath.add( trainstop );
			_result.add( nextPath );
			return;
		}

		if (nextStops.size() == 0) {
			hey = true;
		}

		for ( TrainStop next : nextStops) {
			if (hey) {
				System.out.println("WTFF SIZE == 0 pls help");
			}
			boolean nextValid = true;
			for ( TrainStop previous : path) {
				if ( previous.getStation().getName().equals(next.getStation().getName())) {
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

		if (true);
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
	*
	* @return the possible itineraries.
	*/
	void getItineraries() {
		for ( ArrayList<TrainStop> list : _result ) {
			if (list.size() <= 1) {
				list.clear();
			} else {
				int prevId = list.get(0).getService().getId();
				_services.add(list.get(0).getService());
				_switchStation.add(list.get(0).getStation().getName());
				String stationName = list.get(0).getStation().getName();
				for ( TrainStop stop : list ) {

					if ( prevId != stop.getService().getId()) {
						_services.add(stop.getService());
						_switchStation.add(stationName);

						prevId = stop.getService().getId();
						
							
					}
					stationName = stop.getStation().getName();
				}

				_switchStation.add( _endStation );
				_company.addComposedItinerary(new BuiltItinerary(_services, _switchStation, _departureDate, _company, 0));
				_switchStation.clear();
				_services.clear();
			}
		}
		
		if (_result.size() > 0) {
			_company.addComposedOption();
		}

	}

}