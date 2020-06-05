package br.com.dornelasit.barber.api.exception;

public class AuthenticationException extends BaseException {

	private static final long serialVersionUID = 3807063095089687057L;

	public AuthenticationException(int errorCode, String errorMessage) {
		super();
		super.setErrorCode(errorCode);
		super.setErrorMessage(errorMessage);
	}
	
}
