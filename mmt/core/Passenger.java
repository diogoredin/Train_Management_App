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

	/** The passenger's last 10 Itinerary values (FIXME) */
	private double _lastValues = 0;

	/**
	 * Creates a passenger that isn't associated with any TrainCompany.
	 *
	 * @param id the passenger's unique identifier.
	 * @param name the passenger's name.
	 */
	public Passenger(String name) {
		_name = name;
	}

	/**
	 * Builds a passenger that is associated with a TrainCompany.
	 *
	 * @param trainCompany the train company the passenger travels on.
	 * @param id the passenger's unique identifier.
	 * @param name the passenger's name.
	 */
	public Passenger(TrainCompany trainCompany, String name) {
		this (name);
		_trainCompany = trainCompany;
		_id = _trainCompany.addPassenger (this);
		updateCategory(_lastValues);
	}

	public final int getId() {
		return _id;
	}

	public final String getName() {
		return _name;
	}

	public void updateCategory(double value) {
		_category = _trainCompany.updateCategory(value);
	}

	public String toString() {
		int id = getId();
		String name = getName();
		String catName = _category.getName();
		double values = getLastValues();
		String time = "0:00";

		return "" + id + "|" + name +"|"+ catName+"|"+ values +"|"+ time;
	}

	public double getLastValues() {
		return _lastValues;
	}

	public void setName(String newname) {
		_name = newname;
	}

}