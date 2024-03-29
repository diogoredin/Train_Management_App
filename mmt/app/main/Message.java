package mmt.app.main;

/** Messages for main menu interactions. */
@SuppressWarnings("nls")
public final class Message {

	/**
	 * @return string with prompt for filename to open.
	 */
	public static final String openFile() {
		return "Ficheiro a abrir: ";
	}

	/**
	 * @return string with "file not found" message.
	 */
	public static final String fileNotFound() {
		return "O ficheiro não existe.";
	}

	/**
	 * @param filename the associated non-existent file name.
	 * @return string with "file not found" message (more elaborate).
	 */
	public static final String fileNotFound(String filename) {
		return "O ficheiro '" + filename + "' não existe.";
	}

	/**
	 * @return string with a warning and a question.
	 */
	public static final String newSaveAs() {
		return "Ficheiro sem nome. " + saveAs();
	}

	/**
	 * @return string asking for a filename.
	 */
	public static final String saveAs() {
		return "Guardar ficheiro como: ";
	}

	/**
	 * @return string confirming that user wants to save.
	 */
	public static final String saveBeforeExit() {
		return "Guardar antes de fechar? ";
	}

	/** @return prompt for passenger id. */
	public static String requestPassengerId() {
		return "Identificador do passageiro: ";
	}

	/**
	 * @param id the non-existent passenger id.
	 * @return error message
	 */
	public static Object noSuchPassenger(int id) {
		return "O passageiro " + id + "não existe.";
	}

	/** Prevent instantiation. */
	private Message() {
		// EMPTY
	}

}