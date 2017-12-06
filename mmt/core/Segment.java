package mmt.core;

import java.time.Duration;

/**
 * A Segment is a class which keeps the cost and duration of a trip between
 * two TrainStops.
 */
public class Segment implements java.io.Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301019L;

	/** The cost and duration of this segment. */
	private double _cost;
	private Duration _duration;

	/**
	 * Creates a segment.
	 *
	 * @param cost the segment's cost.
	 * @param duration the segment's duration.
	 */
	Segment(double cost, Duration duration) {
		_cost = cost;
		_duration = duration;
	}

	/**
	 * Returns the segment's duration.
	 * 
	 * @return the segment duration.
	 */
	Duration getDuration() {
		return _duration;
	}

	/**
	 * Returns the segment's cost.
	 *
	 * @return the segment cost.
	 */
	double getCost() {
		return _cost;
	}

}