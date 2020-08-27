package iss.workshop.adprojectmobile.Interfaces;

import java.util.List;

import iss.workshop.adprojectmobile.model.DisbursementList;
import iss.workshop.adprojectmobile.model.Employee;
import iss.workshop.adprojectmobile.model.Requisition;
import iss.workshop.adprojectmobile.model.RequisitionDetail;
import iss.workshop.adprojectmobile.model.Stationery;
import iss.workshop.adprojectmobile.model.CollectionInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    String url = "https://10.0.2.2:5001/api/";

    @GET("store/retrieval/{id}")
    Call<List<RequisitionDetail>> getPendingRequisition(@Path("id") int id);

    @GET("Dept/deptEmp/{id}")
    Call<List<Employee>> GetAllEmployeesByDept(@Path("id") int id);

    @GET("store/stationeries")
    Call<List<Stationery>> getAllStationery();

    @GET("Dept/allCollectionpt")
    Call<List<CollectionInfo>> getAllCollectionPointforDept();

    @GET("store/disbursements/{id}")
    Call<List<DisbursementList>> getAllDisbursementLists(@Path("id") int id);

    @GET("store/departmentReps")
    Call<List<Employee>> getAllDepartmentReps();

    @Headers({"Content-Type: application/json"})
    @POST("store/getretrieval")
    Call<Requisition> sendRequisitionToProcess(@Body Requisition requisitions);

    @Headers({"Content-Type: application/json"})
    @POST("store/processRetrieval")
    Call<List<DisbursementList>> processRetrieval(@Body List<RequisitionDetail> requisitions);

    @Headers({"Content-Type: application/json"})
    @POST("login/post")
    Call<Employee> login(@Body Employee employee);

    @Headers({"Content-Type: application/json"})
    @POST("deptDelegate")
    Call<Employee> DeptDelegate(@Body Employee employee);
}
