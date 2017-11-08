package mmt.app.passenger;

import mmt.core.TicketOffice;
import mmt.core.TrainCompany;

import mmt.app.exceptions.BadPassengerNameException;
import mmt.app.exceptions.DuplicatePassengerNameException;
import mmt.app.exceptions.NoSuchPassengerException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NonUniquePassengerNameException;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

/**
 * §3.3.4. Change passenger name.
 */
public class DoChangerPassengerName extends Command<TicketOffice> {

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public DoChangerPassengerName(TicketOffice receiver) {
		super(Label.CHANGE_PASSENGER_NAME, receiver, new PassengerCommandValidity(receiver));
	}

	/** 
	 * Executes the command.
	 */
	@Override
	public final void execute() throws DialogException {

		String m = Message.requestPassengerId();

		Input<Integer> id = _form.addIntegerInput(m);
		_form.parse();
		_form.clear();

		TrainCompany company = _receiver.getTrainCompany();

		try {
			m = Message.requestPassengerName();
			Input<String> name = _form.addStringInput(m);
			_form.parse();
			_form.clear();
			company.changePassengerName(id.value(), name.value());

		} catch (NoSuchPassengerIdException e) {
			throw new NoSuchPassengerException(e.getId());

		} catch (InvalidPassengerNameException e) {
			throw new BadPassengerNameException(e.getName());

		}
	}
}
