package mmt.app.passenger;

import pt.tecnico.po.ui.ValidityPredicate;
import mmt.core.TicketOffice;

/**
 * Class that verifies if a command may appear in the main menu. 
 */

public class PassengerCommandValidity extends ValidityPredicate<TicketOffice> {

	/**
	 * Constructor.
	 *
	 * @param editor the target TicketOffice which will be used to check the command visibility.
	 */
	public PassengerCommandValidity (TicketOffice ticketOffice) {
		super(ticketOffice);
	}

	/**
	 * Checks whether or not a command is visible and valid.
	 *
	 * @return if the command is visible, returns true, else returns false.
	 */
	public boolean isValid() {
		return _receiver.getTrainCompany().numberPassengers() > 0;
	}
}