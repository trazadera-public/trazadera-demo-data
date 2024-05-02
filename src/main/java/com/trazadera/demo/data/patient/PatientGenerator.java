package com.trazadera.demo.data.patient;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.trazadera.demo.data.Generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatientGenerator extends Generator {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void duplicate(Object object, Faker faker, Locale locale) {
        Patient p = (Patient)object;
        String oldId = p.getPatientId();
        p.setPatientId(faker.idNumber().valid());
        p.setBloodType(faker.options().option("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        p.setWeight(p.getWeight() + faker.number().numberBetween(-5, 15));
        p.setHeight(p.getHeight() + faker.number().numberBetween(-10, +10));
        p.setVisits(generateVisits(faker, faker.number().numberBetween(1, 3)));
        StringBuilder comments = new StringBuilder("Duplicate patient from " + oldId + ": changed blood type, weight, height, visits");
        int dice = faker.number().numberBetween(0, 5);
        switch (dice) {
            case 0 -> {
                p.setName(p.getName() + " " + faker.name().firstName());
                p.setSurname(phoneticMistakes(p.getSurname()));
                p.setEmail(faker.internet().emailAddress());
                comments.append(", name, surname, email");
            }
            case 1 -> {
                p.setSurname(p.getSurname() + " " + p.getSurname().toUpperCase());
                p.setPhone(faker.phoneNumber().phoneNumber());
                comments.append(", surname, phone");
            }
            case 2 -> {
                p.setPhone(validPhone(faker, locale));
                p.setEmail(validEmail(faker, p.getName(), p.getSurname()));
                comments.append(", phone, email");
            }
            case 3 -> {
                String name = p.getName();
                String surname = p.getSurname();
                p.setName(surname);
                p.setSurname(name);
                p.setCity(faker.address().city());
                p.setPostcode(faker.address().zipCode());
                comments.append(", name, surname, city, postcode");
            }
            case 4 -> {
                p.setPostcode(faker.address().zipCode());
                p.setAddress(faker.address().streetAddress());
                p.setCity(faker.address().city());
                p.setCountryCode(faker.address().countryCode());
                comments.append(", address, city, postcode, country");
            }
        }
        p.setComments(comments.toString());
    }

    @Override
    public void distort(Object object, Faker faker, Locale locale) {
        Patient p = (Patient)object;
        StringBuilder comments = new StringBuilder("Distorted patient: ");
        int count = faker.number().numberBetween(1, 5);
        for (int i=0; i<count; i++) {
            if (i > 0) comments.append(", ");
            int dice = faker.number().numberBetween(0, 12);
            switch (dice) {
                case 0 -> {
                    p.setPhone(invalidPhone(faker, p.getPhone()));
                    comments.append("phone");
                }
                case 1 -> {
                    p.setEmail(invalidEmail(faker, p.getEmail()));
                    comments.append("email");
                }
                case 2 -> {
                    p.setBloodType(faker.options().option("Z+", "X-", "X+", "Z-"));
                    comments.append("blood type");
                }
                case 3 -> {
                    p.setWeight(faker.number().numberBetween(-150, -50));
                    comments.append("weight");
                }
                case 4 -> {
                    p.setHeight(faker.number().numberBetween(-120, 0));
                    comments.append("height");
                }
                case 5 -> {
                    p.setBirthDate(dateFormat.format(faker.date().birthday(150, 200)));
                    comments.append("birth date");
                }
                case 6 -> {
                    p.setCountryCode(faker.letterify("??").toUpperCase());
                    comments.append("country code");
                }
                case 7 -> {
                    p.setEmail(p.getEmail() + "|" + faker.internet().emailAddress());
                    comments.append("email (appended)");
                }
                case 8 -> {
                    p.setPhone(p.getPhone() + "|" + faker.phoneNumber().phoneNumber());
                    comments.append("phone (appended)");
                }
                case 9 -> {
                    p.setAddress(null);
                    p.setPostcode(null);
                    p.setCity(null);
                    comments.append("address (null)");
                }
                case 10 -> {
                    p.setPhone("012012012");
                    comments.append("phone (placeholder)");
                }
                case 11 -> {
                    p.setEmail("foo@foo.com");
                    comments.append("email (placeholder)");
                }
            }
        }
        p.setComments(comments.toString());
    }

    @Override
    public Object generate(Faker faker, Locale locale) {
        Patient patient = new Patient();
        patient.setPatientId(faker.idNumber().valid());
        Name name = faker.name();
        patient.setName(name.firstName());
        patient.setSurname(name.lastName());
        patient.setLanguage(extractLanguage(locale));

        int height = faker.number().numberBetween(150, 220);
        patient.setHeight(height);
        patient.setWeight(calculateWeight(faker, height));

        Address address = faker.address();
        patient.setAddress(address.streetAddress());
        patient.setCity(address.city());
        patient.setPostcode(address.zipCode());
        patient.setCountryCode(address.countryCode());

        patient.setGender(faker.options().option("M", "F", "X"));
        patient.setBirthDate(dateFormat.format(faker.date().birthday()));

        // Faker phone and email do not work well with locale
        patient.setPhone(validPhone(faker, locale));
        patient.setEmail(validEmail(faker, patient.getName(), patient.getSurname()));

        patient.setBloodType(faker.options().option("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        patient.setComments("Fake patient generated by Trazadera Demo at " + Instant.now());

        List<Visit> visits = generateVisits(faker, faker.number().numberBetween(1, 5));
        patient.setVisits(visits);

        return patient;
    }


    // Private methods
    // =================================================================================================================


    private int calculateWeight(Faker faker, int height) {
//        Underweight: BMI less than 18.5
//        Normal weight: BMI 18.5 - 24.9
//        Overweight: BMI 25 - 29.9
//        Obese: BMI 30 or greater
        int bmi = faker.number().numberBetween(15, 40);
        return (int) Math.round(bmi * Math.pow(height / 100.0, 2));
    }

    private String extractLanguage(Locale locale) {
        String lang = locale.getLanguage();
        return (lang.contains("_") ?lang.substring(0, lang.indexOf("_")) :lang).toUpperCase();
    }

    private String phoneticMistakes(String token) {
        return token.replace("th", "ht")
                .replace("ç", "s")
                .replace("z", "s")
                .replace("tt", "t")
                .replace("ss", "s");
    }

    public static String cleanNonAscii(String input) {
        // Regular expression to match non-ASCII characters
        String regex = "[^\\x00-\\x7F]"; // Matches any character outside the ASCII range (0-127)
        // Replace non-ASCII characters with an empty string
        return input.replaceAll(regex, "");
    }

    private String validEmail(Faker faker, String name, String surname) {
        String domain = faker.internet().domainName();
        while (domain.contains("--")) {
            domain = faker.internet().domainName();
        }
        String nname = cleanNonAscii(name);
        String ssurname = cleanNonAscii(surname);
        int dice = faker.number().numberBetween(0, 4);
        return switch (dice) {
            case 0 -> nname + "." + ssurname + "@" + domain;
            case 1 -> nname + "_" + ssurname + "@" + domain;
            case 2 -> nname.substring(0,1) + ssurname + faker.date().birthday().getYear() + "@" + domain;
            case 3 -> faker.animal().name().replace(" ", "") + faker.number().numberBetween(0, 10000) + "@" + domain;
            default -> nname + ssurname + "@" + domain;
        };
    }

    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private String validPhone(Faker faker, Locale locale) {
        String country = getCountry(locale);
        Phonenumber.PhoneNumber pn = phoneNumberUtil.getExampleNumberForType(country, PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE);
        int code = pn.getCountryCode();
        String phone = faker.phoneNumber().phoneNumber();
        //System.out.println("Phone: " + phone);
        int prefixIndex = phone.indexOf("+");
        if (prefixIndex != -1) {
            int endPrefixIndex = phone.indexOf(" ", prefixIndex);
            if (endPrefixIndex != -1) {
                phone = "+" + code + " " + phone.substring(endPrefixIndex);
            }
        }
        //System.out.println("--> Phone: " + phone);
        return phone;
    }

    private String getCountry(Locale locale) {
        String full = locale.toString();
        int pos = full.indexOf("_");
        return ((pos > 0) ?full.substring(pos+1) :full).toUpperCase();
    }

    private String invalidEmail(Faker faker, String email) {
        int dice = faker.number().numberBetween(0, 10);
        return switch (dice) {
            case 0 -> email.replace("@", "\"");
            case 1 -> email.replace(".", ",");
            case 2 -> email + ".foo";
            case 3 -> faker.name().firstName() + " " + email;
            case 4 -> faker.name() + "@" + faker.internet().domainName() + "." + faker.internet().domainName();
            case 5 -> faker.name().firstName() + ":" + faker.name().lastName() + "@" + faker.internet().domainName();
            case 6 -> faker.letterify("????????") + "@" + faker.internet().domainSuffix();
            case 7 -> faker.name().firstName() + "@@" + faker.internet().domainName();
            case 8 -> faker.name().firstName() + "%" + faker.name().lastName() + "@" + faker.internet().domainName();
            case 9 -> faker.internet().emailAddress() + " " + faker.internet().emailAddress();
            default -> email.replace("@", " ").replace(".", " ");
        };
    }

    private String invalidPhone(Faker faker, String phone) {
        int dice = faker.number().numberBetween(0, 10);
        return switch (dice) {
            case 0 -> "0000";
            case 1 -> "123123123";
            case 2 -> "+000" + phone;
            case 3 -> "00" + phone;
            case 4 -> "+999" + phone;
            case 5 -> phone + "12312412412132";
            case 6 -> phone + faker.phoneNumber().extension();
            case 7 -> phone + " | " + faker.phoneNumber().phoneNumber();
            case 8 -> "(000) 000-0000";
            case 9 -> phone + "#" + faker.phoneNumber().extension();
            default -> "()" + phone;
        };
    }

    private List<Visit> generateVisits(Faker faker, int num) {
        List<Visit> visits = new ArrayList<>();
        for (int i=0; i<num; i++) {
            Visit visit = new Visit();
            visit.setVisitId(faker.idNumber().valid());
            visit.setDate(dateFormat.format(faker.date().past(3650, java.util.concurrent.TimeUnit.DAYS)));
            visit.setService(Service.random(faker));
            visit.setDiagnosis(faker.medical().diseaseName());
            visit.setPrescription(faker.medical().medicineName().toLowerCase());
            visit.setReason(faker.medical().symptoms());
            visits.add(visit);
        }
        return visits;
    }
}
