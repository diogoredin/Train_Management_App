package mmt.app;

import static pt.tecnico.po.ui.Dialog.IO;

import mmt.core.TicketOffice;
import mmt.core.TrainCompany;
import mmt.core.exceptions.ImportFileException;

import mmt.app.main.MainMenu;
import pt.tecnico.po.ui.Menu;

/**
 * Main driver for the travel management application.
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/* Creates a train company and ticket office for our app */
		TrainCompany company = new TrainCompany();
		TicketOffice office = new TicketOffice(company);

		/* Checks if our using the app through the terminal or with import files */
		String datafile = System.getProperty("import");

		/* If we have an import file we create a new parser */
		if (datafile != null) {
			try {
				office.importFile(datafile);
			} catch (ImportFileException e) {
				e.printStackTrace(); // No behavior described: just present the problem
			}
		}

		Menu menu = new MainMenu(office);
		menu.open();

		IO.close();
	}

}
