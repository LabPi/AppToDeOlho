package com.jp.util;
import android.annotation.SuppressLint;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * @author João Paulo Paiva
 * @atualizado 06/08/2015 
 *
 */

@SuppressLint("SimpleDateFormat")
public class DataUtil 
{
	/**
	 * 
	 * @param data
	 * @return converte uma String e retorna um java.sql.Date
	 */
	public static Date stringToDate(String data)
	{
		java.util.Date date = new java.util.Date();

		try
		{
			date = SimpleDateFormat.getDateInstance().parse(data);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return utilDateToSqlDate(date);
	}

	/**
	 *
	 * @param data
	 * @return converte uma string yyyy-MM-dd para um java.sql.Date
	 */

	public static Date stringSqlToSqlDate(String data)
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date parsed = new java.util.Date();

		try {
			parsed = format.parse(data);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return new Date(parsed.getTime());

	}

	/**
	 *
	 * @param data
	 * @return converte um java.util.Date e retorna uma String
	 */
	public static String dateToString(java.util.Date data)
	{
		return SimpleDateFormat.getDateInstance().format(data);
	}


	//Recebe um sql.Date e retorna uma string
	/**
	 *
	 * @param date
	 * @return recebe um java.sql.Date e retorna uma String
	 */
	public static String dateToString(Date date)
	{
        return SimpleDateFormat.getDateInstance().format(date);
	}

	/**
	 *
	 * @param data
	 * @return converte uma String referente a uma data e retorna um java.util.Date
	 * @throws ParseException
	 * @author João Paulo Paiva
	 */
	public static java.util.Date formataData (String data) throws ParseException
	{
		return SimpleDateFormat.getDateInstance().parse(data);
	}

    public static String formatDateTime(Date data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(data);
    }



	/**
	 *
	 * @param date
	 * @return converte um java.util.Date retornando um java.sql.Date
	 * @author João Paulo Paiva
	 */
	public static Date utilDateToSqlDate(java.util.Date date)
	{
		return new Date(date.getTime());
	}

	/**
	 * 
	 * @param data
	 * @return recebe uma String referente a data e retorna uma String com o incremento +1 no mês
	 */
	public static String incrementaMesData(String data)
	{
		//Transformando em inteiro
		String[] arrayData = data.split("-");
		int ano = Integer.parseInt(arrayData[0]);
		int mes = Integer.parseInt(arrayData[1]);
		int dia = Integer.parseInt(arrayData[2]);
		if(mes < 12)
		{
			mes += 1;
		}
		else
		{
			mes = 1;
			ano += 1;
		}
		return ano + "-" + ((mes<10)?"0"+mes:mes) + "-" + ((dia<10)?"0"+dia:dia);
	}
	
	/**
	 * 
	 * @param data
	 * @return int referente ao mês onde 0 é janeiro e 11 é dezembro
	 */
	
	public static int monthOfDate(String data)
	{
		String[] arrayData = data.split("-");
		int mes = -1;
		try
		{
			mes =  Integer.parseInt(arrayData[1]);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		return mes - 1;
	}
	
	/**
	 * 
	 * @return String com a data atual
	 */
	public static String dataAtual()
	{
		Calendar calendario = Calendar.getInstance();
		int ano = calendario.get(Calendar.YEAR);
		int mes = calendario.get(Calendar.MONTH);
		int dia = calendario.get(Calendar.DAY_OF_MONTH);
		int hora = calendario.get(Calendar.HOUR_OF_DAY);
		int minuto = calendario.get(Calendar.MINUTE);
		int segundo = calendario.get(Calendar.SECOND);
		
		return ano + "-" + mes + "-" + dia + " " + hora + ":" + minuto + ":" + segundo;
	}

	public static String timeStamp()
	{
		Calendar calendario = Calendar.getInstance();
		int ano = calendario.get(Calendar.YEAR);
		int mes = calendario.get(Calendar.MONTH);
		int dia = calendario.get(Calendar.DAY_OF_MONTH);
		int hora = calendario.get(Calendar.HOUR_OF_DAY);
		int minuto = calendario.get(Calendar.MINUTE);
		int segundo = calendario.get(Calendar.SECOND);

		return "" + ano + mes + dia + hora + minuto + segundo;
	}
}
