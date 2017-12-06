package mmt.core;
/**
 * Generic Category class which will define specific categories.
 */
public abstract class Category implements java.io.Serializable {

	/** The category's name. */
	private String _name;

	/** The minimum value spent for a passenger to belong to a category. */
	private double _minimumValue;

	/** The discount percentage attributed to a costumer of the category. */
	private double _discountPercentage;

	/** 
	 * Constructor.
	 *
	 * @param name The new category's name.
	 * @param minimumValue The minimum value for a passenger to belong to the new category.
	 * @param discountPercentage The discount attributed to a passenger belonging to a new category.
	 */
	Category(String name, double minimumValue, double discountPercentage) {
		_name = name;
		_minimumValue = minimumValue;
		_discountPercentage = discountPercentage;
	}

	/**
	 * Returns the category's name.
	 *
	 * @return the category's name.
	 */
	String getName () {
		return _name;
	}

	/**
	 * Returns the category's associated minimum value.
	 * 
	 * @return the category's minimum value.
	 */
	double getMinimumValue () {
		return _minimumValue;
	}

	/**
	 * Returns the category's associated discount percentage.
	 *
	 * @return the category's discount percentage.
	 */
	double getDiscountPercentage () {
		return _discountPercentage;
	}
}