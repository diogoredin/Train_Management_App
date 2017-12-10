package mmt.app.itineraries;

import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.app.exceptions.NoSuchPassengerException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.Display;

/**
 * §3.4.2. Show all itineraries (for a specific passenger).
 */
public class DoShowPassengerItineraries extends Command<TicketOffice> {

	Input<Integer> _id;

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public DoShowPassengerItineraries(TicketOffice receiver) {
		super(Label.SHOW_PASSENGER_ITINERARIES, receiver);
		_id = _form.addIntegerInput(Message.requestPassengerId());
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException {

		try {
			_form.parse();
		if ( _receiver.passengerHasItineraries(_id.value()) ) {
			_display.addLine( _receiver.showPassengerItineraries(_id.value()) );
		} else {
			_display.addLine(Message.noItineraries(_id.value()));
		}
		_display.display();

		} catch (NoSuchPassengerIdException e) {
			throw new NoSuchPassengerException(e.getId());
		}
	}

}
