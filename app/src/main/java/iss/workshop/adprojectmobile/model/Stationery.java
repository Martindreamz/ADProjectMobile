package iss.workshop.adprojectmobile.model;

public class Stationery {

    private int id;
    private String category;
    private String desc;
    private String unit;
    private int reOrderQty;
    private int reOrderLevel;
    private int inventoryQty;

    public Stationery(int id, String category, String desc, String unit, int reOrderQty, int reOrderLevel, int inventoryQty) {
        this.id = id;
        this.category = category;
        this.desc = desc;
        this.unit = unit;
        this.reOrderQty = reOrderQty;
        this.reOrderLevel = reOrderLevel;
        this.inventoryQty = inventoryQty;
    }

    public Stationery() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getReOrderQty() {
        return reOrderQty;
    }

    public void setReOrderQty(int reOrderQty) {
        this.reOrderQty = reOrderQty;
    }

    public int getReOrderLevel() {
        return reOrderLevel;
    }

    public void setReOrderLevel(int reOrderLevel) {
        this.reOrderLevel = reOrderLevel;
    }

    public int getInventoryQty() {
        return inventoryQty;
    }

    public void setInventoryQty(int inventoryQty) {
        this.inventoryQty = inventoryQty;
    }
}
