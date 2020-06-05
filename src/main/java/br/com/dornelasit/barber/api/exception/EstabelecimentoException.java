package br.com.dornelasit.barber.api.exception;

public class EstabelecimentoException extends BaseException{

	private static final long serialVersionUID = 3347995348447058597L;

	public EstabelecimentoException(int errorCode, String errorMessage) {
		super();
		super.setErrorCode(errorCode);
		super.setErrorMessage(errorMessage);
	}
}
