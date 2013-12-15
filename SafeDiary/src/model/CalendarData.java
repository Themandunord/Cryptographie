package model;

import java.util.Date;

public class CalendarData {

	private Date date;
	private Event event;
	
	public CalendarData(Date date, Event event){
		this.date = date;
		this.event = event;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
}
