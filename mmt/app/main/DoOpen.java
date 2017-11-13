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
 * §3.1.1. Open existing document.
 */
public class DoOpen extends Command<TicketOffice> {

	/**
	 * @param receiver
	 */
	public DoOpen(TicketOffice receiver) {
		super(Label.OPEN, receiver);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() {

		try {
			String m = Message.openFile();
			Input<String> input = _form.addStringInput(m);

			_form.parse();
			_form.clear();

			FileInputStream fileIn = new FileInputStream(input.value());
			ObjectInputStream in = new ObjectInputStream(fileIn);

			TrainCompany t = (TrainCompany) in.readObject();
			in.close();
			fileIn.close();

			_receiver.setTrainCompany(t);

		} catch (IOException i) {
			i.printStackTrace();
			return;

		} catch (ClassNotFoundException c) {
			System.out.println("TrainCompany class not found");
			c.printStackTrace();
			return;
		}

	}

}
