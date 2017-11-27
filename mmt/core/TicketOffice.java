package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mmt.core.NewParser;

import java.util.Collection;

import java.io.FileNotFoundException;
import java.io.IOException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;

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
	 * @return the associated file's name.
	 */
	public String getFileName() {
		return _fileName;
	}

	/**
	 * @return if the TicketOffice has an associated file, return true; else, return false.
	 */
	public boolean hasAssociatedFile() {
		return _fileName != null && !_fileName.isEmpty();
	}

	/**
	 * Saves the associated TrainCompany's data to a file.
	 *
	 * @param fileName the name of the file to be saved.
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
	 * @param newname the new name to give the passenger (non-null, must not be an empty String).
	 */
	public void changePassengerName(int id, String newname) throws NoSuchPassengerIdException, InvalidPassengerNameException {
		_trainCompany.changePassengerName(id, newname);
	}

	/**
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
	 */
	public void addPassenger(int id, String name) throws InvalidPassengerNameException {
		Passenger p = new Passenger(id, name);
		_trainCompany.addPassenger(p);
	}

	/**
	 * @return the collection of passengers of this trainCompany ordered by id.
	 */
	public Collection<Passenger> getPassengers() {
		return _trainCompany.getPassengers();
	}

	/**
	 * Returns a passenger's String description, given it's id.
	 *
	 * @param id the passenger's id.
	 * @return passenger String description.
	 */
	public String getPassengerDescription(int id) throws NoSuchPassengerIdException {
		return _trainCompany.getPassengerDescription(id);
	}

	/**
	 * @return the collection of services of this trainCompany ordered by id.
	 */
	public Collection<Service> getServices() {
		return _trainCompany.getServices();
	}

	/**
	 * Get a service given its identifier.
	 * 
	 * @param id the service's identifier.
	 * @return the service with the given identifier.
	 */
	public Service getService(int id) throws NoSuchServiceIdException {
		return _trainCompany.getService(id);
	}

	/**
	 * Looks up a service with a given start station name.
	 *
	 * @param search the station name to look for.
	 * @return the service that has the search start station.
	 */
	public String searchServiceWithStartStation( String search ) throws NoSuchStationNameException { 

		/* Service we are looking for */
		return _trainCompany.searchServiceWithStartStation(search);
	}

	/**
	 * Looks up a service with a given end station name.
	 *
	 * @param search the station name to look for.
	 * @return the service that has the search end station.
	 */
	public String searchServiceWithEndStation( String search ) throws NoSuchStationNameException {

		/* Service we are looking for */
		return _trainCompany.searchServiceWithEndStation(search);

	}

	//FIXME complete and implement the itinerary search (and pre-commit store) method
	public void searchItineraries(int passengerId, String departureStation, String arrivalStation, String departureDate,
		String departureTime) {
		//FIXME implement method
	}

	//FIXME complete and implement the itinerary commit method
	public void commitItinerary(int passengerId, int itineraryNumber) {
		/*FIXME define thrown exceptions */
		//FIXME implement method
	}
}
