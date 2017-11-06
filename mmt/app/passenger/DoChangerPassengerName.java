package mmt.app.passenger;

import mmt.core.TicketOffice;
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
		Input<Integer> id = _form.addIntegerInput("Insira o ID do passageiro cujo nome quer mudar: ");
		_form.parse();

		if (_receiver.getTrainCompany().passengerExists(id.value())) {
			_display.addLine("Selecionado o passageiro com ID: " + _receiver.getTrainCompany().passengerDescription(id.value()));
			_display.display(true);

			Input<String> name = _form.addStringInput("Insira o novo nome do passageiro: ");
			_form.parse();
			String newPassenger = _receiver.getTrainCompany().changePassengerName(id.value(), name.value());
			_display.addLine("A mudança de nome do passageiro com ID: " + newPassenger + "; foi feita com sucesso.");

		} else {
			_display.addLine("O passageiro que selecionou nao existe.");
		}

		_form.clear();
		_display.display();
		return;
	}
}
