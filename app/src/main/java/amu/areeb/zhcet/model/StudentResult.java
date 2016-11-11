package amu.areeb.zhcet.model;

import java.io.Serializable;
import java.util.List;

public class StudentResult implements Serializable {
    public String name, faculty_number, enrolment, spi, cpi, ec, message;
    public List<Result> results;
    public Boolean error;

    public StudentResult(){ }
}
