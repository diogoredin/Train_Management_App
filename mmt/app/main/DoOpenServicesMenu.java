package mmt.app.main;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Menu;

import mmt.core.TicketOffice;
import mmt.app.service.ServicesMenu;

/**
 * §3.1.2. Open menu for managing identifiers and expressions.
 */
public class DoOpenServicesMenu extends Command<TicketOffice> {

	/**
	 * @param receiver
	 */
	public DoOpenServicesMenu(TicketOffice receiver) {
		super(Label.OPEN_SERVICES_MENU, receiver);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() {
		Menu menu = new ServicesMenu(_receiver);
		menu.open();
	}

}
