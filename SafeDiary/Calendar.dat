package model;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

public class Calendar {
	
	public HashMap<Date, CalendarData> datas;
	
	public Calendar(){
		this.datas =  new HashMap<Date, CalendarData>();
	}

	public HashMap<Date, CalendarData> getDatas() {
		return datas;
	}

	public void setDatas(HashMap<Date, CalendarData> datas) {
		this.datas = datas;
	}
	
	public void add(CalendarData cd){
		this.datas.put(cd.getDate(), cd);
	}
	
	public void remove(CalendarData cd){
		this.datas.remove(cd.getDate());
	}
	
	public CalendarData getDataByDate(Date date){
		return this.datas.get(date);
	}
	
	public void setDataByDate(CalendarData cd){
		CalendarData tmp = this.getDataByDate(cd.getDate());
		tmp.setDate(cd.getDate());
		tmp.setEvent(cd.getEvent());
	}
	
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
	
	
}
