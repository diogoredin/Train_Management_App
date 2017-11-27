package mmt.core.exceptions;

/** Exception thrown when the requested service does not exist. */
public class NoSuchPassengerIdException extends Exception {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201709021324L;

	/** Service id. */
	private int _id;

	/**
	 * @param id the corresponding non-existent passenger id.
	 */
	public NoSuchPassengerIdException(int id) {
		_id = id;
	}

	/** @return id */
	public int getId() {
		return _id;
	}

}
