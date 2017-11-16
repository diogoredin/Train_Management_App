package mmt.app.service;

import mmt.core.Service;
import mmt.core.TicketOffice;

import java.util.Collection;
import java.lang.String;

import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;
import pt.tecnico.po.ui.Input;

import mmt.app.exceptions.NoSuchStationException;

/**
 * 3.2.4 Show services arriving at station.
 */
public class DoShowServicesArrivingAtStation extends Command<TicketOffice> {

	/** The Station name to search. */
	private Input<String> _search;

	/**
	 * @param receiver
	 */
	public DoShowServicesArrivingAtStation(TicketOffice receiver) {
		super(Label.SHOW_SERVICES_ARRIVING_AT_STATION, receiver);

		String m = Message.requestStationName();
		_search = _form.addStringInput(m);
	}

	@Override
	public final void execute() {

		_form.parse();

		/* Gets all services */
		Collection<Service> services = _receiver.getTrainCompany().getServices();

		/* Search for the service */
		for ( Service service : services ) {

			if ( _search.value().equals( service.getEndStation().getName() ) ) {
				_display.addLine(service.toString());
			}

		}

		_display.display();

	}

}
