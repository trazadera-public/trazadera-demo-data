package com.trazadera.demo.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.javafaker.Faker;
import com.trazadera.demo.data.patient.PatientGenerator;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public abstract class Generator {

    /**
     * Modify the duplicated object. Usually, the identifier is changed and some other attributes are distorted.
     * @param object Duplicated object. Already cloned, but identifier is the same.
     * @param faker Faker object.
     * @return Distorted object.
     */
    public abstract void duplicate(Object object, Faker faker, Locale locale);

    /**
     * Introduce some distortion in the object. The object is not cloned.
     * @param object Object to distort. Usually, data quality issues are introduced.
     * @param faker Faker object.
     * @return Distorted object.
     */
    public abstract void distort(Object object, Faker faker, Locale locale);

    /**
     * Generate an object.
     * @param faker Faker object.
     * @param locale Locale.
     * @return Generated object.
     */
    public abstract Object generate(Faker faker, Locale locale);

    public static void main(String[] args) throws Exception {

        // Parameters
        String entity = (args.length > 0) ?args[0] :"patient";
        int total = (args.length > 1) ?Integer.parseInt(args[1]) :5000;
        String[] ll = (args.length > 2) ?args[2].split(",") :new String[] { "es_ES", "fr_FR", "pt_PT" };
        String output = (args.length > 3) ?args[3] :"data/tz-" + entity + "-" + total + ".json";
        File outputFile = new File(output);
        double duplicateThreshold = (args.length > 4) ?Double.parseDouble(args[4]) :0.021;
        double qualityThreshold = (args.length > 5) ?Double.parseDouble(args[5]) :0.15;

        // Validation
        if (List.of("patient").contains(entity) == false)
            throw new IllegalArgumentException("Invalid entity: " + entity);
        if (total < 1)
            throw new IllegalArgumentException("Invalid total: " + total);
        if (ll.length < 1)
            throw new IllegalArgumentException("Invalid locales: " + Arrays.toString(ll));
        if (output.isBlank())
            throw new IllegalArgumentException("Invalid output: " + output);
        if (!outputFile.exists() && outputFile.createNewFile() && !outputFile.canWrite())
            throw new IllegalArgumentException("Cannot write to output: " + output);
        if (duplicateThreshold < 0.0 || duplicateThreshold > 1.0)
            throw new IllegalArgumentException("Invalid duplicate threshold: " + duplicateThreshold);
        if (qualityThreshold < 0.0 || qualityThreshold > 1.0)
            throw new IllegalArgumentException("Invalid quality threshold: " + qualityThreshold);
        System.out.println("Generating " + total + " " + entity + " objects with locales " + Arrays.toString(ll) + " to " + output + " (duplicate threshold " + duplicateThreshold + ", quality threshold " + qualityThreshold + ")");

        // Locales
        Locale[] locales = new Locale[ll.length];
        for (int i=0; i<ll.length; i++) {
            locales[i] = new Locale(ll[i]);
        }

        // Generator
        Generator generator = switch (entity) {
            case "patient" -> new PatientGenerator();
            default -> throw new IllegalArgumentException("Unexpected value: " + entity);
        };

        // Object generation
        Random random = new Random();
        Set<Object> objects = new HashSet<>();
        int distorts = 0;
        int duplicates = 0;
        while (objects.size() < total) {
            Locale locale = locales[objects.size() % locales.length];
            Faker faker = Faker.instance(locale);
            Object o = generator.generate(faker, locale);
            objects.add(o);
            double dice = random.nextDouble();
            if (dice < duplicateThreshold) {
                duplicates++;
                Object duplicated = SerializationUtils.clone((Serializable)o);
                objects.add(duplicated);
                generator.duplicate(duplicated, faker, locale);
            } else if (dice < qualityThreshold) {
                distorts++;
                generator.distort(o, faker, locale);
            }
            if (objects.size() % 100 == 0)
                System.out.println("Generated " + objects.size() + " objects (" + distorts + " distorts, " + duplicates + " duplicates)");
        }

        // Output
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(outputFile, objects);
        System.out.println("Generated " + objects.size() + " objects (" + distorts + " distorts, " + duplicates + " duplicates)");
        System.out.println("Output written to " + outputFile.getAbsolutePath());

    }
}
