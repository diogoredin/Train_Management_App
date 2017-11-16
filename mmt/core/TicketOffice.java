package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mmt.core.NewParser;
import mmt.core.NewParser;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.MissingFileAssociationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import mmt.core.exceptions.NonUniquePassengerNameException;

/**
 * Façade for handling persistence and other functions.
 */
public class TicketOffice {

	/** The object doing most of the actual work. */
	private TrainCompany _trainCompany;

	public TicketOffice(TrainCompany trainCompany) {
		_trainCompany = trainCompany;
	}

	public TrainCompany getTrainCompany() {
		return _trainCompany;
	}

	public void setTrainCompany(TrainCompany traincompany) {
		_trainCompany = traincompany;
	}

	public void reset() {
		_trainCompany.deletePassengers();
		//_trainCompany.deleteItineraries();
	}

	public void save(String filename) throws IOException {
	
		/* Opens the given file */
		FileOutputStream fileOut = new FileOutputStream(filename);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);

		/* Writes new content */
		out.writeObject(_trainCompany);

		/* Closes the pipe */
		out.close();
		fileOut.close();

	}

	public void load(String filename) throws IOException, ClassNotFoundException {

		/* Opens the given file */
		FileInputStream fileIn = new FileInputStream(filename);
		ObjectInputStream in = new ObjectInputStream(fileIn);

		/* Replaces the TrainCompany */
		_trainCompany = (TrainCompany) in.readObject();

		/* Closes the pipe */
		in.close();
		fileIn.close();
		
	}

	public void importFile(String datafile) throws ImportFileException {

		/* Creates Parser */
		NewParser parser = new NewParser(_trainCompany);

		/* Parses the File */
		parser.parseFile(datafile);
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

	//FIXME add methods for passenger registration and passenger name update
	//FIXME add other functions if necessary
}
