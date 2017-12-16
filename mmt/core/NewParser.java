package mmt.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;
import java.util.Date;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Arrays;
import java.util.Collections;

import java.text.SimpleDateFormat;

import java.io.IOException;
import java.text.ParseException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchPassengerIdException;

public class NewParser {

	/** The train company associated with the parser. */
	private TrainCompany _trainCompany;

	/** The built itineraries to be sorted. */
	private ArrayList<Itinerary> _built = new ArrayList<Itinerary>();

	/**
	 * Creates a parser which has associated a train company.
	 *
	 * @param trainCompany associated with the parser.
	 */
	public NewParser(TrainCompany trainCompany) {
		_trainCompany = trainCompany;
	}

	/**
	 * Parses a given file and adds the data to our app.
	 *
	 * @param fileName a file with data.
	 * @throws ImportFileException if errors occur in file reading.
	 */
	public void parseFile(String fileName) throws ImportFileException {

		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;

			while ((line = reader.readLine()) != null) {
				parseLine(line);
			}
		} catch (IOException ioe) {
			throw new ImportFileException(ioe);
		}
		try {
			/* Adds the parsed itineraries to their respective passengers, if there are any */
			Collections.sort(_built);
			for (Itinerary itinerary : _built) {
				_trainCompany.getPassenger(itinerary.getPassengerId()).addItinerary(itinerary);
			}
			
		} catch (NoSuchPassengerIdException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Parses a given line of a data file.
	 *
	 * @param line the line to parse.
	 * @throws ImportFileException if errors occur in file reading.
	 */
	private void parseLine(String line) throws ImportFileException {
		String[] components = line.split("\\|");

		switch (components[0]) {
			case "PASSENGER":
				parsePassenger(components);
				break;

			case "SERVICE":
				parseService(components);
				break;

			case "ITINERARY":
				parseItinerary(components);
				break;

		 default:
			throw new ImportFileException("invalid type of line: " + components[0]);
		}
	}

	/**
	 * Parses a passenger, i.e. grabs its properties and adds it to the train company.
	 *
	 * @param components the properties required for the passenger.
	 * @throws ImportFileException if errors occur in file reading.
	 */
	private void parsePassenger(String[] components) throws ImportFileException {
		if (components.length != 2) {
			throw new ImportFileException("invalid number of arguments in passenger line: " + components.length);
		}

		/* Registers the passenger */
		try {

			/* Grabs the full name of the passenger */
			String name = "";
			for (int i = 1; i < components.length; i++) {
				name = name + components[i];
			}

			/* Creates a new passenger for the Train Company */
			int id = _trainCompany.getNextPassengerId();
			Passenger p = new Passenger(id, name, _trainCompany);
			_trainCompany.addPassenger(p);

		} catch (InvalidPassengerNameException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Parses a service, i.e. grabs its properties and adds it to the app.
	 *
	 * @param components the properties required for the service.
	 */
	private void parseService(String[] components) {

		/* Service properties */
		double cost = Double.parseDouble(components[2]);
		int serviceId = Integer.parseInt(components[1]);

		/* Creates the Service and adds it to Train Company */
		Service service = new Service(serviceId, cost);

		/* Calculate cost of each segment */
		int totalSegments = ( components.length - 3 ) / 2;
		double segmentCost = cost / totalSegments;

		/* Adds the segments of this Service */
		for (int i = 3; i+2 < components.length; i += 2) {

			/* Segment Duration */
			String startTime = components[i];
			LocalTime stime = LocalTime.parse(startTime);

			String endTime = components[i + 2];
			LocalTime etime = LocalTime.parse(endTime);

			Duration duration = Duration.between(stime, etime);

			/* Segment Station Names */
			String startStationName = components[i + 1];
			String endStationName = components[i + 3];

			/* Segment Train Stations */
			Station startStation = new Station(startStationName);
			Station endStation = new Station(endStationName);

			/* Builds Segment */
			Segment segment = new Segment(segmentCost, duration);

			/* Builds Train Stops */
			TrainStop start = new TrainStop(startStation, stime, segment, service);
			TrainStop end = new TrainStop(endStation, etime, segment, service);

			/* Adds Next Train Stop */
			start.addNextTrainStop(end);

			/* Adds train stops to the service */
			service.addStart( start );
			service.addEnd( end );

			/* Adds Train Stops to the TrainCompany */
			_trainCompany.addTrainStop( start );
			if ( i+1 == components.length ) { _trainCompany.addTrainStop( end ); }

			/* Adds start train station */
			if ( !_trainCompany.checkStation(startStationName) ) {
				_trainCompany.addStation(startStationName);
			}

			/* Adds end train station */
			if ( !_trainCompany.checkStation(endStationName) ) {
				_trainCompany.addStation(endStationName);
			}
	
		}

		/* Adds the service to the train company */
		_trainCompany.addService(serviceId, service);
	}

	/**
	 * Parses an itinerary, i.e. grabs its properties and adds it to the app.
	 *
	 * @param components the properties required for the itinerary.
	 * @throws ImportFileException if errors occur in file reading.
	 */
	private void parseItinerary(String[] components) throws ImportFileException {
		try {
			if (components.length < 4)
				throw new ImportFileException("Invalid number of elements in itinerary line: " + components.length);

			ItineraryBuilder builder = new ItineraryBuilder(_trainCompany);
			int passengerId = Integer.parseInt(components[1]);
			String date = components[2];

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
			Date departureDate = format.parse(date);

			// criar um itinerário com data indicada
			ArrayList<Service> services = new ArrayList<Service>();
			ArrayList<String> stations = new ArrayList<String>();
			ArrayList<Itinerary> build = new ArrayList<Itinerary>();

			for (int i = 3; i < components.length; i++) {

				String segmentDescription[] = components[i].split("/");

				int serviceId = Integer.parseInt(segmentDescription[0]);
				Service service = _trainCompany.getService(serviceId);
				services.add(service);
				if (i == 3) {
					String departureTrainStop = segmentDescription[1];
					stations.add(departureTrainStop);
				}

				String arrivalTrainStop = segmentDescription[2];
				stations.add(arrivalTrainStop);

			}

			_built.add(builder.buildItinerary(departureDate, services, stations, passengerId));
			

		} catch (NoSuchServiceIdException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}