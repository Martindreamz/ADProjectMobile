package iss.workshop.adprojectmobile.Interfaces;

import java.util.List;

import iss.workshop.adprojectmobile.model.Requisition;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String url = "https://10.0.2.2:5001/api/";

    @GET("dept/retrieval")
    Call<List<Requisition>> getPendingRequisition();
}
