package model;

/**
 * Classe mod�lisant un tripet �v�nement : event/duration/hours
 *
 */

public class Event {
	private String event;
	private int duration;
	private int hours;
	
	/**
	 * Constructeur valu� qui cr�e un triplet Event
	 * @param event String
	 * @param duration Int
	 * @param hours Int
	 */
	public Event(String event, int duration, int hours){
		this.event = event;
		this.duration = duration;
		this.hours = hours;
	}
	
	/**
	 * Retourne l'heure de l'�v�nement
	 * @return
	 */
	public int getHours() {
		return hours;
	}
	/**
	 * Modification de l'heure de l'�v�nement
	 * @param hours Int
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}
	/**
	 * Retourne le nom de l'�v�nement
	 * @return String
	 */
	public String getEvent() {
		return event;
	}
	
	/**
	 * Modification du nom de l'�v�nement
	 * @param event String
	 */
	public void setEvent(String event) {
		this.event = event;
	}
	
	/**
	 * Retourne la dur�e de l'�v�nement
	 * @return Int
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Modification de la dur�e de l'�v�nement
	 * @param duration 
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
