package mmt.app.service;

import java.util.Collection;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;
import pt.tecnico.po.ui.Input;

import mmt.core.Service;
import mmt.core.TicketOffice;

import pt.tecnico.po.ui.DialogException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.app.exceptions.NoSuchServiceException;

/**
 * 3.2.2 Show service by number.
 */
public class DoShowServiceByNumber extends Command<TicketOffice> {

	/** The service id to search. */
	private Input<Integer> _id;

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public DoShowServiceByNumber(TicketOffice receiver) {
		super(Label.SHOW_SERVICE_BY_NUMBER, receiver);
		
		String m = Message.requestServiceId();
		_id = _form.addIntegerInput(m);
	}

	/**
	 * Executes the command.
	 * @see pt.tecnico.po.ui.Command#execute()
	 */
	@Override
	public final void execute() throws DialogException {
		try {

			_form.parse();

			_display.addLine( _receiver.getTrainCompany().getService( _id.value() ).toString() );
			_display.display();

		} catch (NoSuchServiceIdException e) {
			throw new NoSuchServiceException(e.getId());
		}
	}

}
