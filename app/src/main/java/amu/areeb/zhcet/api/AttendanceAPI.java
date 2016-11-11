package amu.areeb.zhcet.api;

import amu.areeb.zhcet.model.StudentAttendance;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AttendanceAPI {
    @GET("/attendance/{fac_no}")
    Call<StudentAttendance> loadAttendance(@Path("fac_no") String fac_no, @Query("api_key") String api_key);
}
