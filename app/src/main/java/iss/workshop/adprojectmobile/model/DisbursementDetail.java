package iss.workshop.adprojectmobile.model;

public class DisbursementDetail {

    private int id;
    private int DisbursementListId;
    private int RequisitionDetailId;
    private int qty;
    private String DisbursementList;
    private String RequisitionDetail;

    public DisbursementDetail(int id, int disbursementListId, int requisitionDetailId, int qty, String disbursementList, String requisitionDetail) {
        this.id = id;
        DisbursementListId = disbursementListId;
        RequisitionDetailId = requisitionDetailId;
        this.qty = qty;
        DisbursementList = disbursementList;
        RequisitionDetail = requisitionDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDisbursementListId() {
        return DisbursementListId;
    }

    public void setDisbursementListId(int disbursementListId) {
        DisbursementListId = disbursementListId;
    }

    public int getRequisitionDetailId() {
        return RequisitionDetailId;
    }

    public void setRequisitionDetailId(int requisitionDetailId) {
        RequisitionDetailId = requisitionDetailId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDisbursementList() {
        return DisbursementList;
    }

    public void setDisbursementList(String disbursementList) {
        DisbursementList = disbursementList;
    }

    public String getRequisitionDetail() {
        return RequisitionDetail;
    }

    public void setRequisitionDetail(String requisitionDetail) {
        RequisitionDetail = requisitionDetail;
    }

    @Override
    public String toString() {
        return "DisbursementDetail{" +
                "id=" + id +
                ", DisbursementListId=" + DisbursementListId +
                ", RequisitionDetailId=" + RequisitionDetailId +
                ", qty=" + qty +
                ", DisbursementList='" + DisbursementList + '\'' +
                ", RequisitionDetail='" + RequisitionDetail + '\'' +
                '}';
    }
}
