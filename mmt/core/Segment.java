package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;

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

	/** The TrainStop (with time and station) where this segment starts. */
	protected TrainStop _start;

	/** The TrainStop (with time and station) where this segment ends. */
	protected TrainStop _end;

	/**
	 * Creates a segment that is associated with a start and end TrainStop.
	 *
	 * @param start the TrainStop (with time and station) where this segment starts.
	 * @param end the TrainStop (with time and station) where this segment ends.
	 */
	public Segment(TrainStop start, TrainStop end) {
		_start = start;
		_end = end;
	}

	/**
	 * @return segment duration.
	 */
	public final Duration getDuration() {
		Duration duration = Duration.between(_start.getTime(), _end.getTime());
		return duration;
	}

	/**
	 * @return segment start data (station and timestamp)
	 */
	public final TrainStop getStart() {
		return _start;
	}

	/**
	 * @return segment end data (station and timestamp)
	 */
	public final TrainStop getEnd() {
		return _end;
	}

}