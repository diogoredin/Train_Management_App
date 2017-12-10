package mmt.core;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Class which manages the categories, updates the passenger's given category and keeps a list of categories.
 */

public class CategoryManager implements java.io.Serializable {
	/** Serial number for serialization. */
	private static final long serialVersionUID = 201708301015L;

	/** List of available categories. */
	private List<Category> _categoryList = new ArrayList<Category>();

	/** 
	 * Constructor for Category Manager. Creates a list of the available categories.
	 * <p>
	 * If new Categories are added, they should be in order of minimum value.
	 */
	CategoryManager() {
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
	Category getCategory (double value) {

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
}