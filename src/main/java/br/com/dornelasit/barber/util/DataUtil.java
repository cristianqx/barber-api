package br.com.dornelasit.barber.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DataUtil {

	public static String converterData(Calendar data) {
		return new SimpleDateFormat("dd/MM/yyyy").format(data.getTime());
	}
	
	public static String converterDataYYYYDD(Calendar data) {
		return new SimpleDateFormat("yyyy/MM").format(data.getTime());
	}
	
	public static Date converterStringParaDate(String data) throws ParseException {
		return new SimpleDateFormat("dd-MM-yyyy").parse(data);
	}
	
	public static Calendar converterStringParaCalendar(String strData) throws ParseException {
		Calendar data = Calendar.getInstance();
		data.setTime(new SimpleDateFormat("yyyy/MM/dd").parse(strData));
		return data;
	}
	
	public static Calendar converterStringParaCalendarHora(String strData) throws ParseException {
		Calendar data = Calendar.getInstance();
		data.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(strData));
		return data;
	}
	
	public static Calendar converterStringParaData(String data) throws ParseException {
		Calendar retorno = Calendar.getInstance();
		retorno.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(data));
		return retorno;
	}
	
	public static String converterDateParaString(Date data) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").format(data);
	}
	
	public static String converterDateParaString(Calendar data) throws ParseException {
		Date newDate = calendarToDate(data);
		return converterDateParaString(newDate);
	}

	public static String converterDataTimeParaString(Calendar data) throws ParseException {

		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC-3"));
	    SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dataStr = String.valueOf(dateFormatLocal.parse(dateFormatGmt.format(data.getTime())));

		return dataStr;
	}
	// Convert Calendar to Date
	private static Date calendarToDate(Calendar calendar) {
		return calendar.getTime();
	}

	public static String converterTimeString(Time horario) {
		DateFormat format = new SimpleDateFormat("HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("UTC-3"));
	    return format.format(horario.getTime());
	}
	
	public static Time converterStringParaTime(String horario) throws ParseException {
		SimpleDateFormat formatador = new SimpleDateFormat("HH:mm"); 
		formatador.setTimeZone(TimeZone.getTimeZone("UTC-3"));
		Date data = formatador.parse(horario); 
		Time time = new Time(data.getTime());
		
		return time;
		
		}

}
