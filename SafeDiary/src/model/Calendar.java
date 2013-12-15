package model;

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
	
	public CalendarData getDataByDate(Date date){
		return this.datas.get(date);
	}
	
	public void setDataByDate(CalendarData cd){
		CalendarData tmp = this.getDataByDate(cd.getDate());
		tmp.setDate(cd.getDate());
		tmp.setEvent(cd.getEvent());
	}
}
