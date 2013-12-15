package model;

public class Event {
	private String event;
	private int minute;
	
	public Event(String event, int minute){
		this.event = event;
		this.minute = minute;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}
}
