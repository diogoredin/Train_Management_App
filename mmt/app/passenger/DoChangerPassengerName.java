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

//FIXME import other classes if necessary

/**
 * §3.3.4. Change passenger name.
 */
public class DoChangerPassengerName extends Command<TicketOffice> {

	//FIXME define input fields

	/**
	 * @param receiver
	 */
	public DoChangerPassengerName(TicketOffice receiver) {
		super(Label.CHANGE_PASSENGER_NAME, receiver, new PassengerCommandValidity(receiver));
		//FIXME initilize input fields
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException {
		String m = Message.requestPassengerId();

		Input<Integer> id = _form.addIntegerInput(m);
		_form.parse();
		_form.clear();

		TrainCompany company = _receiver.getTrainCompany();

		if (company.passengerExists(id.value())) {

			m = Message.requestPassengerName();
			Input<String> name = _form.addStringInput(m);
			_form.parse();
			company.changePassengerName(id.value(), name.value());
		}

		_form.clear();
	}
}
