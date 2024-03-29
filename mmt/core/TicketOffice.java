package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mmt.core.NewParser;

import java.util.Collection;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.IOException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.BadDateSpecificationException;

/**
 * Facade for handling persistence and other functions.
 */
public class TicketOffice {

	/** The object doing most of the actual work. */
	private TrainCompany _trainCompany;

	/** The name of the current file with data associated to this ticket office. */
	private String _fileName;

	/**
	 * Constructor.
	 */
	public TicketOffice() {
		_trainCompany = new TrainCompany();
		_fileName = "";
	}

	/**
	 * Resets a TrainCompany, deleting its associated Passengers and Itineraries,
	 * but not its associated Services.
	 */
	public void reset() {
		_trainCompany.deletePassengers();
		//_trainCompany.deleteItineraries();
		setFileName("");
	}

	/**
	 * Sets a new associated file name.
	 *
	 * @param fileName the new associated file's name.
	 */
	public void setFileName(String fileName) {
		_fileName = fileName;
	}

	/**
	 * Returns a String with the this TicketOffice's associated file name.
	 *
	 * @return the associated file's name.
	 */
	public String getFileName() {
		return _fileName;
	}

	/**
	 * Returns whether or not this TicketOffice has an associated file.
	 *
	 * @return if the TicketOffice has an associated file, return true; else, return false.
	 */
	public boolean hasAssociatedFile() {
		return _fileName != null && !_fileName.isEmpty();
	}

	/**
	 * Saves the associated TrainCompany's data to a file.
	 *
	 * @param fileName the name of the file to be saved.
	 * @throws IOException if errors occur in file writing.
	 */
	public void save(String fileName) throws IOException {
	
		/* Opens the given file */
		FileOutputStream fileOut = new FileOutputStream(fileName);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);

		/* Writes new content */
		out.writeObject(_trainCompany);

