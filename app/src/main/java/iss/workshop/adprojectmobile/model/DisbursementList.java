package iss.workshop.adprojectmobile.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DisbursementList {

    private int id;
    private int departmentId;
    private String date;
    private String deliveryPoint;
    private String status;
    private String Department;
    private String repName;
//    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DisbursementList(int id, int departmentId, String date, String deliveryPoint, String status, String department) {
        this.id = id;
        this.departmentId = departmentId;
        this.date = date;
        this.deliveryPoint = deliveryPoint;
        this.status = status;
        Department = department;
        repName = null;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
//        this.date = LocalDateTime.parse(date, dtf);
        this.date = date;
    }

    public String getDeliveryPoint() {
        return deliveryPoint;
    }

    public void setDeliveryPoint(String deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    @Override
    public String toString() {
        return "DisbursementList{" +
                "id=" + id +
                ", DepartmentId=" + departmentId +
                ", date=" + date +
                ", deliveryPoint='" + deliveryPoint + '\'' +
                ", Department='" + Department + '\'' +
                '}';
    }
}
