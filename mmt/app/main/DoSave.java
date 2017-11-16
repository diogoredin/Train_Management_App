package mmt.app.main;

import java.io.IOException;

import mmt.core.TrainCompany;
import mmt.core.TicketOffice;

import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;
import pt.tecnico.po.ui.Input;

/**
 * §3.1.1. Save to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<TicketOffice> {
	
	/** New name of save file. */
	private Input<String> _file;

	/**
	 * @param receiver
	 */
	public DoSave(TicketOffice receiver) {
		super(Label.SAVE, receiver);

		String m = Message.newSaveAs();
		_file = _form.addStringInput(m);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() {

		try {

			if ( _file.value().isEmpty() ) {
				_form.parse();
			}

			_receiver.save( _file.value() );

		} catch (IOException i) {
			i.printStackTrace();
			return;
		}

	}
}
