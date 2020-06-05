package br.com.dornelasit.barber.api.exception;

public class HorarioFuncionamentoException extends BaseException{

	private static final long serialVersionUID = 3347995348447058597L;

	public HorarioFuncionamentoException(int errorCode, String errorMessage) {
		super();
		super.setErrorCode(errorCode);
		super.setErrorMessage(errorMessage);
	}
}
