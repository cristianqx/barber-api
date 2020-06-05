package br.com.dornelasit.barber.api.exception;

public class UsuarioException extends BaseException {
	
	private static final long serialVersionUID = 7389764480771045718L;

	public UsuarioException(int errorCode, String errorMessage) {
		super();
		super.setErrorCode(errorCode);
		super.setErrorMessage(errorMessage);
	}
}
