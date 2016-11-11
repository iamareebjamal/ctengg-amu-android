package amu.areeb.zhcet.api;

import amu.areeb.zhcet.BuildConfig;
import amu.areeb.zhcet.model.StudentAttendance;
import amu.areeb.zhcet.model.StudentResult;
import amu.areeb.zhcet.utils.Utils;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentService {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://ctengg-api.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Call<StudentAttendance> getAttendanceCall(String fac_no){
        return retrofit.create(AttendanceAPI.class).loadAttendance(fac_no, Utils.decrypt(BuildConfig.API_KEY));
    }

    public static Call<StudentResult> getResultCall(String fac_no, String en_no){
        return retrofit.create(ResultAPI.class).loadResult(fac_no, en_no, Utils.decrypt(BuildConfig.API_KEY));
    }
}
