package mmt.core;

/**
 * Class describing the frequent passengers of the train company.
 *
 * Passengers are awarded a discount of 15% if they have at least 250 combined cost in the last 10 itineraries.
 */
public class FrequentCategory extends Category {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301018L;

	/**
	 * Constructor.
	 */
	FrequentCategory() {
		super("FREQUENTE", 250.0, 15.0);
	}
}