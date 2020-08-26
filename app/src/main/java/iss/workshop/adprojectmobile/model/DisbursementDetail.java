package iss.workshop.adprojectmobile.model;

public class DisbursementDetail {

    private int id;
    private int disbursementListId;
    private int requisitionDetailId;
    private int qty;
    private String disbursementList;
    private String requisitionDetail;

    public DisbursementDetail(int id, int disbursementListId, int requisitionDetailId, int qty, String disbursementList, String requisitionDetail) {
        this.id = id;
        this.disbursementListId = disbursementListId;
        this.requisitionDetailId = requisitionDetailId;
        this.qty = qty;
        this.disbursementList = disbursementList;
        this.requisitionDetail = requisitionDetail;
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

    @Override
    public String toString() {
        return "DisbursementDetail{" +
                "id=" + id +
                ", DisbursementListId=" + disbursementListId +
                ", RequisitionDetailId=" + requisitionDetailId +
                ", qty=" + qty +
                ", DisbursementList='" + disbursementList + '\'' +
                ", RequisitionDetail='" + requisitionDetail + '\'' +
                '}';
    }
}
