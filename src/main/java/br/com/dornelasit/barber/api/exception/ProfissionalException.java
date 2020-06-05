package br.com.dornelasit.barber.api.exception;

public class ProfissionalException extends BaseException{

	private static final long serialVersionUID = 3347995348447058597L;

	public ProfissionalException(int errorCode, String errorMessage) {
		super();
		super.setErrorCode(errorCode);
		super.setErrorMessage(errorMessage);
	}
}
