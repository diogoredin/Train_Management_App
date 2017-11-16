package mmt.app.passenger;

import java.util.Collection;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;

import mmt.core.Passenger;
import mmt.core.TicketOffice;

/**
 * §3.3.1. Show all passengers.
 */
public class DoShowAllPassengers extends Command<TicketOffice> {

	/**
	 * @param receiver
	 */
	public DoShowAllPassengers(TicketOffice receiver) {
		super(Label.SHOW_ALL_PASSENGERS, receiver);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() {
		Collection<Passenger> passengers = _receiver.getTrainCompany().getPassengers();

		passengers.forEach((Passenger passenger)-> {
			_display.addLine(passenger.toString());
		});

		_display.display();

	}

}
