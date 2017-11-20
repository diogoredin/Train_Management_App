package mmt.core.categories;

/**
 * Class describing the most basic category. Passengers are not awarded any discount in this category.
 */
public class NormalCategory extends Category {
	
	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301017L;

	/**
	 * Constructor.
	 */
	public NormalCategory() {
		super("NORMAL", 0.0, 0.0);
	}
}