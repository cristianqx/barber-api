package br.com.dornelasit.barber.util;

public class Util {

	/**
	 * Verifica se o valor eh um numero
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNumeric(String s) {
		if (s == null) {
			return false;
		}
		return s.matches("[-+]?\\d*\\.?\\d+");
	}

	/**
	 * Valida se a string esta nula ou vazia
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNullOrEmpty(String value) {
		return !(value != null && value.trim().length() > 0);
	}
	
	/**
	 * Retira os caracteres especiais do CPF ponto(.) e hifén (-)
	 * 
	 * @param value
	 * @return
	 */
	
	public static String formataCpf(String objeto) {
		objeto = objeto.replaceAll("\\.", "");
		objeto = objeto.replaceAll("-", "");
		return objeto;
	}

}
