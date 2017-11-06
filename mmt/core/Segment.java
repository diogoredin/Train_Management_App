package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.time.Instant;
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

public class Segment {

    protected TrainStop _start;
    protected TrainStop _end;
    protected double _cost;

	public Segment(TrainStop start, TrainStop end, double cost) {
        _start = start;
        _end = end;
        _cost = cost;
	}

	public final String showSegment() {
		return "{ " + _start.getStation().getName() + "->" +  _end.getStation().getName() + "}";
	}

    public final double getFractionCost() {
		return _cost;
	}

     public final void getTotalDuration() {
		//FIXME should be difference between start and end
	}

}