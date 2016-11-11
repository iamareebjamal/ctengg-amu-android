package amu.areeb.zhcet.utils;

import android.text.TextUtils;
import java.util.Calendar;

public class Utils
{
	public static boolean isFacultyNumber(String fcNo){
		if(fcNo.length()==9)
			return true;
		else if(fcNo.length()!=8)
			return false;
			
		String fYear = fcNo.substring(0,2);
		String fBranch = fcNo.substring(2, 5);
		String fRNo = fcNo.substring(5);

		if(TextUtils.isDigitsOnly(fYear)&&TextUtils.isDigitsOnly(fRNo)&&fBranch.matches("^[ACEKLMP][EKR][B]$+")&&Integer.parseInt(fYear)<=getSmallYear()){
			return true;
		}else{
			return false;
		}
	}

	public static int getSmallYear(){
		return Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2));
	}
	
	public static int getMonth(){
		return Calendar.getInstance().get(Calendar.MONTH);
	}
	
	public static boolean isEnrolmentNumber(String enNo){
		if(enNo.length()!=6){
			return false;
		}
		String rgNo = enNo.substring(0,2);
		String RNo = enNo.substring(2);

		if(TextUtils.isDigitsOnly(RNo)&&rgNo.matches("^[FG][B-Z]$+")){
			return true;
		}else{
			return false;
		}
	}
}
