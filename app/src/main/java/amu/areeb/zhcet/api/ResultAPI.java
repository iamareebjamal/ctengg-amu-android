package amu.areeb.zhcet.api;

import amu.areeb.zhcet.model.StudentResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ResultAPI {
    @GET("/result/btech")
    Call<StudentResult> loadResult(@Query("fac") String fac_no, @Query("en") String en_no, @Query("api_key") String api_key);
}
