package model;

/**
 * Classe qui permet de cr�er un doublet : Date/Evenement
 * 
 */
import java.util.Date;

public class CalendarData {

	/**
	 * Date de l'�v�nement
	 */
	private Date date;
	/**
	 * Evenement
	 */
	private Event event;
	
	/**
	 * Constructeur du doublet Date/Evenement
	 * @param date Date
	 * @param event Event
	 */
	public CalendarData(Date date, Event event){
		this.date = date;
		this.event = event;
	}

	/**
	 * Retourne la date
	 * @return date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Modification de la Date
	 * @param date Date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Retourne l'�venement
	 * @return event
	 */
	public Event getEvent() {
		return event;
	}
	
	/**
	 * Modification de l'�v�nement
	 * @param event
	 */
	public void setEvent(Event event) {
		this.event = event;
	}
	
}
