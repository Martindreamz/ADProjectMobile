package iss.workshop.adprojectmobile.model;

import java.util.Date;
import java.util.List;

public class PurchaseOrder {
    private int id;
    private int clerkId;
    private int supplierId;
    private String dateOfOrder;
    private String status;
    private int stockAdjustmentId;
    private List<PurchaseOrderDetail> DetailList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(String dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStockAdjustmentId() {
        return stockAdjustmentId;
    }

    public void setStockAdjustmentId(int stockAdjustmentId) {
        this.stockAdjustmentId = stockAdjustmentId;
    }

    public List<PurchaseOrderDetail> getDetailList() {
        return DetailList;
    }

    public void setDetailList(List<PurchaseOrderDetail> detailList) {
        DetailList = detailList;
    }
}
