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
import mmt.core.exceptions.NoSegmentsException;

/**
 * 3.2.3 Show services departing from station.
 */
public class DoShowServicesDepartingFromStation extends Command<TicketOffice> {

	/**
	 * @param receiver
	 */
	public DoShowServicesDepartingFromStation(TicketOffice receiver) {
		super(Label.SHOW_SERVICES_DEPARTING_FROM_STATION, receiver);
	}

	@Override
	public final void execute() throws DialogException {
		try {
			String m = Message.requestStationName();
			Input<String> search = _form.addStringInput(m);

			_form.parse();
			_form.clear();

			/* Gets all services */
			Collection<Service> services = _receiver.getTrainCompany().getServices();

			/* Search for the service */
			for ( Service service : services ) {

				if ( search.value().equals( service.getStartStation().getName() ) ) {
					_display.addLine(service.toString());
				}
			}

			_display.display();
		}
		catch (NoSegmentsException e) {
		}
	}

}
