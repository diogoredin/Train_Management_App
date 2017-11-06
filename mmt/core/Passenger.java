package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mmt.core.categories.*;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.MissingFileAssociationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import mmt.core.exceptions.NonUniquePassengerNameException;

public class Passenger {

	/** The TrainCompany to which this passenger travels on. */
	private TrainCompany _trainCompany;

	/** The passenger's unique identifier. */
	protected int _id;

	/** The passenger's name. */
	protected String _name;

	/** The passenger's category. */
	private Category _category;

	/**
	 * Creates a passenger that isn't associated with any TrainCompany.
	 *
	 * @param id the passenger's unique identifier.
	 * @param name the passenger's name.
	 */
	public Passenger(int id, String name) {
		_id = id;
		_name = name;
	}

	/**
	 * Builds a passenger that is associated with a TrainCompany.
	 *
	 * @param trainCompany the train company the passenger travels on.
	 * @param id the passenger's unique identifier.
	 * @param name the passenger's name.
	 */
	public Passenger(TrainCompany trainCompany, int id, String name) {
		this (id, name);
		_trainCompany = trainCompany;
		_trainCompany.addPassenger (this);
	}

	public final int getId() {
		return _id;
	}

	public final String getName() {
		return _name;
	}

}