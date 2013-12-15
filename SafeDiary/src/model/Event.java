package model;

public class Event {
	private String event;
	private int duration;
	private int hours;
	
	public Event(String event, int duration, int hours){
		this.event = event;
		this.duration = duration;
		this.hours = hours;
	}
	
	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
