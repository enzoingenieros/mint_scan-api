package com.mintitv.api.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Field descriptions for Technical Card data fields.
 * Maps field codes to their Spanish descriptions as defined in the ITV documentation.
 */
public final class TechnicalCardFieldDescriptions {
    
    private static final Map<String, String> FIELD_DESCRIPTIONS = new HashMap<>();
    
    static {
        // Section A - Manufacturer data
        FIELD_DESCRIPTIONS.put("A.1", "Nombre del fabricante del vehículo base");
        FIELD_DESCRIPTIONS.put("A.2", "Dirección del fabricante del vehículo base");
        
        // Section B - Completed vehicle manufacturer
        FIELD_DESCRIPTIONS.put("B.1", "Nombre del fabricante del vehículo completado");
        FIELD_DESCRIPTIONS.put("B.2", "Dirección del fabricante del vehículo completado");
        
        // Section C - ITV specific
        FIELD_DESCRIPTIONS.put("C.I", "Código ITV");
        FIELD_DESCRIPTIONS.put("C.L", "Clasificación del vehículo");
        FIELD_DESCRIPTIONS.put("C.V", "Control VIN");
        
        // Section D - Vehicle identification
        FIELD_DESCRIPTIONS.put("D.1", "Marca");
        FIELD_DESCRIPTIONS.put("D.2", "Tipo / Variante / Versión");
        FIELD_DESCRIPTIONS.put("D.3", "Denominación comercial del vehículo");
        FIELD_DESCRIPTIONS.put("D.6", "Procedencia");
        
        // Section E - VIN
        FIELD_DESCRIPTIONS.put("E", "Nº de identificación del vehículo");
        
        // Section EP - Protection structure
        FIELD_DESCRIPTIONS.put("EP", "Estructura de protección");
        FIELD_DESCRIPTIONS.put("EP.1", "Marca de la estructura de protección");
        FIELD_DESCRIPTIONS.put("EP.2", "Modelo de la estructura de protección");
        FIELD_DESCRIPTIONS.put("EP.3", "N° de homologación de la estructura de protección");
        FIELD_DESCRIPTIONS.put("EP.4", "Nº identificativo de la estructura de protección");
        
        // Section F - Masses and dimensions
        FIELD_DESCRIPTIONS.put("F.1", "Masa Máxima en carga Técnicamente Admisible (MMTA)");
        FIELD_DESCRIPTIONS.put("F.1.1", "Masa Máxima en carga Técnicamente Admisible en cada eje 1°/2°/3°");
        FIELD_DESCRIPTIONS.put("F.1.5", "Masa Máxima en carga Técnicamente Admisible en 5a rueda o pivote de acoplamiento");
        FIELD_DESCRIPTIONS.put("F.2", "Masa Máxima en carga Admisible del Vehículo en circulación (MMA)");
        FIELD_DESCRIPTIONS.put("F.2.1", "Masa Máxima autorizada en cada eje 1°/2°/3°");
        FIELD_DESCRIPTIONS.put("F.3", "Masa Máxima Técnicamente Admisible del conjunto (MMTAC)");
        FIELD_DESCRIPTIONS.put("F.3.1", "Masa Máxima Autorizada del conjunto MMC");
        FIELD_DESCRIPTIONS.put("F.4", "Altura total");
        FIELD_DESCRIPTIONS.put("F.5", "Anchura total");
        FIELD_DESCRIPTIONS.put("F.5.1", "Anchura máxima carrozable");
        FIELD_DESCRIPTIONS.put("F.6", "Longitud total");
        FIELD_DESCRIPTIONS.put("F.6.1", "Longitud máxima carrozable");
        FIELD_DESCRIPTIONS.put("F.7", "Vía anterior");
        FIELD_DESCRIPTIONS.put("F.7.1", "Vía posterior");
        FIELD_DESCRIPTIONS.put("F.8", "Voladizo posterior");
        FIELD_DESCRIPTIONS.put("F.8.1", "Voladizo máximo posterior carrozable");
        
        // Section G - Operating mass
        FIELD_DESCRIPTIONS.put("G", "Masa en Orden de marcha (MOM)");
        FIELD_DESCRIPTIONS.put("G.1", "Masa en vacío para vehículos categoría L");
        FIELD_DESCRIPTIONS.put("G.2", "Masa Mínima Admisible del vehículo completado");
        
        // Section J - Vehicle category
        FIELD_DESCRIPTIONS.put("J", "Categoría del vehículo");
        FIELD_DESCRIPTIONS.put("J.1", "Carrocería del vehículo");
        FIELD_DESCRIPTIONS.put("J.2", "Clase");
        FIELD_DESCRIPTIONS.put("J.3", "Volumen de bodegas");
        
        // Section K - Type approval
        FIELD_DESCRIPTIONS.put("K", "N° de homologación del vehículo base");
        FIELD_DESCRIPTIONS.put("K.1", "N° de homologación del vehículo completado (Latin K)");
        FIELD_DESCRIPTIONS.put("Κ.1", "N° de homologación del vehículo completado");
        FIELD_DESCRIPTIONS.put("K.2", "N° certificado TITV vehículo base");
        
        // Section L - Axles and wheels
        FIELD_DESCRIPTIONS.put("L", "N° de ejes y ruedas");
        FIELD_DESCRIPTIONS.put("L.0", "Nº y posición de ejes con ruedas gemelas");
        FIELD_DESCRIPTIONS.put("L.1", "Ejes motrices");
        FIELD_DESCRIPTIONS.put("L.2", "Dimensiones de los neumáticos");
        
        // Section M - Wheelbase
        FIELD_DESCRIPTIONS.put("M.1", "Distancia entre ejes 1°-2°, 2°-3°");
        FIELD_DESCRIPTIONS.put("M.4", "Distancia entre 5a rueda o pivote de acoplamiento y último eje");
        
        // Section O - Towing capacity
        FIELD_DESCRIPTIONS.put("O.1", "Masa Remolcable con frenos / Masa Remolcable Técnicamente Admisible del vehículo de motor en caso de:");
        FIELD_DESCRIPTIONS.put("O.1.1", "Barra de tracción");
        FIELD_DESCRIPTIONS.put("O.1.2", "Semirremolque");
        FIELD_DESCRIPTIONS.put("O.1.3", "Remolque eje central");
        FIELD_DESCRIPTIONS.put("O.1.4", "Remolque sin freno");
        FIELD_DESCRIPTIONS.put("O.2.1", "Masa Máxima remolcable Técnicamente Admisible con frenos mecánicos");
        FIELD_DESCRIPTIONS.put("O.2.2", "Masa Máxima remolcable Técnicamente Admisible con frenos de inercia");
        FIELD_DESCRIPTIONS.put("O.2.3", "Masa Máxima remolcable Técnicamente Admisible con frenos hidráulicos o neumáticos");
        FIELD_DESCRIPTIONS.put("O.3", "Tipo de freno de servicio");
        
        // Section P - Engine
        FIELD_DESCRIPTIONS.put("P.1", "Cilindrada");
        FIELD_DESCRIPTIONS.put("P.1.1", "Número y disposición de los cilindros");
        FIELD_DESCRIPTIONS.put("P.2", "Potencia de motor");
        FIELD_DESCRIPTIONS.put("P.2.1", "Potencia fiscal");
        FIELD_DESCRIPTIONS.put("P.3", "Tipo de combustible o fuente de energía");
        FIELD_DESCRIPTIONS.put("P.5", "Código de identificación del motor");
        FIELD_DESCRIPTIONS.put("P.5.1", "Fabricante o marca del motor");
        
        // Section Q - Power/weight ratio
        FIELD_DESCRIPTIONS.put("Q", "Relación potencia / masa");
        
        // Section R - Color
        FIELD_DESCRIPTIONS.put("R", "Color");
        
        // Section S - Seating
        FIELD_DESCRIPTIONS.put("S.1", "Nº de plazas asiento / N° de asientos o sillines");
        FIELD_DESCRIPTIONS.put("S.1.1", "Campo S.1.1 (legacy)");
        FIELD_DESCRIPTIONS.put("S.1.2", "Cinturones de seguridad");
        FIELD_DESCRIPTIONS.put("S.2", "Nº de plazas de pie");
        
        // Section T - Top speed
        FIELD_DESCRIPTIONS.put("T", "Velocidad máxima");
        
        // Section U - Noise
        FIELD_DESCRIPTIONS.put("U.1", "Nivel sonoro en parada");
        FIELD_DESCRIPTIONS.put("U.2", "Velocidad del motor a la que se mide el nivel sonoro o vehículo parado");
        
        // Section V - Emissions
        FIELD_DESCRIPTIONS.put("V.7", "Emisiones de CO2");
        FIELD_DESCRIPTIONS.put("V.8", "Emisiones de CO");
        FIELD_DESCRIPTIONS.put("V.9", "Nivel de emisiones");
        
        // Section Z - Year and series
        FIELD_DESCRIPTIONS.put("Z", "Año y número de la serie corta");
        
        // Special fields (not in standard sections)
        FIELD_DESCRIPTIONS.put("matricula", "Matrícula");
        FIELD_DESCRIPTIONS.put("certificado", "Certificado");
        FIELD_DESCRIPTIONS.put("fechaEmision", "Fecha de Emisión");
        FIELD_DESCRIPTIONS.put("numEjes", "Número de Ejes");
        FIELD_DESCRIPTIONS.put("numRuedas", "Número de Ruedas");
        FIELD_DESCRIPTIONS.put("numNeumaticos", "Número de Neumáticos");
    }
    
    private TechnicalCardFieldDescriptions() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Gets the Spanish description for a field code.
     *
     * @param fieldCode the field code (e.g., "A.1", "B.2")
     * @return the Spanish description, or null if not found
     */
    public static String getDescription(String fieldCode) {
        return FIELD_DESCRIPTIONS.get(fieldCode);
    }
    
    /**
     * Gets all field descriptions.
     *
     * @return unmodifiable map of field codes to descriptions
     */
    public static Map<String, String> getAllDescriptions() {
        return new HashMap<>(FIELD_DESCRIPTIONS);
    }
    
    /**
     * Checks if a field code has a description.
     *
     * @param fieldCode the field code to check
     * @return true if the field has a description
     */
    public static boolean hasDescription(String fieldCode) {
        return FIELD_DESCRIPTIONS.containsKey(fieldCode);
    }
}