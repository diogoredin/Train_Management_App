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
	public final void execute() throws DialogException {

		try {
			
			_form.parse();

			/* Search for the service */
			Collection<Service> services = _receiver.searchServiceWithStartStation( _search.value() );

			/* Display the service */
			services.forEach((Service s)-> {
				_display.addLine(s.toString());
			});
			
			_display.display();

		} catch (NoSuchStationNameException e) {
			throw new NoSuchStationException(e.getName());
		}

	}

}
