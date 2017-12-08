package mmt.core;

public interface Visitable {
	void accept(Visitor visitor);
}