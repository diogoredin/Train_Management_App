package mmt.app.passenger;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;

import mmt.core.Passenger;
import mmt.core.TicketOffice;
import mmt.core.TrainCompany;

import pt.tecnico.po.ui.DialogException;
import mmt.app.exceptions.BadPassengerNameException;
import mmt.app.exceptions.DuplicatePassengerNameException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NonUniquePassengerNameException;

/**
 * §3.3.3. Register passenger.
 */
public class DoRegisterPassenger extends Command<TicketOffice> {

	/** Request name for new passenger. */
	private Input<String> _name;

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public DoRegisterPassenger(TicketOffice receiver) {
		super(Label.REGISTER_PASSENGER, receiver);

		String m = Message.requestPassengerName();
		_name = _form.addStringInput(m);
	}

	/** 
	 * Executes the command.
	 * @see pt.tecnico.po.ui.Command#execute()
	 */
	@Override
	public final void execute() throws DialogException {
		try {

			_form.parse();

			int id = _receiver.getNextPassengerId();
			_receiver.addPassenger(id, _name.value());

		} catch (InvalidPassengerNameException e) {
			throw new BadPassengerNameException (e.getName());
		}

	}

}