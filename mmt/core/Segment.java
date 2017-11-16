package mmt.core;

import java.util.ArrayList;

import java.time.Duration;

public class Segment implements java.io.Serializable {

	/** The cost and duration of this segment. */
	private double _cost;
	private Duration _duration;

	/**
	 * Creates a segment that is associated with a start and end TrainStop.
	 *
	 * @param start the TrainStop (with time and station) where this segment starts.
	 * @param end the TrainStop (with time and station) where this segment ends.
	 */
	public Segment(double cost, Duration duration) {
		_cost = cost;
		_duration = duration;
	}

	/**
	 * @return segment duration.
	 */
	public final Duration getDuration() {
		return _duration;
	}

	/**
	 * @return segment cost.
	 */
	public final double getCost() {
		return _cost;
	}

}