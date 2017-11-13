package mmt.app.passenger;

import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.app.exceptions.NoSuchPassengerException;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.Display;

/**
 * §3.3.2. Show specific passenger.
 */
public class DoShowPassengerById extends Command<TicketOffice> {

	/**
	 * @param receiver
	 */
	public DoShowPassengerById(TicketOffice receiver) {
		super(Label.SHOW_PASSENGER_BY_ID, receiver);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException {
		try {
			String m = Message.requestPassengerId();
			Input<Integer> id = _form.addIntegerInput(m);
			
			_form.parse();
			_form.clear();

			_display.addLine(_receiver.getTrainCompany().getPassengerDescription(id.value()));
			_display.display();

		} catch (NoSuchPassengerIdException e) {
			throw new NoSuchPassengerException(e.getId());
		}
	}

}
