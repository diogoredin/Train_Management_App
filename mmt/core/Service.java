package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.MissingFileAssociationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import mmt.core.exceptions.NonUniquePassengerNameException;

public class Service {

	protected int _id;
    protected Station _start;
    protected Station _end;

    protected ArrayList<Segment> _segments;

	public Service(int id, Station start, Station end) {
		_id = id;
        _start = start;
        _end = end;
	}

	public final String showService() {

        String serviceTxt = "Service ( " + _id + ") : ";

        for (Segment segment : _segments ) {
			serviceTxt = serviceTxt + segment.showSegment();
		}

		return serviceTxt;
	}

    public final Station getStartStation() {
		return _start;
	}

    public final Station getEndStation() {
		return _end;
	}

    public final double getTotalCost() {

        double totalCost = 0;

        for (Segment segment : _segments ) {
			totalCost = totalCost + segment.getFractionCost();
		}

		return totalCost;
	}

    /* public final Double getTotalDuration() {

        Double totalDuration = 0;

        for (Segement segment : this._segments ) {
			totalDuration = totalDuration + segment.getTotalDuration();
		}

		return totalDuration;
	} */

    public final int requestServiceID() {
		return _id;
	}

}