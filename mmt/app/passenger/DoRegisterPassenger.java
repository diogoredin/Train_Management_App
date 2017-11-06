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

//FIXME import other classes if necessary

/**
 * §3.3.3. Register passenger.
 */
public class DoRegisterPassenger extends Command<TicketOffice> {

	/**
	 * @param receiver
	 */
	public DoRegisterPassenger(TicketOffice receiver) {
		super(Label.REGISTER_PASSENGER, receiver);
		//FIXME initialize input fields
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException {
		Input<String> name = _form.addStringInput("Insira o nome do passageiro que quer registar: ");
		_form.parse();
		Passenger p = new Passenger(_receiver.getTrainCompany(), name.value());
		_display.addLine("O registo do passageiro com ID: " + p.toString() + "; foi feito com sucesso.");
		_display.display();
		_form.clear();

	}

}