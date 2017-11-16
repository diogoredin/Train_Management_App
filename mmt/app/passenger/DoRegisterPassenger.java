package mmt.app.passenger;

import mmt.core.Passenger;
import mmt.core.TicketOffice;
import mmt.core.TrainCompany;

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

	/** Request name for new passenger. */
	private Input<String> _name;

	/**
	 * @param receiver
	 */
	public DoRegisterPassenger(TicketOffice receiver) {
		super(Label.REGISTER_PASSENGER, receiver);

		String m = Message.requestPassengerName();
		_name = _form.addStringInput(m);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException {
		try {

			_form.parse();

			TrainCompany company = _receiver.getTrainCompany();
			int id = company.getNextPassengerId();
			Passenger p = new Passenger(id, _name.value());
			company.addPassenger(p);

		} catch (InvalidPassengerNameException e) {
			throw new BadPassengerNameException (e.getName());
		}

	}

}