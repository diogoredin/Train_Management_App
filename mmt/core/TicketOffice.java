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

	public void reset() {
		_trainCompany.deletePassengers();
	}

	public void save(String filename) {
		//FIXME implement this function
	}

	public void load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
		//FIXME implement this function
	}

	public void importFile(String datafile) throws ImportFileException {
		NewParser parser = new NewParser(_trainCompany);
		parser.parseFile(datafile);
	}

	//FIXME complete and implement the itinerary search (and pre-commit store) method
	public void searchItineraries(int passengerId, String departureStation, String arrivalStation, String departureDate,
		String departureTime) {
		/* FIXME define thrown exceptions */
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
