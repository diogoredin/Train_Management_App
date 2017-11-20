package mmt.app.main;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;
import pt.tecnico.po.ui.Input;

import mmt.core.TrainCompany;
import mmt.core.TicketOffice;

import java.io.IOException;
import java.io.FileNotFoundException;
import pt.tecnico.po.ui.DialogException;

/**
 * §3.1.1. Open existing document.
 */
public class DoOpen extends Command<TicketOffice> {

	/** The input file to open. */
	private Input<String> _file;

	/**
	 * @param receiver the associated TicketOffice.
	 */
	public DoOpen(TicketOffice receiver) {
		super(Label.OPEN, receiver);
		
		String m = Message.openFile(); 
		_file = _form.addStringInput(m);
	}

	/** 
	 * Executes the command.
	 * @see pt.tecnico.po.ui.Command#execute()
	 */
	@Override
	public final void execute() throws DialogException {

		try {
			_form.parse();
			_receiver.setFileName( _file.value() );
			_receiver.load( _file.value() );

		} catch (FileNotFoundException i) {
			_display.addLine( Message.fileNotFound() );
			_display.display();

		} catch (IOException i) {
			i.printStackTrace();

		} catch (ClassNotFoundException i) {
			i.printStackTrace();

		}

	}

}
