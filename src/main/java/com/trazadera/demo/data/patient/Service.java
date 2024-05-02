package com.trazadera.demo.data.patient;

import com.github.javafaker.Faker;

import java.io.Serializable;

public class Service implements Serializable {

    private static String[] MEDICAL_SERVICES = {
            "Vaccination",
            "Blood tests",
            "X-ray imaging",
            "Ultrasound scan",
            "MRI scan",
            "Physical therapy",
            "Dental cleaning",
            "Eye examination",
            "Colonoscopy",
            "Mammography",
            "Cardiac stress test",
            "Electrocardiogram (ECG)",
            "Endoscopy",
            "Lung function test",
            "Allergy testing",
            "Skin biopsy",
            "Chiropractic adjustment",
            "Psychiatric evaluation",
            "Occupational therapy",
            "Speech therapy",
            "Dietary counseling",
            "Drug rehabilitation",
            "Wound care",
            "Cast application",
            "Acupuncture",
            "Hearing test",
            "Prenatal care",
            "Childbirth assistance",
            "Neonatal care",
            "Cataract surgery",
            "Dialysis",
            "Chemotherapy",
            "Radiation therapy",
            "Orthopedic surgery",
            "Appendectomy",
            "Hernia repair",
            "Gastric bypass surgery",
            "Hip replacement",
            "Knee arthroscopy",
            "Coronary angioplasty",
            "Stent placement",
            "Cesarean section",
            "Laparoscopic surgery",
            "Endoscopic sinus surgery",
            "Tonsillectomy",
            "Root canal treatment",
            "Dental implant surgery",
            "Orthodontic treatment",
            "Cosmetic surgery"
    };

    private String serviceId;
    private String description;

    public String getServiceId() {
        return serviceId;
    }

    public Service setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Service setDescription(String description) {
        this.description = description;
        return this;
    }

    public static Service random(Faker faker) {
        int num = faker.number().numberBetween(0, MEDICAL_SERVICES.length);
        Service service = new Service();
        service.setServiceId(String.format("%05d", num));
        service.setDescription(MEDICAL_SERVICES[num]);
        return service;
    }


}
