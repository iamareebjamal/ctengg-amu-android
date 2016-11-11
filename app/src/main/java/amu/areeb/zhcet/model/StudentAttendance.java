package amu.areeb.zhcet.model;

import java.io.Serializable;
import java.util.List;

public class StudentAttendance implements Serializable {

    public String name, fac, user, message;
    public Boolean error;
    public List<Attendance> attendance;

    public StudentAttendance(){}
}
