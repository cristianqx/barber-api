package br.com.dornelasit.barber.api.exception;

public class AgendaException extends BaseException{

	private static final long serialVersionUID = 3347995348447058597L;

	public AgendaException(int errorCode, String errorMessage) {
		super();
		super.setErrorCode(errorCode);
		super.setErrorMessage(errorMessage);
	}
}
