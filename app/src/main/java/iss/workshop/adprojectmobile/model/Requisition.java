package iss.workshop.adprojectmobile.model;

public class Requisition {

    private int id;
    private int employeeId;
    private String dateOfRequest;
    private String dateOfAuthorizing;
    private int authorizerId;
    private String status;
    private String comment;
    private String employee;
    private String authorizer;

    public Requisition(int id, int employeeId, String dateOfRequest, String dateOfAuthorizing, int authorizerId, String status, String comment, String employee, String authorizer) {
        this.id = id;
        this.employeeId = employeeId;
        this.dateOfRequest = dateOfRequest;
        this.dateOfAuthorizing = dateOfAuthorizing;
        this.authorizerId = authorizerId;
        this.status = status;
        this.comment = comment;
        this.employee = employee;
        this.authorizer = authorizer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(String dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public String getDateOfAuthorizing() {
        return dateOfAuthorizing;
    }

    public void setDateOfAuthorizing(String dateOfAuthorizing) {
        this.dateOfAuthorizing = dateOfAuthorizing;
    }

    public int getAuthorizerId() {
        return authorizerId;
    }

    public void setAuthorizerId(int authorizerId) {
        this.authorizerId = authorizerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(String authorizer) {
        this.authorizer = authorizer;
    }

    @Override
    public String toString() {
        return "Requisition{" +
                "id='" + id + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", dateOfRequest='" + dateOfRequest + '\'' +
                ", dateOfAuthorizing='" + dateOfAuthorizing + '\'' +
                ", authorizerId='" + authorizerId + '\'' +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                ", employee='" + employee + '\'' +
                ", authorizer='" + authorizer + '\'' +
                '}';
    }
}
