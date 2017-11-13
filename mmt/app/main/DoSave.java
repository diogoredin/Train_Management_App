package mmt.app.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

			_form.parse();

			FileOutputStream fileOut = new FileOutputStream(_file.value());
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			out.writeObject(_receiver.getTrainCompany());
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}

	}
}
