package mmt.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.Locale;

import java.time.LocalTime;
import java.time.Duration;

public class BuiltItinerary {

	/** The TrainCompany associated to this ItineraryBuilder */
	private TrainCompany _company;

	/** Name of the starting station of the itinerary */
	private String _startStation;

	/** Name of the ending station of the itinerary */
	private String _endStation;

	/** the itinerary's associated cost */
	private double _cost;

	/** String description of the itinerary */
	private String _description;

	/** Duration of the itinerary */
	private Duration _duration;

	/** Passenger Id */
	private int _id;

	/** Departure date for this itinerary */
	private String _departureDate;

	/** Services the Itinerary passes through */
	private ArrayList<Service> _services = new ArrayList<Service>();

	/** Stations where the passenger gets on or off */
	private ArrayList<String> _switchStation = new ArrayList<String>();

	/** Whether or not this starting service serves as valid start for an itinerary*/
	private boolean _isValid;

	// Used in the creation from the menu
	BuiltItinerary (boolean isSingular, Service startService, String startStation, String endStation,
	 TrainCompany company, String departureDate) {
		
		_company = company;
		_startStation = startStation;
		_endStation = endStation;
		_services.add(startService);
		_switchStation.add(_startStation);
		_isValid = false;
		_duration = Duration.ZERO;
		_departureDate = departureDate;


		if (isSingular) {
			_switchStation.add(_endStation);
			_isValid = true;
		} else {
			// Dijkstra
			return;
		}
		this.build();
	}

	// Only used in Parser
	BuiltItinerary (ArrayList<Service> services, ArrayList<String> switchStations, String departureDate, TrainCompany company, int passengerId) {
		
		_company = company;
		_services = services;
		_switchStation = switchStations;
		_isValid = true;
		_duration = Duration.ZERO;
		_departureDate = departureDate;
		_id = passengerId;
		this.build();

	}


	void build () {

		// Attribute creation for already found path

		StringBuffer descriptionBuf = new StringBuffer();
		int i = 0;

		// Starts prevTime as a generic time stamp

		LocalTime prevTime = LocalTime.MIDNIGHT;

		LocalTime time;
		Station station;
		String name;
		Duration duration;
		boolean firstService = true;

		for ( Service service : _services ) {

			duration = Duration.ZERO;
			Collection<TrainStop> startStops = service.getStartTrainStops();
			Collection<TrainStop> endStops = service.getEndTrainStops();
			StringBuffer stopsBuf = new StringBuffer();

			boolean hasStarted = false;
			boolean firstStation = false;
			int j = 0;


			for ( TrainStop stop : startStops ) {
				if ( stop.getStation().getName().equals( _switchStation.get(i) ) ) {

					time = stop.getTime();
					station = stop.getStation();
					name = station.getName();
					if ( !firstService ) {
						_duration = _duration.plus(Duration.between(prevTime, time));
					}
					prevTime = time;

					stopsBuf.append("\n" + stop.getTime().toString() + " " + name);
					if (j == 0) {
						firstStation = true;
					}
					break;
				}
				j++;
			}

			for ( TrainStop stop : endStops ) {
				if ( firstStation ) {
					hasStarted = true;
				}
				if ( !hasStarted && stop.getStation().getName().equals( _switchStation.get(i) ) ) {

					hasStarted = true;

				} else if (hasStarted) {

					time = stop.getTime();
					station = stop.getStation();
					name = station.getName();

					duration = duration.plus(Duration.between(prevTime, time));
					prevTime = time;

					stopsBuf.append("\n" + time.toString() + " " + name);

					// Checks if this is a station where the passenger gets off the service
					if ( stop.getStation().getName().equals( _switchStation.get(i + 1) ) ) {
						firstService = false;
						break;
					}
				}
				firstService = false;

			}

			double fractionDuration = ( double ) duration.toMinutes()/ ( double ) service.getTotalDuration().toMinutes();
			_duration = _duration.plus(duration);

			double fractionCost = service.getCost() * fractionDuration;
			_cost += fractionCost;

			StringBuffer serviceBuf = new StringBuffer();

			String costDesc = String.format(Locale.US, "%.2f", fractionCost);

			String serviceDescription = "\nServiço #" + service.getId() + " @ " + costDesc;

			serviceBuf.append( serviceDescription );
			serviceBuf.append( stopsBuf );
			descriptionBuf.append( serviceBuf );

			i++;
		}

		_description = descriptionBuf.toString();
	}


	boolean isValid() {
		return _isValid;
	}

	public String toString() {
		return _description;
	}

	String getDepartureDate() {
		return _departureDate;
	}

	double getCost() {
		return _cost;
	}

	Duration getDuration() {
		return _duration;
	}

	int getId() {
		return _id;
	}

}