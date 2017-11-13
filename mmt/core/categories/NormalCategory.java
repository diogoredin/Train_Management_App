package mmt.core.categories;

/**
 * Class describing the most basic category. Passengers are not awarded any discount in this category.
 */
public class NormalCategory extends Category implements java.io.Serializable {

	public NormalCategory() {
		super("NORMAL", 0.0, 0.0);
	}
}