package com.mintitv.cli.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintitv.api.models.TechnicalCardData;
import com.mintitv.api.models.TechnicalCardFieldDescriptions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Utility class for printing Technical Card data with Spanish field descriptions.
 * Uses reflection to dynamically match field values with their descriptions.
 */
public class TechnicalCardPrinter {
    
    private static final String SECTION_SEPARATOR = "=";
    private static final int SECTION_WIDTH = 60;
    private static final String NOT_AVAILABLE = "N/D";
    
    private static final Map<String, String> SECTION_NAMES = new LinkedHashMap<>();
    
    static {
        // Define section names and order
        SECTION_NAMES.put("", "Datos Generales");
        SECTION_NAMES.put("A", "Fabricante del Vehículo");
        SECTION_NAMES.put("B", "Fabricante del Vehículo Completado");
        SECTION_NAMES.put("C", "Datos ITV");
        SECTION_NAMES.put("D", "Identificación del Vehículo");
        SECTION_NAMES.put("E", "Número de Identificación");
        SECTION_NAMES.put("F", "Masas y Dimensiones");
        SECTION_NAMES.put("G", "Masa en Orden de Marcha");
        SECTION_NAMES.put("J", "Categoría del Vehículo");
        SECTION_NAMES.put("K", "Homologación");
        SECTION_NAMES.put("Κ", "Homologación");
        SECTION_NAMES.put("L", "Ejes y Ruedas");
        SECTION_NAMES.put("M", "Distancia entre Ejes");
        SECTION_NAMES.put("O", "Capacidad de Remolque");
        SECTION_NAMES.put("P", "Motor");
        SECTION_NAMES.put("Q", "Relación Potencia/Masa");
        SECTION_NAMES.put("R", "Color");
        SECTION_NAMES.put("S", "Plazas");
        SECTION_NAMES.put("T", "Velocidad Máxima");
        SECTION_NAMES.put("U", "Nivel Sonoro");
        SECTION_NAMES.put("V", "Emisiones");
        SECTION_NAMES.put("Z", "Año y Serie");
        SECTION_NAMES.put("EP", "Estructura de Protección");
    }
    
    /**
     * Prints the complete technical card data with Spanish descriptions.
     *
     * @param data the technical card data to print
     */
    public static void printTechnicalCardData(TechnicalCardData data) {
        if (data == null) {
            System.out.println("No hay datos de ficha técnica disponibles.");
            return;
        }
        
        // Group fields by section
        Map<String, List<FieldInfo>> fieldsBySection = groupFieldsBySection(data);
        
        // Print general data first
        if (fieldsBySection.containsKey("")) {
            printSection(SECTION_NAMES.get(""), fieldsBySection.get(""));
        }
        
        // Print each section in order
        for (Map.Entry<String, String> section : SECTION_NAMES.entrySet()) {
            String sectionKey = section.getKey();
            if (!sectionKey.isEmpty() && fieldsBySection.containsKey(sectionKey)) {
                printSection(section.getValue(), fieldsBySection.get(sectionKey));
            }
        }
        
        // Print array fields
        printArrayFields(data);
    }
    
    /**
     * Groups fields by their section based on the field code prefix.
     */
    private static Map<String, List<FieldInfo>> groupFieldsBySection(TechnicalCardData data) {
        Map<String, List<FieldInfo>> sections = new LinkedHashMap<>();
        
        for (Field field : data.getClass().getDeclaredFields()) {
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (jsonProperty != null) {
                String fieldCode = jsonProperty.value();
                Object value = getFieldValue(data, field);
                
                if (value != null && !isArrayField(fieldCode)) {
                    String section = getSection(fieldCode);
                    sections.computeIfAbsent(section, k -> new ArrayList<>())
                            .add(new FieldInfo(fieldCode, value.toString()));
                }
            }
        }
        
        return sections;
    }
    
    /**
     * Determines the section of a field based on its code.
     */
    private static String getSection(String fieldCode) {
        if (fieldCode.equals("matricula") || fieldCode.equals("certificado") || 
            fieldCode.equals("fechaEmision") || fieldCode.equals("numEjes") || 
            fieldCode.equals("numRuedas") || fieldCode.equals("numNeumaticos")) {
            return "";
        }
        
        // Handle Greek Kappa
        if (fieldCode.startsWith("Κ")) {
            return "Κ";
        }
        
        // Handle EP fields
        if (fieldCode.startsWith("EP")) {
            return "EP";
        }
        
        // Extract first letter as section
        if (fieldCode.length() > 0 && Character.isLetter(fieldCode.charAt(0))) {
            return String.valueOf(fieldCode.charAt(0));
        }
        
        return "";
    }
    
    /**
     * Prints a section with its fields.
     */
    private static void printSection(String sectionName, List<FieldInfo> fields) {
        System.out.println();
        System.out.println(formatSectionHeader(sectionName));
        
        for (FieldInfo field : fields) {
            String description = TechnicalCardFieldDescriptions.getDescription(field.code);
            
            if (description != null) {
                printField(description + " (" + field.code + ")", field.value);
            } else {
                // If no description found, show field code only
                printField(field.code, field.value);
            }
        }
    }
    
    /**
     * Formats a section header.
     */
    private static String formatSectionHeader(String title) {
        int padding = (SECTION_WIDTH - title.length() - 2) / 2;
        String paddingStr = SECTION_SEPARATOR.repeat(Math.max(0, padding));
        return paddingStr + " " + title + " " + paddingStr;
    }
    
    /**
     * Prints a single field with its value.
     */
    private static void printField(String label, String value) {
        if (value != null && !value.isEmpty()) {
            System.out.println(label + ": " + value);
        }
    }
    
    /**
     * Prints array fields (homologaciones, observaciones, reformas).
     */
    private static void printArrayFields(TechnicalCardData data) {
        if (data.getHomologaciones() != null && !data.getHomologaciones().isEmpty()) {
            System.out.println();
            System.out.println(formatSectionHeader("Homologaciones"));
            for (int i = 0; i < data.getHomologaciones().size(); i++) {
                System.out.println("  " + (i + 1) + ". " + data.getHomologaciones().get(i));
            }
        }
        
        if (data.getObservaciones() != null && !data.getObservaciones().isEmpty()) {
            System.out.println();
            System.out.println(formatSectionHeader("Observaciones Adicionales"));
            for (int i = 0; i < data.getObservaciones().size(); i++) {
                System.out.println("  " + (i + 1) + ". " + data.getObservaciones().get(i));
            }
        }
        
        if (data.getReformas() != null && !data.getReformas().isEmpty()) {
            System.out.println();
            System.out.println(formatSectionHeader("Reformas"));
            for (int i = 0; i < data.getReformas().size(); i++) {
                System.out.println("  " + (i + 1) + ". " + data.getReformas().get(i));
            }
        }
    }
    
    /**
     * Gets the value of a field using reflection.
     */
    private static Object getFieldValue(TechnicalCardData data, Field field) {
        try {
            String fieldName = field.getName();
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getter = data.getClass().getMethod(getterName);
            return getter.invoke(data);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Checks if a field is an array field.
     */
    private static boolean isArrayField(String fieldCode) {
        return fieldCode.equals("homologaciones") || 
               fieldCode.equals("observaciones") || 
               fieldCode.equals("reformas");
    }
    
    /**
     * Helper class to store field information.
     */
    private static class FieldInfo {
        final String code;
        final String value;
        
        FieldInfo(String code, String value) {
            this.code = code;
            this.value = value;
        }
    }
}