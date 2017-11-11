package mmt.core.categories;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Class which manages the categories, updates the passenger's given category and keeps a list of categories.
 */

public class CategoryManager {

	/** List of available categories. */
	private List<Category> _categoryList = new ArrayList<Category>();

	/** 
	 * Constructor for Category Manager. Creates a list of the available categories.
	 */
	public CategoryManager() {
		_categoryList.add(new NormalCategory());
		_categoryList.add(new FrequentCategory());
		_categoryList.add(new SpecialCategory());
	}

	/**
	 * Returns the category corresponding to the value of the itineraries.
	 *
	 * @param value value of the last 10 itineraries.
	 * @return corresponding category.
	 */
	public Category getCategory (double value) {

		ListIterator<Category> iter = _categoryList.listIterator();

		Category category = null;

		while (iter.hasNext()) {
			category = iter.next();
			if (category.getMinimumValue() > value) {

				iter.previous();
				return iter.previous();
			}
		}
		return category;
	}

	/**
	 * Returns the name of the category corresponding to the value of the itineraries.
	 * 
	 * @param value value of the last 10 itineraries.
	 * @return corresponding category name.
	 */
	public String getCategoryName(double value) {

		return getCategory(value).getName();
	}

	/**
	 * Returns the discount attributed to the category corresponding to the value of the itineraries.
	 *
	 * @param value value of the last 10 itineraries.
	 * @return corresponding category discount.
	 */
	public double getCategoryDiscount (double value) {

		return getCategory(value).getDiscountPercentage();
	}

	/**
	 * Returns the minimum value associated with the category corresponding to the value of the itineraries. 
	 *
	 * @param value value of the last 10 itineraries.
	 * @return corresponding category minimum value.
	 */
	public double getCategoryMinimum (double value) {

		return getCategory(value).getMinimumValue(); 
	}

	/**
	 * Returns a String which identifies the category corresponding to the value of the itineraries. 
	 *
	 * @param value value of the last 10 itineraries.
	 * @return String identifier.
	 */
	public String getCategoryDescription (double value) {

		String name = getCategoryName(value);
		double minimum = getCategoryMinimum(value);
		double percentage = getCategoryDiscount(value);

		return "Categoria " + name + " | Valor Minimo de Entrada: " + minimum + " | Valor de Desconto: " + percentage;
	}
}