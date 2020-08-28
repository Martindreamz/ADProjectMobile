package iss.workshop.adprojectmobile.model;

public class PurchaseOrderDetail {
    private int id;
    private int stationeryId;
    private int qty;
    private int purchaseOrderId;

    public PurchaseOrderDetail(int id, int stationeryId, int qty, int purchaseOrderId) {
        this.id = id;
        this.stationeryId = stationeryId;
        this.qty = qty;
        this.purchaseOrderId = purchaseOrderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationeryId() {
        return stationeryId;
    }

    public void setStationeryId(int stationeryId) {
        this.stationeryId = stationeryId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
}
