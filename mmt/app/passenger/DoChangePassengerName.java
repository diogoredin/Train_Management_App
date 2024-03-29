package mmt.app.passenger;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;

import mmt.core.TicketOffice;
import mmt.core.TrainCompany;

import pt.tecnico.po.ui.DialogException;
import mmt.app.exceptions.BadPassengerNameException;
import mmt.app.exceptions.NoSuchPassengerException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchPassengerIdException;


/**
 * §3.3.4. Change passenger name.
 */
public class DoChangePassengerName extends Command<TicketOffice> {

	/** The requested id. */
	private Input<Integer> _id;

	/** The new passenger's name. */
	private Input<String> _name;

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public DoChangePassengerName(TicketOffice receiver) {
		super(Label.CHANGE_PASSENGER_NAME, receiver);
		
		String m = Message.requestPassengerId();
		_id = _form.addIntegerInput(m);
		m = Message.requestPassengerName();
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
			_receiver.changePassengerName(_id.value(), _name.value());

		} catch (NoSuchPassengerIdException e) {
			throw new NoSuchPassengerException(e.getId());

		} catch (InvalidPassengerNameException e) {
			throw new BadPassengerNameException(e.getName());
		}
	}
}