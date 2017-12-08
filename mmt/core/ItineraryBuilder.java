package mmt.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.Locale;

import java.time.LocalTime;
import java.time.Duration;

/**
 * Class which builds the itineraries for the passengers.
 */

public class ItineraryBuilder implements Visitor {

	/** The TrainCompany associated to this ItineraryBuilder */
	private TrainCompany _company;

	/** Name of the starting station of the itinerary */
	private String _startStation;

	/** Name of the ending station of the itinerary */
	private String _endStation;

	/** Cost of the built itinerary */
	private double _cost;

	/** String description of the itinerary */
	private String _description;

	/** Duration of built itinerary */
	private Duration _duration;

	/** List of starting Service candidates */
	private ArrayList<Service> _startingServices = new ArrayList<Service>();

	/** List of Services the itinerary passes through */
	private ArrayList<Service> _services = new ArrayList<Service>();

	/** List of station names where the passenger gets on or off the train */
	private ArrayList<String> _switchStation = new ArrayList<String>();



	ItineraryBuilder( String startStation, String endStation, String departureDate, 
		String departureTime, TrainCompany trainCompany ) {

		_startStation = startStation;
		_endStation = endStation;
		_company = trainCompany;
		_cost = 0;
		_duration = Duration.ZERO;
		_switchStation.add( _startStation );

		// Starts by making a list of services with the starting station, and those that also contain the end station 
		_company.getServices().forEach(( Service s )-> {
			s.accept(this);
		});

		// If there are already valid candidates, there is no need to check further.
		if ( _services.size() > 0 ) {

			if ( _services.size() != 1) {
				// get the service which gets there the fastest
			}

		} else {
			// Dijkstra  
		}

		// Attribute creation for already found path

		Duration duration = Duration.ZERO;
		StringBuffer descriptionBuf = new StringBuffer();
		int i = 0;

		// Starts prevTime as a generic time stamp

		LocalTime prevTime = LocalTime.MIDNIGHT;

		for ( Service service : _services ) {
			Collection<TrainStop> stops = service.getAllTrainStops();
			StringBuffer stopsBuf = new StringBuffer();

			boolean hasStarted = false;


			for ( TrainStop stop : stops ) {
				if ( !hasStarted && stop.getStation().getName().equals( _switchStation.get(i) ) ) {

					hasStarted = true;
					prevTime = stop.getTime();

				} else {

					LocalTime time = stop.getTime();
					Station station = stop.getStation();
					String name = station.getName();

					stopsBuf.append("\n" + time.toString() + " " + name);

					// Checks if this is a station where the passenger gets off the service
					if ( !stop.getStation().getName().equals( _switchStation.get(i + 1) ) ) {

						duration = duration.plus(Duration.between(prevTime, time));
						prevTime = time;

					} else {
						break;
					}
				}


			}

			double fractionDuration = (double) duration.toMinutes() / (double) service.getTotalDuration().toMinutes();
			_duration = _duration.plus(duration);

			double fractionCost = service.getCost() / fractionDuration;
			_cost += fractionCost;

			StringBuffer serviceBuf = new StringBuffer();

			String costDesc = String.format(Locale.US, "%.2f", fractionCost);

			String serviceDescription = "Serviço #" + service.getId() + " @ " + costDesc;

			serviceBuf.append(serviceDescription);
			serviceBuf.append(stopsBuf);

			descriptionBuf.append(serviceBuf);

			i++;
		}

		_description = descriptionBuf.toString();

	}


	public void visit ( Service service ) {

		Collection<TrainStop> stops = service.getAllTrainStops();
		boolean isValidStart= false;
		LocalTime startTime;

		for ( TrainStop stop : stops ) {

			// checks if the service has the start station
			if ( !isValidStart && stop.getStation().getName().equals( _startStation ) ) {

				_startingServices.add( service );
				isValidStart = true;
				startTime = stop.getTime();
			}

			// if it has the start station, checks if it has the end station
			if ( isValidStart && stop.getStation().getName().equals( _endStation ) ) {

				if (_switchStation.size() == 1) {
					_switchStation.add( _endStation );
				}

				_services.add( service );
				break;
			}
		}

	}
	public void visit (TrainStop stop) {

	}
}