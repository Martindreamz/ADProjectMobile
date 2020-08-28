package iss.workshop.adprojectmobile.model;

import java.util.Date;

public class PurchaseOrder {
    private int id;
    private int clerkId;
    private int supplierId;
    private Date dateOfOrder;
    private String status;
    private int stockAdjustmentId;

    public PurchaseOrder(int id, int clerkId, int supplierId, Date dateOfOrder, String status, int stockAdjustmentId) {
        this.id = id;
        this.clerkId = clerkId;
        this.supplierId = supplierId;
        this.dateOfOrder = dateOfOrder;
        this.status = status;
        this.stockAdjustmentId = stockAdjustmentId;
    }

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

    public Date getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(Date dateOfOrder) {
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
}
