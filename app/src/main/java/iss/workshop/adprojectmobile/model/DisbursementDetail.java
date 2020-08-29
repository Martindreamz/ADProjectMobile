package iss.workshop.adprojectmobile.model;

public class DisbursementDetail {

    private int id;
    private int disbursementListId;
    private int requisitionDetailId;
    private int qty;
    private String disbursementList;
    private String requisitionDetail;
    private int StationeryId;
    private String StationeryDesc;
    private String requestedEmp;
    private String deliveryPoint;
    private String bitmap;

    public DisbursementDetail(int id, int disbursementListId, int requisitionDetailId, int qty, String disbursementList, String requisitionDetail, String requestedEmp, String deliveryPoint, String bitmap) {
        this.id = id;
        this.disbursementListId = disbursementListId;
        this.requisitionDetailId = requisitionDetailId;
        this.qty = qty;
        this.disbursementList = disbursementList;
        this.requisitionDetail = requisitionDetail;
        this.StationeryId = 0;
        this.StationeryDesc = null;
        this.requestedEmp = null;
        this.deliveryPoint = null;
        this.bitmap=bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDisbursementListId() {
        return disbursementListId;
    }

    public void setDisbursementListId(int disbursementListId) {
        this.disbursementListId = disbursementListId;
    }

    public int getRequisitionDetailId() {
        return requisitionDetailId;
    }

    public void setRequisitionDetailId(int requisitionDetailId) {
        this.requisitionDetailId = requisitionDetailId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDisbursementList() {
        return disbursementList;
    }

    public void setDisbursementList(String disbursementList) {
        this.disbursementList = disbursementList;
    }

    public String getRequisitionDetail() {
        return requisitionDetail;
    }

    public void setRequisitionDetail(String requisitionDetail) {
        this.requisitionDetail = requisitionDetail;
    }

    public int getStationeryId() {
        return StationeryId;
    }

    public void setStationeryId(int stationeryId) {
        this.StationeryId = stationeryId;
    }

    public String getStationeryDesc() {
        return StationeryDesc;
    }

    public void setStationeryDesc(String stationeryDesc) {
        this.StationeryDesc = stationeryDesc;
    }

    public String getRequestedEmp() { return requestedEmp; }

    public void setRequestedEmp(String requestedEmp) { this.requestedEmp = requestedEmp; }

    public String getDeliveryPoint() { return deliveryPoint; }

    public void setDeliveryPoint(String deliveryPoint) { this.deliveryPoint = deliveryPoint; }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "DisbursementDetail{" +
                "id=" + id +
                ", disbursementListId=" + disbursementListId +
                ", requisitionDetailId=" + requisitionDetailId +
                ", qty=" + qty +
                ", disbursementList='" + disbursementList + '\'' +
                ", requisitionDetail='" + requisitionDetail + '\'' +
                ", StationeryId=" + StationeryId +
                ", StationeryDesc='" + StationeryDesc + '\'' +
                ", requestedEmp='" + requestedEmp + '\'' +
                ", deliveryPoint='" + deliveryPoint + '\'' +
                '}';
    }
}
