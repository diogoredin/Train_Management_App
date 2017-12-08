package mmt.core;

import java.time.LocalTime;

/**
 * Class describing a TrainStop, which is an event when a train stops at a certain time at a certain station.
 */
public class TrainStop implements java.io.Serializable, Visitable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301014L;

	/** Stores the station of this TrainStop event. */
	private Station _station;

	/** Stores the instant of this TrainStop event. */
	private LocalTime _time;

	/** Stores the segment to which it belongs. */
	private Segment _segment;

	/**
	 * Constructor.
	 *
	 * @param station the station where the train stops.
	 * @param time the time when the train arrives.
	 * @param segment the segment to which the trainstop belongs.
	 */
	TrainStop(Station station, LocalTime time, Segment segment) {
		_station = station;
		_time = time;
		_segment = segment;
	}

	/** 
	 * Returns the Station of this TrainStop.
	 *
	 * @return the station of this TrainStop.
	 */
	Station getStation() {
		return _station;
	}

	/** 
	 * Returns the time of this TrainStop.
	 *
	 * @return the time of this TrainStop event.
	 */
	LocalTime getTime() {
		return _time;
	}

	/** 
	 * Returns the segment of this TrainStop.
	 *
	 * @return the segment of this TrainStop event. 
	 */
	Segment getSegment() {
		return _segment;
	}

	public void accept(Visitor visitor) {}

}