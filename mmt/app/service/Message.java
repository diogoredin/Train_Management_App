package mmt.app.service;

/** Messages for menu interactions. */
@SuppressWarnings("nls")
public final class Message {

	/**
	 * @return prompt for identifier
	 */
	public static String requestServiceId() {
		return "Identificador do serviço: ";
	}

	/**
	 * @param id the corresponding non-existent service id.
	 * @return error message
	 */
	public static String noSuchServiceId(int id) {
		return "O serviço com o número " + id + " não existe.";
	}

	/**
	 * @return prompt for name
	 */
	public static String requestStationName() {
		return "Nome da estação: ";
	}

	/**
	 * @param name the corresponding non-existent station name.
	 * @return error message
	 */
	public static String noSuchStationName(String name) {
		return "A estação com o nome '" + name + "' não existe.";
	}

	/**
	 * @param name the corresponding non-existent service class name.
	 * @return error message
	 */
	public static String noSuchServiceClassName(String name) {
		return "A classe de serviço com o nome '" + name + "' não existe.";
	}

	/**
	 * @param pid the passenger's id.
	 * @param iid the itinerary's id.
	 * @return error message
	 */
	public static String noSuchItineraryChoiceForPassanger(int pid, int iid) {
		return "A escolha do itinerário " + iid + " é inválida para o passageiro " + pid + ".";
	}

	/**
	 * @param name the invalid passenger name.
	 * @return error message
	 */
	public static String invalidPassengerName(String name) {
		return "'" + name + "' não pode ser usado como nome de passageiro.";
	}

	/**
	 * @param name the duplicate passenger name.
	 * @return error message
	 */
	public static String duplicatePassengerName(String name) {
		return "O nome '" + name + "' já está a ser usado por outro passageiro.";
	}

	/**
	 * @param date the corresponding invalid date.
	 * @return error message
	 */
	public static String invalidDateSpecification(String date) {
		return "A data '" + date + "' é inválida.";
	}

	/**
	 * @param time the corresponding invalid time.
	 * @return error message
	 */
	public static String invalidTimeSpecification(String time) {
		return "A hora '" + time + "' é inválida.";
	}

	/** Prevent instantiation. */
	private Message() {
		// EMPTY
	}

}
