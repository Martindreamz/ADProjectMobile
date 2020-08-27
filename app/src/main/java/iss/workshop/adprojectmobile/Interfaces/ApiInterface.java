package iss.workshop.adprojectmobile.Interfaces;

import java.util.List;

import iss.workshop.adprojectmobile.model.DisbursementDetail;
import iss.workshop.adprojectmobile.model.DisbursementList;
import iss.workshop.adprojectmobile.model.Employee;
import iss.workshop.adprojectmobile.model.Requisition;
import iss.workshop.adprojectmobile.model.RequisitionDetail;
import iss.workshop.adprojectmobile.model.Stationery;
import iss.workshop.adprojectmobile.model.StockAdjustmentDetail;
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

    @GET("store/retrieval/{id}") //store/retrieval/15
    Call<List<RequisitionDetail>> getPendingRequisition(@Path("id") int id);

    @GET("Dept/deptEmp/{id}")
    Call<List<Employee>> GetAllEmployeesByDept(@Path("id") int id);

    @GET("store/stationeries")
    Call<List<Stationery>> getAllStationery();

    @GET("Dept/allCollectionpt")
    Call<List<CollectionInfo>> getAllCollectionPointforDept();

    @GET("store/disbursements/{id}")
    Call<List<DisbursementList>> getAllDisbursementLists(@Path("id") int id); //find by clerk ID

    @GET("store/departmentReps")
    Call<List<Employee>> getAllDepartmentReps();

    @GET("store/disbursementdetail/{id}")
    Call<List<DisbursementDetail>> getAllDisbursementDetail(@Path("id") int id); //disbursement list ID

    @GET("store/disbursementlist/{id}")
    Call<DisbursementList> getDisbursementList(@Path("id") int id);

    @GET("dept/deptEmp/{id}")
    Call<List<Employee>> getAllEmployeesByDept(@Path("id") int id);


    @Headers({"Content-Type: application/json"})
    @POST("store/getretrieval")
    Call<Requisition> sendRequisitionToProcess(@Body Requisition requisitions);

    @Headers({"Content-Type: application/json"})
    @POST("store/processRetrieval/{id}/{year}/{month}/{day}")
    Call<List<DisbursementList>> processRetrieval(@Body List<RequisitionDetail> requisitions, @Path("id") int id, @Path("year") int year, @Path("month") int month, @Path("day") int day);
    @Headers({"Content-Type: application/json"})
    @POST("login/post")
    Call<Employee> login(@Body Employee employee);

    @Headers({"Content-Type: application/json"})
    @POST("store/updateInventory")
    Call<StockAdjustmentDetail>  updateInventory(@Body List<Stationery> stationeries);


    @Headers({"Content-Type: application/json"})
    @POST("deptDelegate")
    Call<Employee> DeptDelegate(@Body Employee employee);
}
