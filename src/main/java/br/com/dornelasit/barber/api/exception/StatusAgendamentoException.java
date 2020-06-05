package br.com.dornelasit.barber.api.exception;

public class StatusAgendamentoException extends BaseException {
	
	private static final long serialVersionUID = 7389764480771045718L;

	public StatusAgendamentoException(int errorCode, String errorMessage) {
		super();
		super.setErrorCode(errorCode);
		super.setErrorMessage(errorMessage);
	}
}
