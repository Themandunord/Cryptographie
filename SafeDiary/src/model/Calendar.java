package model;

/**
 * Classe qui permet de stocker toutes les donn�es de l'utilisateur
 * 
 */

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Calendar {
	
	/**
	 * HashMap contenant les donn�es du calendrier de l'utilisateur
	 */
	public HashMap<Date, CalendarData> datas;
	
	/**
	 * Constructeur par d�faut du Calendrier
	 */
	public Calendar(){
		this.datas =  new HashMap<Date, CalendarData>();
	}
	
	/**
	 * Constructeur permettant de construire un Calendrier � partir d'un string
	 * @param str String au format 
	 * @throws ParseException #;#;#~#
	 */
	public Calendar(String str) throws ParseException{
		this.datas =  new HashMap<Date, CalendarData>();
		String tmp[] = str.split(";");
		
		for(int i = 0; i < tmp.length ; i++)
		{
			String[] tmp_datas = null;
			tmp_datas = tmp[i].split("~");
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.US);
			Date d = sdf.parse(tmp_datas[0]);
			this.add(new CalendarData(d, new Event(tmp_datas[1], Integer.valueOf(tmp_datas[2]), Integer.valueOf(tmp_datas[3]))));
		}
	}

	/**
	 * Retourne la HashMap
	 * @return datas
	 */
	public HashMap<Date, CalendarData> getDatas() {
		return datas;
	}

	/**
	 * Modification de la HashMap
	 * @param datas la HashMap de donn�es
	 */
	public void setDatas(HashMap<Date, CalendarData> datas) {
		this.datas = datas;
	}
	
	/**
	 * Permet d'ajouter des donn�es dans le Calendrier
	 * @param cd CalendarData
	 */
	public void add(CalendarData cd){
		this.datas.put(cd.getDate(), cd);
	}
	
	/**
	 * Supprime une donn�e dans la HashMap � partir d'une donn�e
	 * @param cd CalendarData
	 */
	public void remove(CalendarData cd){
		this.datas.remove(cd.getDate());
	}
	
	/**
	 * Retourne les donn�es � partir d'une Date donn�e
	 * @param date Date
	 * @return Une donn�e du calendrier
	 */
	public CalendarData getDataByDate(Date date){
		return this.datas.get(date);
	}
	
	/**
	 * Modification d'une donn�e � partir d'une Date donn�e en param�tre
	 * @param cd CalendarData
	 */
	public void setDataByDate(CalendarData cd){
		CalendarData tmp = this.getDataByDate(cd.getDate());
		if(tmp == null){
			this.add(cd);
		}
		else{
			tmp.setDate(cd.getDate());
			tmp.setEvent(cd.getEvent());
		}
		
	}
	
	/**
	 * Retourne une liste des donn�es d'une certaine date
	 * @param date Date
	 * @return Liste de donn�es
	 */
	@SuppressWarnings("deprecation")
	public ArrayList<CalendarData> getDataByDay(Date date){
		ArrayList<CalendarData> tmp = new ArrayList<CalendarData>();
		for(CalendarData cd : datas.values()){
			Date d = cd.getDate();
			if(d.getYear() == date.getYear() && d.getMonth() == date.getMonth() && d.getDate() == date.getDate())
				tmp.add(cd);
		}
		
		return tmp;
	}
	
	/**
	 * Retourne un tableau de byte contenant les informations du calendrier a crypter
	 * @return Un tableau de byte du calendrier
	 * @throws UnsupportedEncodingException
	 */
	public byte[] getBytes() throws UnsupportedEncodingException{
		String tmp = "";
		for(CalendarData cd : datas.values()){
			tmp += cd.getDate() + "~";
			tmp += cd.getEvent().getEvent() + "~";
			tmp += cd.getEvent().getDuration() + "~";
			tmp += cd.getEvent().getHours();
			tmp += ";";
		}
		
		return tmp.getBytes("utf-8");
	}
	
	/**
	 * Supprime une donn�e par la Date
	 * @param date Date
	 */
	public void removeByDate(Date date){
		datas.remove(date);
	}
	
	
}
