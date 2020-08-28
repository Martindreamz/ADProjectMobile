package iss.workshop.adprojectmobile.model;

public class Supplier {
    private int id;
    private String supplierCode;
    private String name;
    private String contactPerson;
    private String email;
    private String phoneNum;
    private String gstRegisNo;
    private String fax;
    private String address;
    public int priority;

    public Supplier(int id, String supplierCode, String name, String contactPerson, String email, String phoneNum, String gstRegisNo, String fax, String address, int priority) {
        this.id = id;
        this.supplierCode = supplierCode;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phoneNum = phoneNum;
        this.gstRegisNo = gstRegisNo;
        this.fax = fax;
        this.address = address;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getGstRegisNo() {
        return gstRegisNo;
    }

    public void setGstRegisNo(String gstRegisNo) {
        this.gstRegisNo = gstRegisNo;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
