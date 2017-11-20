package mmt.core.categories;

/**
 * Class describing the special passengers of the train company.
 * <p>
 * Passengers are awarded a discount of 50% if they have at least 2500 combined cost in the last 10 itineraries.
 */
public class SpecialCategory extends Category {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301016L;
	/**
	 * Constructor.
	 */
	public SpecialCategory() {
		super("ESPECIAL", 2500.0, 50.0);
	}
}