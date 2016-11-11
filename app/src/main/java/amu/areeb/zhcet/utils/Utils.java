package amu.areeb.zhcet.utils;

import amu.areeb.zhcet.attendance.AttendanceGetter;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;

public class Utils {
    public static boolean isFacultyNumber(String fcNo) {
        if (fcNo.length() == 9)
            return true;
        else if (fcNo.length() != 8)
            return false;

        String fYear = fcNo.substring(0, 2);
        String fBranch = fcNo.substring(2, 5);
        String fRNo = fcNo.substring(5);

        if (TextUtils.isDigitsOnly(fYear) && TextUtils.isDigitsOnly(fRNo) && fBranch.matches("^[ACEKLMP][EKR][B]$+") && Integer.parseInt(fYear) <= getSmallYear()) {
            return true;
        } else {
            return false;
        }
    }

    public static int getSmallYear() {
        return Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2));
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static boolean isEnrolmentNumber(String enNo) {
        if (enNo.length() != 6) {
            return false;
        }
        String rgNo = enNo.substring(0, 2);
        String RNo = enNo.substring(2);

        if (TextUtils.isDigitsOnly(RNo) && rgNo.matches("^[FG][B-Z]$+")) {
            return true;
        } else {
            return false;
        }
    }

    public static String getDetail(String facNo) {
        String streams[] = {"PEB", "Computer", "LEB", "Electronics", "EEB", "Electrical", "MEB", "Mechanical", "CEB", "Civil", "KEB", "Chemical", "PKB", "Petrochemical"};
        String stream = facNo.substring(2, 5);
        int index = Arrays.asList(streams).indexOf(stream);
        stream = index == -1 ? stream : streams[index + 1];
        int year = Utils.getSmallYear() - Integer.parseInt(facNo.substring(0, 2));

        if (Utils.getMonth() > Calendar.AUGUST)
            year++;
        String post = "st";

        switch (year) {
            case 1:
                post = "st";
                break;
            case 2:
                post = "nd";
                break;
            case 3:
                post = "rd";
                break;
            case 4:
                post = "th";
        }

        return facNo + " " + year + post + " Year" + " " + stream;
    }

    public static AttendanceGetter load(Context context) {
        AttendanceGetter aGetter = null;
        try {
            FileInputStream fis = context.openFileInput("attendance.db");
            ObjectInputStream o = new ObjectInputStream(fis);
            aGetter = (AttendanceGetter) o.readObject();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return aGetter;
    }

    public static void save(Context context, AttendanceGetter aGetter) {
        try {
            FileOutputStream fos = context.openFileOutput("attendance.db", Context.MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(fos);
            o.writeObject(aGetter);
            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {

        }

    }
}
