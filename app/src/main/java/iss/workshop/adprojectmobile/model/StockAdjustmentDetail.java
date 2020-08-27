package iss.workshop.adprojectmobile.model;

import java.io.Serializable;

public class StockAdjustmentDetail implements Serializable {
    private int id;
    private int stockAdjustmentId;
    private int discpQty;
    public String comment;
    public String status;

    public StockAdjustmentDetail(int id, int stockAdjustmentId, int discpQty, String comment, String status){
        this.id=id;
        this.stockAdjustmentId=stockAdjustmentId;
        this.discpQty=discpQty;
        this.comment=comment;
        this.status=status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStockAdjustmentId() {
        return stockAdjustmentId;
    }

    public void setStockAdjustmentId(int stockAdjustmentId) {
        this.stockAdjustmentId = stockAdjustmentId;
    }

    public int getDiscpQty() {
        return discpQty;
    }

    public void setDiscpQty(int discpQty) {
        this.discpQty = discpQty;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
