package amu.areeb.zhcet.model;

import java.io.Serializable;

public class Result implements Serializable {
    public String course, course_name, grades,
            sessional_marks, exam_marks, total,
            highest, class_average, grace,
            subject_rank;

    public Result() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        if (!course.equals(result.course)) return false;
        if (!grades.equals(result.grades)) return false;
        if (!total.equals(result.total)) return false;
        return (grace.equals(result.grace)) && subject_rank.equals(result.subject_rank);

    }

    @Override
    public int hashCode() {
        int result = course.hashCode();
        result = 31 * result + grades.hashCode();
        result = 31 * result + total.hashCode();
        result = 31 * result + grace.hashCode();
        result = 31 * result + subject_rank.hashCode();
        return result;
    }
}
