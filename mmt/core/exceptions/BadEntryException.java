package mmt.core.exceptions;

/**
 * Exception for unknown import file entries.
 */
public class BadEntryException extends Exception {

	/** Class serial number. */
	private static final long serialVersionUID = 201409301048L;

	/** Bad bad entry specification. */
	private String _entrySpecification;

	/**
	 * @param entrySpecification the bad entry.
	 */
	public BadEntryException(String entrySpecification) {
		_entrySpecification = entrySpecification;
	}

	/**
	 * @param entrySpecification the bad entry.
	 * @param cause the exception corresponding to the cause.
	 */
	public BadEntryException(String entrySpecification, Exception cause) {
		super(cause);
		_entrySpecification = entrySpecification;
	}

	/**
	 * @return the bad entry specification.
	 */
	public String getEntrySpecification() {
		return _entrySpecification;
	}

}
