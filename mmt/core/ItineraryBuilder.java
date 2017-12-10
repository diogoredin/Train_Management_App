package mmt.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.Locale;

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



	ItineraryBuilder( String startStation, String endStation, String departureDate, 
		String departureTime, TrainCompany trainCompany ) throws BadTimeSpecificationException {

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

		/*

		_startingServices.forEach(( Service start) -> {
			BuiltItinerary built = new BuiltItinerary(false, start, _startStation, _endStation, _company);
			if (built.isValid()) {
				_company.addItineraryOption(built);
			}
		});
		*/


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
}