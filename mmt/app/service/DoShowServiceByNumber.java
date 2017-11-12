package mmt.app.service;

import mmt.core.Service;
import mmt.core.TicketOffice;

import java.util.Collection;

import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;
import pt.tecnico.po.ui.Input;

import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.app.exceptions.NoSuchServiceException;

/**
 * 3.2.2 Show service by number.
 */
public class DoShowServiceByNumber extends Command<TicketOffice> {

	/**
	 * @param receiver
	 */
	public DoShowServiceByNumber(TicketOffice receiver) {
		super(Label.SHOW_SERVICE_BY_NUMBER, receiver);
	}

	@Override
	public final void execute() throws DialogException {
		try {
			String m = Message.requestServiceId();
			Input<Integer> id = _form.addIntegerInput(m);

			_form.parse();
			_form.clear();

			_display.addLine(_receiver.getTrainCompany().getService(id.value()).toString());
			_display.display();

		} catch (NoSuchServiceIdException e) {
			throw new NoSuchServiceException(e.getId());
		}
	}

}
