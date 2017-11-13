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

	/** The requested id. */
	private Input<Integer> _id;

	/**
	 * @param receiver
	 */
	public DoShowPassengerById(TicketOffice receiver) {
		super(Label.SHOW_PASSENGER_BY_ID, receiver);

		String m = Message.requestPassengerId();
		_id = _form.addIntegerInput(m);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException {
		try {
			
			_form.parse();

			_display.addLine(_receiver.getTrainCompany().getPassengerDescription(_id.value()));
			_display.display();

		} catch (NoSuchPassengerIdException e) {
			throw new NoSuchPassengerException(e.getId());
		}
	}

}
