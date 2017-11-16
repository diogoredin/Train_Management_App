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
 * §3.1.1. Save to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<TicketOffice> {
	
	/** New name of save file. */
	private Input<String> _file;

	/** Named of saved file. */
	private String _fileName;

	/**
	 * @param receiver
	 */
	public DoSave(TicketOffice receiver) {
		super(Label.SAVE, receiver);

		String m = Message.newSaveAs();
		_file = _form.addStringInput(m);
		_fileName = "";
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() {

		try {

			if ( _fileName.isEmpty() ) {
				_form.parse();
				_fileName = _file.value();
			}

			_receiver.save( _fileName );

		} catch (FileNotFoundException i) {
			_display.addLine( Message.fileNotFound( _file.value() ) );
			_display.display();
			return;

		} catch (IOException i) {
			i.printStackTrace();
			return;

		}
		

	}
}
