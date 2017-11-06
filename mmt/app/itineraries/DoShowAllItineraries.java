package mmt.app.itineraries;

import mmt.core.TicketOffice;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;

//FIXME import other classes if necessary

/**
 * §3.4.1. Show all itineraries (for all passengers).
 */
public class DoShowAllItineraries extends Command<TicketOffice> {

  /**
   * @param receiver
   */
  public DoShowAllItineraries(TicketOffice receiver) {
    super(Label.SHOW_ALL_ITINERARIES, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    //FIXME implement command
  }

}
