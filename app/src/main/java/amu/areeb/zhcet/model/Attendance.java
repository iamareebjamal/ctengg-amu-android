package amu.areeb.zhcet.model;

import java.io.Serializable;

public class Attendance implements Serializable {

    /*"#FF6D00" /*Orange "#00E676" /*Green "#536DFE" /*Indigo* "#448AFF" /*Blue "#40C4FF" /*Light Blue*/
    public static final String[] COLORS = {"#FF5252" /*Red*/, "#FF4081" /*Pink*/, "#e040fb" /*Purple*/,
            "#00E5FF" /*Cyan*/,
            "#1DE9B6" /*Teal*/, "#00c853" /*Light Green*/, "#F9A825" /*Yellow*/,
            "#FF6E40" /*Deep Orange*/};
    private static final long serialVersionUID = 1L;
    public static String name;
    public String subject, attended, total, perc, remark, date;

    public Attendance() {
    }

    public Attendance(String subject, String attended, String total, String perc, String remark, String date) {
        this.subject = subject;
        this.attended = attended;
        this.total = total;
        this.perc = perc;
        this.remark = remark;
        this.date = date;
    }
}