		/* Closes the pipe */
		out.close();
		fileOut.close();

	}

	/**
	 * Loads the new TrainCompany data from a file.
	 *
	 * @param fileName the name of the file from which the data will be loaded.
	 * @throws IOException if errors occur in file reading.
	 * @throws ClassNotFoundException if the file cannot be found.
	 */
	public void load(String fileName) throws IOException, ClassNotFoundException {

		/* Opens the given file */
		FileInputStream fileIn = new FileInputStream(fileName);
		ObjectInputStream in = new ObjectInputStream(fileIn);

		/* Replaces the TrainCompany */
		_trainCompany = (TrainCompany) in.readObject();

		/* Closes the pipe */
		in.close();
		fileIn.close();
		
	}

	/**
	 * Imports the default Services, Passengers and Itineraries from a file.
	 *
	 * @param fileName the name of the import file.
	 * @throws ImportFileException if the file cannot be properly read.
	 */
	public void importFile(String fileName) throws ImportFileException {

		/* Creates Parser */
		NewParser parser = new NewParser(_trainCompany);

		/* Parses the File */
		parser.parseFile(fileName);
	}
	
	/**
	 * Changes a given passenger name.
	 * 
	 * @param id of the passenger.
	 * @param newname the new name to give the passenger
	 * @throws NoSuchPassengerIdException if passenger id does not exist.
	 * @throws InvalidPassengerNameException if passenger name is null or an empty String.
	 */
	public void changePassengerName(int id, String newname) throws NoSuchPassengerIdException, InvalidPassengerNameException {
		_trainCompany.changePassengerName(id, newname);
	}

	/**
	 * Returns the next passenger's assigned id.
	 * 
	 * @return the next passenger's assigned id.
	 */
	public int getNextPassengerId() {
		return _trainCompany.getNextPassengerId();
	}

	/**
	 * Add passenger.
	 * 
	 * @param id the new passenger's id.
	 * @param name the new passenger's name.
	 * @throws InvalidPassengerNameException if passenger name is null or an empty String.
	 */
	public void addPassenger(int id, String name) throws InvalidPassengerNameException {
		Passenger p = new Passenger(id, name, _trainCompany);
		_trainCompany.addPassenger(p);
	}

	/**
	 * Returns a collection containing the passengers of the TrainCompany.
	 *
	 * @return the collection of passengers of this TrainCompany ordered by id.
	 */
	public Collection<Passenger> getPassengers() {
		return _trainCompany.getPassengers();
	}

	/**
	 * Returns a passenger's String description, given it's id.
	 *
	 * @param id the passenger's id.
	 * @return passenger String description.
	 * @throws NoSuchPassengerIdException if passenger id does not exist.
	 */
	public String getPassengerDescription(int id) throws NoSuchPassengerIdException {
		return _trainCompany.getPassengerDescription(id);
	}

	/**
	 * Returns a collection containing the services of the TrainCompany.
	 *
	 * @return the collection of services of this TrainCompany ordered by id.
	 */
	public Collection<Service> getServices() {
		return _trainCompany.getServices();
	}

	/**
	 * Get a service given its identifier.
	 * 
	 * @param id the service's identifier.
	 * @return the service with the given identifier.
	 * @throws NoSuchServiceIdException if the service id does not exist.
	 */
	public Service getService(int id) throws NoSuchServiceIdException {
		return _trainCompany.getService(id);
	}

	/**
	 * Looks up a service with a given start station name.
	 *
	 * @param search the station name to look for.
	 * @return the service that has the search start station.
	 * @throws NoSuchStationNameException if station name does not exist.
	 */
	public Collection<Service> searchServiceWithStartStation( String search ) throws NoSuchStationNameException { 

		/* Service we are looking for */
		return _trainCompany.searchServiceWithStartStation(search);
	}

	/**
	 * Looks up a service with a given end station name.
	 *
	 * @param search the station name to look for.
	 * @return the service that has the search end station.
	 * @throws NoSuchStationNameException if station name does not exist.
	 */
	public Collection<Service> searchServiceWithEndStation( String search ) throws NoSuchStationNameException {

		/* Service we are looking for */
		return _trainCompany.searchServiceWithEndStation(search);

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
	public ArrayList<Itinerary> searchItineraries(int passengerId, String departureStation, String arrivalStation, String departureDate,
		String departureTime) throws NoSuchPassengerIdException, BadTimeSpecificationException, NoSuchStationNameException, BadDateSpecificationException {
		return _trainCompany.searchItineraries(passengerId, departureStation, arrivalStation, departureDate, departureTime);
	}

	/**
	 * Adds an itinerary to a passenger.
	 *
	 * @param id the id of the passenger who bought the itinerary. 
	 * @param itinerary the chosen itinerary.
	 * @throws NoSuchPassengerIdException if the passenger id does not exist.
	 */
	public void commitItinerary(int passengerId, Itinerary itinerary) throws NoSuchPassengerIdException {
		_trainCompany.commitItinerary(passengerId, itinerary);
	}

	/**
	 * Displays the itineraries bought by a given passenger.
	 *
	 * @param id passenger id to whom the itinaries belong.
	 * @return the itineraries as a string.
	 * @throws NoSuchPassengerIdException if the passenger id does not exist.
	 */
	public String showPassengerItineraries(int id) throws NoSuchPassengerIdException {
		return _trainCompany.showPassengerItineraries(id);
	}

	/**
	 * Let's us know if a given passenger has purchased itineraries.
	 *
	 * @param id the id of the passenger.
	 * @return true if the passenger has itineraries, false otherwise.
	 * @throws NoSuchPassengerIdException if the passenger id does not exist.
	 */
	public boolean passengerHasItineraries(int id) throws NoSuchPassengerIdException {
		return _trainCompany.passengerHasItineraries(id);
	}

	/**
	 * Looks up all itineraries in the TrainCompany.
	 *
	 * @return all itineraries in the TrainCompany.
	 */
	public String showAllItineraries() {
		return _trainCompany.showAllItineraries();
	}

	/**
	 * Updates all itinerary id's by one.
	 */
	public void updateListId(ArrayList<Itinerary> itineraryOptions) {
		_trainCompany.updateListId(itineraryOptions);
	}
}
