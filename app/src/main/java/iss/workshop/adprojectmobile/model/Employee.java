package iss.workshop.adprojectmobile.model;

public class Employee {

    //[Required]
    private int id;
    private String name;
    private String password;
    private String email;
    private String role;
    private String phoneNum;
    private int departmentId;
//    private Department department;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public Employee(String email,String password) {
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "Id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
