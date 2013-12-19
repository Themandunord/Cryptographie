package model;

/**
 * Classe modélisant un tripet événement : event/duration/hours
 *
 */

public class Event {
	private String event;
	private int duration;
	private int hours;
	
	/**
	 * Constructeur valué qui crée un triplet Event
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
	 * Retourne l'heure de l'événement
	 * @return
	 */
	public int getHours() {
		return hours;
	}
	/**
	 * Modification de l'heure de l'événement
	 * @param hours Int
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}
	/**
	 * Retourne le nom de l'événement
	 * @return String
	 */
	public String getEvent() {
		return event;
	}
	
	/**
	 * Modification du nom de l'événement
	 * @param event String
	 */
	public void setEvent(String event) {
		this.event = event;
	}
	
	/**
	 * Retourne la durée de l'événement
	 * @return Int
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Modification de la durée de l'événement
	 * @param duration 
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
