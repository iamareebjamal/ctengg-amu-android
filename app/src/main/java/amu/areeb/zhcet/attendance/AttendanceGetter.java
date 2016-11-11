package amu.areeb.zhcet.attendance;

import java.util.List;

import amu.areeb.zhcet.model.Attendance;
import android.util.Log;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import android.annotation.SuppressLint;
import java.io.IOException;
import java.io.Serializable;

public class AttendanceGetter implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private boolean success, ran;
	private String name;
	private String fac_no = "14PEB049";
	private String error = "Error";
	private List<Attendance> attendance;
	
	public AttendanceGetter(String fac_no){
		this.fac_no = fac_no;
	}
	
	public boolean getSuccess(){
		return success;
	}
	
	public String getName(){
		return name;
	}
	
	public String getFacultyNumber(){
		return fac_no;
	}
	
	public String getError(){
		return error;
	}
	
	@SuppressLint("DefaultLocale")
	public static String toTitleCase(String givenString) {

		String[] arr = givenString.toLowerCase().split(" ");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
		}
		return sb.toString().trim();
	}
	
	public List<Attendance> getAttendance(){
		if(!ran)
			getAttendanceResult();
		return attendance;
	}
	
	public String getAttendanceResult(){
		if(!ran){
			List<Attendance> result = fetchAttendance();
			if(success){
				attendance = result;
				return "Success";
			} else {
				return error;
			}
		} else if(ran&&success){
			return "Success";
		}
		
		return error;
	}
	
	private List<Attendance> fetchAttendance(){
		ran = true;
		try
		{
			Document doc  = Jsoup.connect("http://ctengg.amu.ac.in/web/table.php?id="+fac_no).timeout(5000).get();
			Element table = doc.select("table").get(0);
			try{
				this.name = toTitleCase(doc.select("strong").text().replaceAll(fac_no, "").trim());
			} catch (StringIndexOutOfBoundsException e){
				success = false;
				error = "Invalid Faculty Number";
				return null;
			}
			System.out.println(name+"\n");

			List<Attendance> l = new ArrayList<Attendance>();
			for(Element subs : table.select("tr")){
				try{
					Elements att = subs.select("td");
					Attendance a = new Attendance();
					Attendance.name = this.name;
					a.subject = att.get(0).text();
					a.total = att.get(1).text();
					a.attended = att.get(2).text();
					a.perc = att.get(3).text();
					a.remark = att.get(4).text();
					a.date = att.get(5).text();
					l.add(a);
					success = true;
				} catch (IndexOutOfBoundsException e){}
				
			}
			return l;
		}
		catch (IOException e)
		{
			success = false;
			error = "Timeout or Site Unavailable. Please retry";
			return null;
		}
	}
	
}
