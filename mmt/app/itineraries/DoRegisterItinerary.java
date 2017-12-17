package mmt.app.itineraries;

import java.util.ArrayList;
import java.util.Collections;
import java.time.LocalTime;

import mmt.core.TicketOffice;
import mmt.core.Itinerary;

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
	 * @param receiver the associated TicketOffice.
	 */
	public DoRegisterItinerary(TicketOffice receiver) {
		super(Label.REGISTER_ITINERARY, receiver);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException {
			try {
				
			Input<Integer> id = _form.addIntegerInput(Message.requestPassengerId());
			Input<String> start = _form.addStringInput(Message.requestDepartureStationName());
			Input<String> end = _form.addStringInput(Message.requestArrivalStationName());
			Input<String> date = _form.addStringInput(Message.requestDepartureDate());
			Input<String> time = _form.addStringInput(Message.requestDepartureTime());

			_form.parse();
			_form.clear();
			ArrayList<Itinerary> itineraryOptions;
			itineraryOptions = _receiver.searchItineraries(id.value(), start.value(), end.value(), date.value(), time.value());

			Collections.sort(itineraryOptions);
			_receiver.updateListId(itineraryOptions);

			itineraryOptions.forEach((Itinerary it)-> {
				_display.addLine(it.toString());
			});

			_display.display();

			int options = itineraryOptions.size();

			if (options == 0) return;
			Input<Integer> choice = _form.addIntegerInput(Message.requestItineraryChoice());

			_form.parse();
			_form.clear();

			if (choice.value() != 0) {
				if (choice.value() > options || choice.value() < 0) {
					throw new NoSuchItineraryChoiceException(id.value(),choice.value());
				}
				_receiver.commitItinerary(id.value(), itineraryOptions.get(choice.value() - 1));
			}


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
	
	} 
}