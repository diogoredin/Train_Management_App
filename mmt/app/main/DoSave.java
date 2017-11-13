package mmt.app.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mmt.core.TicketOffice;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;

/**
 * §3.1.1. Save to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<TicketOffice> {
	
	/**
	 * @param receiver
	 */
	public DoSave(TicketOffice receiver) {
		super(Label.SAVE, receiver);
	}

	/** @see pt.tecnico.po.ui.Command#execute() */
	@Override
	public final void execute() {
		
		try {
			FileOutputStream fileOut = new FileOutputStream("trainCompany.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			out.writeObject(_receiver.getTrainCompany());
			out.close();
			fileOut.close();

			System.out.printf("Serialized data is saved in trainCompany.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}

	}
}
