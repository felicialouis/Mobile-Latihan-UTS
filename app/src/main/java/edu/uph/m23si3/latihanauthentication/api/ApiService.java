package edu.uph.m23si3.latihanauthentication.api;

import edu.uph.m23si3.latihanauthentication.model.Kota;
import edu.uph.m23si3.latihanauthentication.model.Provinsi;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/provinces.json")
    Call<ApiResponse<Provinsi>> getProvinsi();

    @GET("api/regencies/{province_code}.json")
    Call<ApiResponse<Kota>> getKota(@Path("province_code") String provinceCode);
}
