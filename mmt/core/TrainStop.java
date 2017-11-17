package mmt.core;

import java.time.LocalTime;

/**
 * Class describing a TrainStop, which is an event when a train stops at a certain time at a certain station.
 */
public class TrainStop implements java.io.Serializable {

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
	public TrainStop(Station station, LocalTime time, Segment segment) {
		_station = station;
		_time = time;
		_segment = segment;
	}

	/** 
	 * @return the station of this TrainStop.
	 */
	public final Station getStation() {
		return _station;
	}

	/** 
	 * @return the time of this TrainStop event.
	 */
	public final LocalTime getTime() {
		return _time;
	}

	/** 
	 * @return the segment of this TrainStop event. 
	 */
	public final Segment getSegment() {
		return _segment;
	}

}