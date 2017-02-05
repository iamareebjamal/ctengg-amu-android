package amu.areeb.zhcet.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Calendar;

import amu.areeb.zhcet.model.StudentAttendance;
import amu.areeb.zhcet.model.StudentResult;

public class Utils {

    public static final String[] COLORS = {"#FF5252" /*Red*/, "#FF4081" /*Pink*/, "#e040fb" /*Purple*/,
            "#00E5FF" /*Cyan*/,
            "#1DE9B6" /*Teal*/, "#00c853" /*Light Green*/, "#F9A825" /*Yellow*/,
            "#FF6E40" /*Deep Orange*/};

    public static boolean isFacultyNumber(String fcNo) {
        if (fcNo.length() == 9)
            return true;
        else if (fcNo.length() != 8)
            return false;

        String fYear = fcNo.substring(0, 2);
        String fBranch = fcNo.substring(2, 5);
        String fRNo = fcNo.substring(5);

        return TextUtils.isDigitsOnly(fYear) && TextUtils.isDigitsOnly(fRNo) && fBranch.matches("^[ACEKLMP][EKR][B]$+") && Integer.parseInt(fYear) <= getSmallYear();
    }

    private static int getSmallYear() {
        return Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2));
    }

    private static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static boolean isEnrolmentNumber(String enNo) {
        if (enNo.length() != 6) {
            return false;
        }
        String rgNo = enNo.substring(0, 2);
        String RNo = enNo.substring(2);

        return  (TextUtils.isDigitsOnly(RNo) && rgNo.matches("^[FG][B-Z]$+"));
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

    private static Object load(Context context, String database) {
        Object object = null;
        try {
            FileInputStream fis = context.openFileInput(database);
            ObjectInputStream o = new ObjectInputStream(fis);
            object = o.readObject();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    private static void save(Context context, Object object, String database) {
        try {
            FileOutputStream fos = context.openFileOutput(database, Context.MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(fos);
            o.writeObject(object);
            fos.close();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static StudentAttendance loadAttendance(Context context) {
        return (StudentAttendance) load(context, "attendance.db");
    }

    public static StudentResult loadResult(Context context) {
        return (StudentResult) load(context, "result.db");
    }

    public static void saveAttendance(Context context, StudentAttendance studentAttendance) {
        save(context, studentAttendance, "attendance.db");
    }

    public static void saveResult(Context context, StudentResult studentResult) {
        save(context, studentResult, "result.db");
    }

    public static String decrypt(String obs) {
        return new String(Base64.decode(obs, Base64.DEFAULT));
    }
}
