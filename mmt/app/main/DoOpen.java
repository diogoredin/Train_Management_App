package mmt.app.main;

import java.io.IOException;

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

	/** The saved input file name. */
	private String _inputName;

	/**
	 * @param receiver
	 */
	public DoOpen(TicketOffice receiver) {
		super(Label.OPEN, receiver);
		
		String m = Message.openFile(); 
		_input = _form.addStringInput(m);
		_inputName = "";
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() {

		try {

			if ( _inputName.isEmpty() ) {
				_form.parse();
				_inputName = _input.value();
			}

			_receiver.load( _inputName );

		} catch (IOException i) {
			i.printStackTrace();
			return;

		} catch (ClassNotFoundException i) {
			i.printStackTrace();
			return;
		}

	}

}
