package mmt.app.main;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Menu;

import mmt.app.itineraries.ItinerariesMenu;
import mmt.core.TicketOffice;

/**
 * §3.1.2. Open itineraries menu.
 */
public class DoOpenItinerariesMenu extends Command<TicketOffice> {

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public DoOpenItinerariesMenu(TicketOffice receiver) {
		super(Label.OPEN_ITINERARIES_MENU, receiver);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() {
		Menu menu = new ItinerariesMenu(_receiver);
		menu.open();
	}

}
