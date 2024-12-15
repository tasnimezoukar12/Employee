package com.example.employee.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class employee {

    private final StringProperty employeeId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty gender;
    private final StringProperty phone;
    private final StringProperty position;
    private final StringProperty dateMember;
    private byte[] imageData;

    // Constructor
    public employee(String employeeId, String firstName, String lastName, String gender, String phone, String position, String dateMember,   byte[] imageData) {
        this.employeeId = new SimpleStringProperty(employeeId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.gender = new SimpleStringProperty(gender);
        this.phone = new SimpleStringProperty(phone);
        this.position = new SimpleStringProperty(position);
        this.dateMember = new SimpleStringProperty(dateMember);
        this.imageData = imageData;
    }

    // Getters and Setters for observable properties
    public String getEmployeeId() {
        return employeeId.get();
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId.set(employeeId);
    }

    public StringProperty employeeIdProperty() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public String getPosition() {
        return position.get();
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public StringProperty positionProperty() {
        return position;
    }

    public String getDateMember() {
        return dateMember.get();
    }

    public void setDateMember(String dateMember) {
        this.dateMember.set(dateMember);
    }

    public StringProperty dateMemberProperty() {
        return dateMember;
    }
    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
