package iss.workshop.adprojectmobile.Interfaces;

import java.util.List;

import iss.workshop.adprojectmobile.model.Department;
import iss.workshop.adprojectmobile.model.DisbursementDetail;
import iss.workshop.adprojectmobile.model.DisbursementList;
import iss.workshop.adprojectmobile.model.Employee;
import iss.workshop.adprojectmobile.model.PurchaseOrder;
import iss.workshop.adprojectmobile.model.PurchaseOrderDetail;
import iss.workshop.adprojectmobile.model.Requisition;
import iss.workshop.adprojectmobile.model.RequisitionDetail;
import iss.workshop.adprojectmobile.model.Stationery;
import iss.workshop.adprojectmobile.model.StockAdjustment;
import iss.workshop.adprojectmobile.model.StockAdjustmentDetail;
import iss.workshop.adprojectmobile.model.CollectionInfo;
import iss.workshop.adprojectmobile.model.Supplier;
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

    @GET("store/getAllPOs")
    Call<List<PurchaseOrder>> GetAllPos();

    @GET("store/getPOD/{id}")
    Call<List<PurchaseOrderDetail>> GetPurchaseOrderDetail(@Path("id") int id);

    @GET("store/Suppliers")
    Call<List<Supplier>> GetAllSuppliers();

    @GET("store/getSupplier/{id}")
    Call<Supplier> GetSupplier(@Path("id") int id);

    @GET("store/departmentReps")
    Call<List<Employee>> getAllDepartmentReps();

    @GET("store/disbursementdetail/{id}")
    Call<List<DisbursementDetail>> getAllDisbursementDetail(@Path("id") int id); //disbursement list ID

    @GET("store/disbursementlist/{id}")
    Call<DisbursementList> getDisbursementList(@Path("id") int id);

    @GET("dept/deptEmp/{id}")
    Call<List<Employee>> getAllEmployeesByDept(@Path("id") int id);

    @GET("Dept/{id}")
    Call<Department> getDepartmentById(@Path("id") int id);

    @GET("Dept/nearestDisbursementListByDept/{id}")
    Call<List<DisbursementList>>  getNearestDisbursementByDeptId(@Path("id") int id);

    @GET("Dept/disbursementDetailByDept/{id}")
    Call<List<DisbursementDetail>> getDisbursementDetailByDeptId(@Path("id") int id);

    @GET("Dept/deptToDeliverReqDetail/{id}")
    Call<List<RequisitionDetail>> getToDeliverRequisitionDetailByDeptId(@Path("id") int id);

    @GET("Dept/deptToDeliverReq/{id}")
    Call<List<Requisition>> getToDeliverRequisitionsByDeptId(@Path("id") int id);

    @GET("Dept/deptAllReq/{id}")
    Call<List<Requisition>> getAllRequisitionsByDeptId(@Path("id") int id);

    @GET("Dept/deptAllReqDetail/{id}")
    Call<List<RequisitionDetail>> getAllRequisitionsDetailByDeptId(@Path("id") int id);

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
    Call<List<StockAdjustmentDetail>>  updateInventory(@Body List<Stationery> stationeries);

    @Headers({"Content-Type: application/json"})
    @POST("store/stkAd/{id}")
    Call<StockAdjustment> PostTestStkAd(@Body List<StockAdjustmentDetail> stockAdjustmentDetails, @Path("id") int id);

    @Headers({"Content-Type: application/json"})
    @POST("store/receivedGoods/{id}")
    Call<StockAdjustment> PostReceivedGoods(@Body List<StockAdjustmentDetail> StockAdjustmentDetails, @Path("id") int id);


    @Headers({"Content-Type: application/json"})
    @POST("dept/deptDelegate/{id}")
    Call<Employee> DeptDelegate(@Body Employee employee,@Path("id")int id);

    @POST("dept/deptRevokeDelegate/{deptid}/{staffid}")
    Call<Employee> RevokeDelegate(@Path("deptid") int deptid,@Path("staffid") int staffid);

    @POST("dept/deptDelegate")
    Call<Employee> DeptDelegate(@Body Employee employee);

    @Headers({"Content-Type: application/json"})
    @POST("store/PORecieved/{id}")
    Call<PurchaseOrder> PORecieved(@Path("id")int id);

    @Headers({"Content-Type: application/json"})
    @POST("dept/confirmDisbursementByDept")
    Call<List<DisbursementDetail>> SendDisbursementDetail (@Body List<DisbursementDetail> updatedDisbursementDetailList);

    @Headers({"Content-Type: application/json"})
    @POST("store/updateDisbursementBitmap/{DLid}")
    Call<DisbursementList> updateDisbursementBitmap(@Body String bitmap, @Path("DLid") int id);
}
