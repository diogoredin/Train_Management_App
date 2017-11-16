package mmt.app.main;

import java.io.IOException;
import java.io.FileNotFoundException;

import mmt.core.TrainCompany;
import mmt.core.TicketOffice;

import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;
import pt.tecnico.po.ui.Input;

/**
 * §3.1.1. Open existing document.
 */
public class DoOpen extends Command<TicketOffice> {

	/** The input file to open. */
	private Input<String> _input;

	/**
	 * @param receiver
	 */
	public DoOpen(TicketOffice receiver) {
		super(Label.OPEN, receiver);
		
		String m = Message.openFile(); 
		_input = _form.addStringInput(m);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() {

		try {

			_form.parse();
			_receiver.load( _input.value() );

		} catch (FileNotFoundException i) {
			_display.addLine( Message.fileNotFound( _input.value() ) );
			_display.display();
			return;

		} catch (IOException i) {
			i.printStackTrace();
			return;

		} catch (ClassNotFoundException i) {
			i.printStackTrace();
			return;

		}

	}

}
