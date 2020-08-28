package iss.workshop.adprojectmobile.model;

public class RequisitionDetail {

    private int id;
    private int requisitionId;
    private int stationeryId;
    private int reqQty;
    private int rcvQty;
    private String status;
    private String requisition;
    private String stationery;
    private String stationeryDesc;
    private String employeeName;

    public RequisitionDetail(int id, int requisitionId, int stationeryId, int reqQty, int rcvQty, String status, String requisition, String stationery, String employeeName) {
        this.id = id;
        this.requisitionId = requisitionId;
        this.stationeryId = stationeryId;
        this.reqQty = reqQty;
        this.rcvQty = rcvQty;
        this.status = status;
        this.requisition = requisition;
        this.stationery = stationery;
        this.stationeryDesc = null;
        this.employeeName = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(int requisitionId) {
        this.requisitionId = requisitionId;
    }

    public int getStationeryId() {
        return stationeryId;
    }

    public void setStationeryId(int stationeryId) {
        this.stationeryId = stationeryId;
    }

    public int getReqQty() {
        return reqQty;
    }

    public void setReqQty(int reqQty) {
        this.reqQty = reqQty;
    }

    public int getRcvQty() {
        return rcvQty;
    }

    public void setRcvQty(int rcvQty) {
        this.rcvQty = rcvQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequisition() {
        return requisition;
    }

    public void setRequisition(String requisition) {
        this.requisition = requisition;
    }

    public String getStationery() {
        return stationery;
    }

    public void setStationery(String stationery) {
        this.stationery = stationery;
    }

    public String getStationeryDesc() { return stationeryDesc; }

    public void setStationeryDesc(String stationeryDesc) { this.stationeryDesc = stationeryDesc; }

    public String getEmployeeName() { return employeeName; }

    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    @Override
    public String toString() {
        return "RequisitionDetail{" +
                "id=" + id +
                ", requisitionId=" + requisitionId +
                ", stationeryId=" + stationeryId +
                ", reqQty=" + reqQty +
                ", rcvQty=" + rcvQty +
                ", status='" + status + '\'' +
                ", requisition='" + requisition + '\'' +
                ", stationery='" + stationery + '\'' +
                '}';
    }
}
