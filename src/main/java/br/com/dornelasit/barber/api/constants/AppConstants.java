package br.com.dornelasit.barber.api.constants;

import java.text.SimpleDateFormat;

public interface AppConstants {

	/*
	 * Atributos de STATUS_AGENDAMENTO	
	 */
	public static final Short STATUS_AGENDAMENTO_AGENDADO = 1; 
	public static final Short STATUS_AGENDAMENTO_FINALIZADO = 2;
	public static final Short STATUS_AGENDAMENTO_CANCELADO = 3;
	
	
	/*
	 * Atributos de tipos de usuario
	 */
    public final static Integer TIPO_USUARIO_CLIENTE = 1;
    public final static Integer TIPO_USUARIO_PROFISSIONAL = 2;
    public final static Integer TIPO_USUARIO_ADM = 3;
    public final static Boolean USUARIO_ATIVO = true;
    public final static Boolean USUARIO_DESATIVADO = false;
    
}
