package model;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Calendar {
	
	public HashMap<Date, CalendarData> datas;
	
	public Calendar(){
		this.datas =  new HashMap<Date, CalendarData>();
	}
	
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
		if(tmp == null){
			this.add(cd);
		}
		else{
			tmp.setDate(cd.getDate());
			tmp.setEvent(cd.getEvent());
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public ArrayList<CalendarData> getDataByDay(Date date){
		ArrayList<CalendarData> tmp = new ArrayList<CalendarData>();
		for(CalendarData cd : datas.values()){
			Date d = cd.getDate();
			if(d.getYear() == date.getYear() && d.getMonth() == date.getMonth()
					&& d.getDay() == date.getDay())
				tmp.add(cd);
		}
		
		return tmp;
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
