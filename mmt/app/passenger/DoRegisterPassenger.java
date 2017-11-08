package mmt.app.passenger;

import mmt.core.Passenger;
import mmt.core.TicketOffice;

import mmt.app.exceptions.BadPassengerNameException;
import mmt.app.exceptions.DuplicatePassengerNameException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NonUniquePassengerNameException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

/**
 * §3.3.3. Register passenger.
 */
public class DoRegisterPassenger extends Command<TicketOffice> {

	/**
	 * @param receiver
	 */
	public DoRegisterPassenger(TicketOffice receiver) {
		super(Label.REGISTER_PASSENGER, receiver);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException {
		try {
			String m = Message.requestPassengerName();
			Input<String> name = _form.addStringInput(m);
			_form.parse();
			_form.clear();
			Passenger p = new Passenger(_receiver.getTrainCompany(), name.value());
		} catch (InvalidPassengerNameException e) {
			throw new BadPassengerNameException (e.getName());
		}

	}

}