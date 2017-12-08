package mmt.app.service;

import java.util.Collection;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;
import pt.tecnico.po.ui.Input;

import mmt.core.Service;
import mmt.core.TicketOffice;

import mmt.app.exceptions.NoSuchStationException;
import mmt.core.exceptions.NoSuchStationNameException;

import pt.tecnico.po.ui.DialogException;

/**
 * 3.2.4 Show services arriving at station.
 */
public class DoShowServicesArrivingAtStation extends Command<TicketOffice> {

	/** The Station name to search. */
	private Input<String> _search;

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public DoShowServicesArrivingAtStation(TicketOffice receiver) {
		super(Label.SHOW_SERVICES_ARRIVING_AT_STATION, receiver);

		String m = Message.requestStationName();
		_search = _form.addStringInput(m);
	}

	/**
	 * Executes the command.
	 * @see pt.tecnico.po.ui.Command#execute()
	 */
	@Override
	public final void execute() throws DialogException {

		try {
			
			_form.parse();

			/* Search for the services */
			Collection<Service> services = _receiver.searchServiceWithEndStation( _search.value() );

			/* Display the services */
			services.forEach((Service s)-> {
				_display.addLine(s.toString());
			});
			
			_display.display();

		} catch (NoSuchStationNameException e) {
			throw new NoSuchStationException(e.getName());
		}

	}

}
