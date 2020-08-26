package iss.workshop.adprojectmobile.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DisbursementList {

    private int id;
    private int DepartmentId;
    private String date;
    private String deliveryPoint;
    private String Department;
//    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DisbursementList(int id, int departmentId, String date, String deliveryPoint, String department) {
        this.id = id;
        DepartmentId = departmentId;
        this.date = date;
        this.deliveryPoint = deliveryPoint;
        Department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(int departmentId) {
        DepartmentId = departmentId;
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
                ", DepartmentId=" + DepartmentId +
                ", date=" + date +
                ", deliveryPoint='" + deliveryPoint + '\'' +
                ", Department='" + Department + '\'' +
                '}';
    }
}
