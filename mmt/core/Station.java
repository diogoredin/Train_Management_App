package mmt.core;
/**
 * Class describing a Station where a train stops.
 */
public class Station implements java.io.Serializable {

	/** Stores the name of the station. */
	private String _name;

	/**
	 * Creates a station with a name.
	 *
	 * @param name identifies the station.
	 */
	public Station(String name) {
		_name = name;
	}

	/** 
	 * @return the name of the station.
	 **/
	public final String getName() {
		return _name;
	}

}