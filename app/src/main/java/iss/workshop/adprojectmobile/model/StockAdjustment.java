package iss.workshop.adprojectmobile.model;

import java.util.Date;

public class StockAdjustment {
    private int id;
    private String type;
    private Date date;
    private int employeeId;

    public StockAdjustment(int id, String type, Date date, int employeeId) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.employeeId = employeeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
