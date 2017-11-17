package mmt.app.service;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Menu;

import mmt.core.TicketOffice;

/** Menu builder for operations on schedules. */
public class ServicesMenu extends Menu {

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public ServicesMenu(TicketOffice receiver) {
		super(Label.TITLE, new Command<?>[] {
			new DoShowAllServices(receiver),
			new DoShowServiceByNumber(receiver),
			new DoShowServicesDepartingFromStation(receiver),
			new DoShowServicesArrivingAtStation(receiver),
		});
	}

}
