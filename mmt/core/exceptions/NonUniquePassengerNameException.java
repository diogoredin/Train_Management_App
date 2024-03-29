package mmt.core.exceptions;

/** Exception thrown when a passenger name is invalid. */
public class NonUniquePassengerNameException extends Exception {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201709021324L;

	/** Passenger name. */
	private String _name;

	/**
	 * @param name the corresponding non-unique passenger name.
	 */
	public NonUniquePassengerNameException(String name) {
		_name = name;
	}

	/** @return name */
	public String getName() {
		return _name;
	}

}
