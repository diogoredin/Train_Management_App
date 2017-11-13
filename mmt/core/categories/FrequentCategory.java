package mmt.core.categories;

/**
 * Class describing the frequent passengers of the train company.
 * <p>
 * Passengers are awarded a discount of 15% if they have at least 250 combined cost in the last 10 itineraries.
 */
public class FrequentCategory extends Category implements java.io.Serializable {

	public FrequentCategory() {
		super("FREQUENTE", 250.0, 15.0);
	}
}