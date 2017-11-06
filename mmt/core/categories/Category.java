package mmt.core;

public abstract class Category {

	private String _name;

	private double _minimumValue;

	private double _discountPercentage;

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