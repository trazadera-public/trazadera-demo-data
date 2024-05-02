package com.trazadera.demo.data.patient;

import java.io.Serializable;

public class Visit implements Serializable {
    private String visitId;
    private String date;
    private Service service;
    private String reason;
    private String diagnosis;
    private String prescription;

    public String getVisitId() {
        return visitId;
    }

    public Visit setVisitId(String visitId) {
        this.visitId = visitId;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Visit setDate(String date) {
        this.date = date;
        return this;
    }

    public Service getService() {
        return service;
    }

    public Visit setService(Service service) {
        this.service = service;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public Visit setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public Visit setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public String getPrescription() {
        return prescription;
    }

    public Visit setPrescription(String prescription) {
        this.prescription = prescription;
        return this;
    }

}
