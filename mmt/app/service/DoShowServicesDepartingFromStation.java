package mmt.app.service;

import java.util.Collection;

import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;
import pt.tecnico.po.ui.Input;

import mmt.core.Service;
import mmt.core.TicketOffice;

/**
 * 3.2.3 Show services departing from station.
 */
public class DoShowServicesDepartingFromStation extends Command<TicketOffice> {

	/** Station name to search. */
	private Input<String> _search;

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public DoShowServicesDepartingFromStation(TicketOffice receiver) {
		super(Label.SHOW_SERVICES_DEPARTING_FROM_STATION, receiver);

		String m = Message.requestStationName();
		_search = _form.addStringInput(m);
	}

	/**
	 * Executes the command.
	 * @see pt.tecnico.po.ui.Command#execute()
	 */
	@Override
	public final void execute() {

		_form.parse();

		/* Gets all services */
		Collection<Service> services = _receiver.getTrainCompany().getServices();

		/* Search for the service */
		for ( Service service : services ) {

			if ( _search.value().equals( service.getStartStation().getName() ) ) {
				_display.addLine(service.toString());
			}

		}

		_display.display();
	}

}
