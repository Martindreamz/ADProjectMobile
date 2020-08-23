package iss.workshop.adprojectmobile.Interfaces;

import java.util.List;

import iss.workshop.adprojectmobile.model.Requisition;
import iss.workshop.adprojectmobile.model.RequisitionDetail;
import iss.workshop.adprojectmobile.model.Stationery;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    String url = "https://10.0.2.2:5001/api/";

    @GET("store/retrieval")
    Call<List<RequisitionDetail>> getPendingRequisition();

    @GET("store/stationeries")
    Call<List<Stationery>> getAllStationery();

    @Headers({"Content-Type: application/json"})
    @POST("store/getretrieval")
    Call<Requisition> sendRequisitionToProcess(@Body Requisition requisitions);

    @Headers({"Content-Type: application/json"})
    @POST("store/processRetrieval")
    Call<List<RequisitionDetail>> processRetrieval(@Body List<RequisitionDetail> requisitions);


}
