package mmt.core.categories;
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
	public Category(String name, double minimumValue, double discountPercentage) {
		_name = name;
		_minimumValue = minimumValue;
		_discountPercentage = discountPercentage;
	}

	/**
	 * @return the category's name.
	 */
	public String getName () {
		return _name;
	}

	/**
	 * @return the category's minimum value.
	 */
	public double getMinimumValue () {
		return _minimumValue;
	}

	/**
	 * @return the category's discount percentage.
	 */
	public double getDiscountPercentage () {
		return _discountPercentage;
	}
}