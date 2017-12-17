package mmt.core;

import java.util.ArrayList;
import java.time.Duration;
import java.util.Locale;
import java.time.LocalTime;
import java.util.Date;

import java.text.SimpleDateFormat;

import java.text.ParseException;

import mmt.core.exceptions.NoSuchServiceIdException;

/**
 * Class describing the itineraries created by the Passengers, representing "trips" between train stations.
 */

public class Itinerary implements java.io.Serializable, Comparable<Itinerary> {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301020L;

	/** the itinerary's associated id */
	private int _id;

	/** the itinerary's passenger's id */
	private int _passengerId;

	/** The itinerary's Departure Date. */
	private Date _departureDate;

	/** the trainstops that compose the itinerary */
	private ArrayList<TrainStop> _trainStops = new ArrayList<TrainStop>();

	/**
	 * Creates an itinerary that is associated with an id and cost.
	 *
	 * @param id the new itinerary's unique identifier.
	 * @param cost the new itinerary's cost.
	 */
	Itinerary(Date departureDate, ArrayList<TrainStop> trainStops) {
		_id = 0;
		_passengerId = 0;
		_departureDate = departureDate;
		_trainStops = trainStops;
	}

	/** 
	 * Returns the itinerary's unique identifier.
	 *
	 * @return the itinerary's unique identifier.
	 */
	int getId() {
		return _id;
	}

	/**
	 * Update the ID after it has been commited. Prior to commit the id is the option order, afterwards
	 * it's related to the number of itineraries bought by the passenger.
	 *
	 * @param id the new id give the itinerary.
	 */
	void updateId(int id) {
		_id = id;
	}

	/**
	 * Updates the passenger that bought this itinerary.
	 *
	 */
	void setPassengerId(int id) {
		_passengerId = id;
	}

	/**
	 * Returns the id of the passenger who bought this itinerary.
	 *
	 * @return the id of of the passenger who bought this itinerary.
	 */
	int getPassengerId() {
		return _passengerId;
	}

	/**
	 * Returns the itinerary's first service.
	 *
	 * @return the itinerary's first service.
	 */
	Service getDepartureService() {
		return _trainStops.get(0).getService();
	}

	/**
	 * Returns the itinerary's cost.
	 *
	 * @return the itinerary's cost.
	 */
	double getCost() {

		/* Holds the Service ID */
		int id = 0;

		/* The cost to be returned */
		double cost = 0;

		/* Iterate over all TrainStops */
		for ( TrainStop trainstop : _trainStops ) {

			/* Get the Service of the TrainStop */
			Service service = trainstop.getService();

			/* Checks if we need to add an header */
			if ( id != service.getId() ) {
				cost = cost + getPartialCost(service);
			}

			/* Update the Service ID */
			id = service.getId();

		}

		return cost;
	}

	/** 
	 * Returns the partial cost of an itinerary's service.
	 *
	 * @return the partial cost of the service.
	 */
	double getPartialCost( Service service ) {

		/* Time and Duration */
		LocalTime previous = LocalTime.MIDNIGHT;
		Duration duration = Duration.ZERO;

		/* Counts the number of TrainStops on this Service */
		int count = 0;

		/* Iterate over all TrainStops */
		for ( TrainStop trainstop : _trainStops ) {

			/* Count time between TrainStops of this service */
			if ( (service.getId() == trainstop.getService().getId()) && (count != 0 )) {
				duration = duration.plus(Duration.between(previous, trainstop.getTime()));
				previous = trainstop.getTime();
				count++;
			}

			/* First one so we don't have any duration yet */
			else if ( (service.getId() == trainstop.getService().getId()) && (count == 0) ) {
				previous = trainstop.getTime();
				count++;
			}

		}

		/* Calculate the fraction of the service to be paid */
		double fractionDuration = (double) duration.toMinutes() / (double) service.getTotalDuration().toMinutes();

		/* Calculated cost */
		double calculatedCost = service.getCost() * fractionDuration;

		/* Return the calculated cost */
		return calculatedCost;
	}

	/** 
	 * Returns the itinerary's duration.
	 *
	 * @return the itinerary's duration.
	 */
	Duration getDuration() {

		/* Duration */
		Duration duration = Duration.between( _trainStops.get(0).getTime(), _trainStops.get( _trainStops.size()-1 ).getTime() );
		return duration;
	}

	/** 
	 * Allows an itinerary to be shown.
	 *
	 * @return a string with all the itinerary details.
	 */
	public String toString() {

		/* Holds the Result */
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(_departureDate);
		String itineraryCost = String.format(Locale.US, "%.2f", getCost());
		StringBuffer resultBuf = new StringBuffer();
		resultBuf.append("\nItinerário " + _id + " para " + date + " @ " + itineraryCost);

		/* Holds the Service ID */
		int id = 0;

		/* Iterate over all TrainStops */
		for ( TrainStop trainstop : _trainStops ) {
			/* Get the Service of the TrainStop */
			Service service = trainstop.getService();

			/* Checks if we need to add a header */
			if ( id != service.getId() ) {
				String serviceCost = String.format(Locale.US, "%.2f", getPartialCost(service));
				resultBuf.append("\nServiço #" + service.getId() + " @ " + serviceCost);
			}

			/* Update the Service ID */
			id = service.getId();

			/* Appends segments of the Itinerary */
			resultBuf.append("\n" + trainstop.getTime().toString() + " " + trainstop.getStation().getName());

		}



		return resultBuf.toString();
	}

	Date getDepartureDate() {
		return _departureDate;
	}

	LocalTime getDepartureTime() {
		return _trainStops.get(0).getTime();
	}

	LocalTime getArrivalTime() {
		return _trainStops.get(_trainStops.size() - 1).getTime();
	}

	public int compareTo(Itinerary itinerary) {

		/* Checks the departure date */
		if (this.getDepartureDate().equals(itinerary.getDepartureDate())) {

			/* Checks the departure time */
			if (this.getDepartureTime().equals(itinerary.getDepartureTime())) {

				/* Checks the arrival time */
				if (this.getArrivalTime().equals(itinerary.getArrivalTime())) {

					/* Checks the duration */
					if(this.getDuration().equals(itinerary.getDuration())) {

						/* Finally, checks the cost */
						if (this.getCost() > itinerary.getCost()) return 1;
						else if (this.getCost() < itinerary.getCost()) return -1;
						else return 0;

					} else {
						return this.getDuration().compareTo(itinerary.getDuration());
					}

				} else {
					return this.getArrivalTime().compareTo(itinerary.getArrivalTime());
				}

			} else {
				return this.getDepartureTime().compareTo(itinerary.getDepartureTime());
			}

		} else {
			return this.getDepartureDate().compareTo(itinerary.getDepartureDate());
		}

	}

}