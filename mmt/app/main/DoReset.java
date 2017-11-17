package mmt.app.main;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;

import mmt.core.TicketOffice;

/**
 * §3.1.1. Reset the ticket office.
 */
public class DoReset extends Command<TicketOffice> {

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public DoReset(TicketOffice receiver) {
		super(Label.RESET, receiver);
	}

	/** 
	 * Executes the command.
	 * @see pt.tecnico.po.ui.Command#execute()
	 */
	@Override
	public final void execute() {
		_receiver.reset();
	}

}
