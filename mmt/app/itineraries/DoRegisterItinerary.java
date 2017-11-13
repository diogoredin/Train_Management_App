package mmt.app.itineraries;

import mmt.core.TicketOffice;
import mmt.app.exceptions.BadDateException;
import mmt.app.exceptions.BadTimeException;
import mmt.app.exceptions.NoSuchItineraryException;
import mmt.app.exceptions.NoSuchPassengerException;
import mmt.app.exceptions.NoSuchStationException;
import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.Display;

/**
 * §3.4.3. Add new itinerary.
 */
public class DoRegisterItinerary extends Command<TicketOffice> {

	/**
	 * @param receiver
	 */
	public DoRegisterItinerary(TicketOffice receiver) {
		super(Label.REGISTER_ITINERARY, receiver);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException {
		/*
		try {
			//FIXME implement command
			// must call (at least) _receiver.searchItineraries(...) and _receiver.commitItinerary(...)
		} catch (NoSuchPassengerIdException e) {
			throw new NoSuchPassengerException(e.getId());
		} catch (NoSuchStationNameException e) {
			throw new NoSuchStationException(e.getName());
		} catch (NoSuchItineraryChoiceException e) {
			throw new NoSuchItineraryException(e.getPassengerId(), e.getItineraryId());
		} catch (BadDateSpecificationException e) {
			throw new BadDateException(e.getDate());
		} catch (BadTimeSpecificationException e) {
			throw new BadTimeException(e.getTime());
		}
		*/
	}
}
