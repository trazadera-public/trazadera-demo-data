package com.trazadera.demo.data.patient;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Patient implements Serializable, Comparable<Patient> {
    private String patientId;
    private String name;
    private String surname;
    private String address;
    private String city;
    private String gender;
    private String language;
    private String postcode;
    private String countryCode;
    private String birthDate;
    private String phone;
    private String email;
    private String bloodType;
    private int weight;
    private int height;
    private String comments;
    private List<Visit> visits;

    public String getPatientId() {
        return patientId;
    }

    public Patient setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Patient setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public Patient setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Patient setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Patient setCity(String city) {
        this.city = city;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Patient setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getPostcode() {
        return postcode;
    }

    public Patient setPostcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Patient setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public Patient setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Patient setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Patient setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getBloodType() {
        return bloodType;
    }

    public Patient setBloodType(String bloodType) {
        this.bloodType = bloodType;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public Patient setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Patient setHeight(int height) {
        this.height = height;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public Patient setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public Patient setVisits(List<Visit> visits) {
        this.visits = visits;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public Patient setLanguage(String language) {
        this.language = language;
        return this;
    }

    // Overrides
    // ====================================================================================================

    @Override
    public int compareTo(Patient o) {
        return Objects.compare(this.patientId, o.patientId, String::compareTo);
    }

}
