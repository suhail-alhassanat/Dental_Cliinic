package com.suhail.dentalcliinic.models;

import java.util.ArrayList;
import java.util.Date;

public class Doctor {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private int identityNumber;
    private String membershipNumber;
    private String specialization;
    private String imageUrl;
    private String department;
    private ArrayList<String> workDays;
    private ArrayList<String> workHours;
    private Date hiringDate;
    private Date endDate;

    public Doctor(String name, String email, String phone, String address, String gender, int identityNumber, String membershipNumber, String specialization, String imageUrl, String department, ArrayList<String> workDays, ArrayList<String> workHours, Date hiringDate, Date endDate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.identityNumber = identityNumber;
        this.membershipNumber = membershipNumber;
        this.specialization = specialization;
        this.imageUrl = imageUrl;
        this.department = department;
        this.workDays = workDays;
        this.workHours = workHours;
        this.hiringDate = hiringDate;
        this.endDate = endDate;
    }

    public Doctor(String name, String email, String phone, String address, String gender, int identityNumber, String membershipNumber, String specialization, String department, ArrayList<String> workDays, ArrayList<String> workHours, Date hiringDate, Date endDate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.identityNumber = identityNumber;
        this.membershipNumber = membershipNumber;
        this.specialization = specialization;
        this.department = department;
        this.workDays = workDays;
        this.workHours = workHours;
        this.hiringDate = hiringDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(int identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ArrayList<String> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(ArrayList<String> workDays) {
        this.workDays = workDays;
    }

    public ArrayList<String> getWorkHours() {
        return workHours;
    }

    public void setWorkHours(ArrayList<String> workHours) {
        this.workHours = workHours;
    }

    public Date getHiringDate() {
        return hiringDate;
    }

    public void setHiringDate(Date hiringDate) {
        this.hiringDate = hiringDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
