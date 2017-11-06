package mmt.core.categories;

/**
 * Abstract Class Category used as a base for other implemented categories.
 */
public abstract class Category {

	/** The category's name. */
	private String _name;

	/** The category's minimum value for a passenger to be able to enter it. */
	private double _minimumValue;

	/** The discount percentage attributed to passengers of a given category. */
	private double _discountPercentage;

	/** 
	 * Constructor.
	 *
	 * @param name the new category's name.
	 * @param minimumValue the new category's base value.
	 * @param discontPercentage the new category's attributed discount.
	 */
	public Category(String name, double minimumValue, double discountPercentage) {
		_name = name;
		_minimumValue = minimumValue;
		_discountPercentage = discountPercentage;
	}

	public String getName () {
		return _name;
	}

	public double getMinimumValue () {
		return _minimumValue;
	}

	public double getDiscountPercentage () {
		return _discountPercentage;
	}
}