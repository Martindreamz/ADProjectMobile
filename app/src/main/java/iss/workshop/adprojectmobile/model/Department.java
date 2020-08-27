package iss.workshop.adprojectmobile.model;

public class Department {

    private int id;
    private String deptName;
    private String deptCode;
    private String delgtStartDate;
    private String delgtEndDate;
    private String CollectionId;

    public Department(int id, String deptName, String deptCode, String delgtStartDate, String delgtEndDate, String CollectionId) {
        this.id = id;
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.delgtStartDate = delgtStartDate;
        this.delgtEndDate = delgtEndDate;
        this.CollectionId = CollectionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDelgtStartDate() {
        return delgtStartDate;
    }

    public void setDelgtStartDate(String delgtStartDate) {
        this.delgtStartDate = delgtStartDate;
    }

    public String getDelgtEndDate() {
        return delgtEndDate;
    }

    public void setDelgtEndDate(String delgtEndDate) {
        this.delgtEndDate = delgtEndDate;
    }

    public String getCollectionId() {
        return CollectionId;
    }

    public void setCollectionId(String collectionId) {
        CollectionId = collectionId;
    }
}
