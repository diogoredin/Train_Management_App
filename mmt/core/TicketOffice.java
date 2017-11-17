package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mmt.core.NewParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import mmt.core.exceptions.ImportFileException;

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
	 * @return the associated TrainCompany.
	 */
	public TrainCompany getTrainCompany() {
		return _trainCompany;
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
	
	//FIXME add other functions if necessary
}
