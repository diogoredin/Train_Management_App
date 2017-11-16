package mmt.app.service;

import java.util.Collection;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;

import mmt.core.Service;
import mmt.core.TicketOffice;

/**
 * 3.2.1 Show all services.
 */
public class DoShowAllServices extends Command<TicketOffice> {

	/**
	 * @param receiver
	 */
	public DoShowAllServices(TicketOffice receiver) {
		super(Label.SHOW_ALL_SERVICES, receiver);
	}

	@Override
	public final void execute() {
		
		Collection<Service> services = _receiver.getTrainCompany().getServices();
		services.forEach((Service service)-> {
			_display.addLine(service.toString());
		});
		_display.display();
	}

}
