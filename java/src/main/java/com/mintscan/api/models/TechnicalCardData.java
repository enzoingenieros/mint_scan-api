package com.mintscan.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Model for technical card data fields.
 * Contains all possible fields from vehicle technical documentation.
 */
public class TechnicalCardData {
    
    @JsonProperty("matricula")
    private String matricula;
    
    @JsonProperty("certificado")
    private String certificado;
    
    // Section A - Manufacturer data
    /** Nombre del fabricante del vehículo base */
    @JsonProperty("A.1")
    private String a1;
    
    /** Dirección del fabricante del vehículo base */
    @JsonProperty("A.2")
    private String a2;
    
    // Section B - Completed vehicle manufacturer
    /** Nombre del fabricante del vehículo completado */
    @JsonProperty("B.1")
    private String b1;
    
    /** Dirección del fabricante del vehículo completado */
    @JsonProperty("B.2")
    private String b2;
    
    // Section C - ITV specific fields
    /** Código ITV */
    @JsonProperty("C.I")
    private String ci;
    
    /** Clasificación del vehículo */
    @JsonProperty("C.L")
    private String cl;
    
    /** Control VIN */
    @JsonProperty("C.V")
    private String cv;
    
    // Section D - Vehicle data
    /** Marca */
    @JsonProperty("D.1")
    private String d1;
    
    /** Tipo / Variante / Versión */
    @JsonProperty("D.2")
    private String d2;
    
    /** Denominación comercial del vehículo */
    @JsonProperty("D.3")
    private String d3;
    
    /** Procedencia */
    @JsonProperty("D.6")
    private String d6;
    
    // Section E - Identification
    /** Nº de identificación del vehículo */
    @JsonProperty("E")
    private String e;
    
    // Section F - Masses and dimensions
    /** Masa Máxima en carga Técnicamente Admisible (MMTA) */
    @JsonProperty("F.1")
    private String f1;
    
    /** Masa Máxima en carga Técnicamente Admisible en cada eje 1°/2°/3° */
    @JsonProperty("F.1.1")
    private String f11;
    
    /** Masa Máxima en carga Técnicamente Admisible en 5a rueda o pivote de acoplamiento */
    @JsonProperty("F.1.5")
    private String f15;
    
    /** Masa Máxima en carga Admisible del Vehículo en circulación (MMA) */
    @JsonProperty("F.2")
    private String f2;
    
    /** Masa Máxima autorizada en cada eje 1°/2°/3° */
    @JsonProperty("F.2.1")
    private String f21;
    
    /** Masa Máxima Técnicamente Admisible del conjunto (MMTAC) */
    @JsonProperty("F.3")
    private String f3;
    
    /** Masa Máxima Autorizada del conjunto MMC */
    @JsonProperty("F.3.1")
    private String f31;
    
    /** Altura total */
    @JsonProperty("F.4")
    private String f4;
    
    /** Anchura total */
    @JsonProperty("F.5")
    private String f5;
    
    /** Anchura máxima carrozable */
    @JsonProperty("F.5.1")
    private String f51;
    
    /** Longitud total */
    @JsonProperty("F.6")
    private String f6;
    
    /** Longitud máxima carrozable */
    @JsonProperty("F.6.1")
    private String f61;
    
    /** Vía anterior */
    @JsonProperty("F.7")
    private String f7;
    
    /** Vía posterior */
    @JsonProperty("F.7.1")
    private String f71;
    
    /** Voladizo posterior */
    @JsonProperty("F.8")
    private String f8;
    
    /** Voladizo máximo posterior carrozable */
    @JsonProperty("F.8.1")
    private String f81;
    
    // Section G - Operating mass
    /** Masa en Orden de marcha (MOM) */
    @JsonProperty("G")
    private String g;
    
    /** Masa en vacío para vehículos categoría L */
    @JsonProperty("G.1")
    private String g1;
    
    /** Masa Mínima Admisible del vehículo completado */
    @JsonProperty("G.2")
    private String g2;
    
    // Section J - Vehicle category
    /** Categoría del vehículo */
    @JsonProperty("J")
    private String j;
    
    /** Carrocería del vehículo */
    @JsonProperty("J.1")
    private String j1;
    
    /** Clase */
    @JsonProperty("J.2")
    private String j2;
    
    /** Volumen de bodegas */
    @JsonProperty("J.3")
    private String j3;
    
    // Section K - Type approval
    /** N° de homologación del vehículo base */
    @JsonProperty("K")
    private String k;
    
    /** N° de homologación del vehículo completado (Latin K) */
    @JsonProperty("K.1")
    private String k1;
    
    /** N° de homologación del vehículo completado (Greek Kappa) */
    @JsonProperty("Κ.1")
    private String k1Greek;
    
    /** N° certificado TITV vehículo base */
    @JsonProperty("K.2")
    private String k2;
    
    // Section L - Axles and wheels
    /** N° de ejes y ruedas */
    @JsonProperty("L")
    private String l;
    
    /** Nº y posición de ejes con ruedas gemelas */
    @JsonProperty("L.0")
    private String l0;
    
    /** Ejes motrices */
    @JsonProperty("L.1")
    private String l1;
    
    /** Dimensiones de los neumáticos */
    @JsonProperty("L.2")
    private String l2;
    
    // Section M - Wheelbase
    /** Distancia entre ejes 1°-2°, 2°-3° */
    @JsonProperty("M.1")
    private String m1;
    
    /** Distancia entre 5a rueda o pivote de acoplamiento y último eje */
    @JsonProperty("M.4")
    private String m4;
    
    // Section O - Towing capacity
    /** Masa Remolcable con frenos / Masa Remolcable Técnicamente Admisible del vehículo de motor en caso de: */
    @JsonProperty("O.1")
    private String o1;
    
    /** Barra de tracción */
    @JsonProperty("O.1.1")
    private String o11;
    
    /** Semirremolque */
    @JsonProperty("O.1.2")
    private String o12;
    
    /** Remolque eje central */
    @JsonProperty("O.1.3")
    private String o13;
    
    /** Remolque sin freno */
    @JsonProperty("O.1.4")
    private String o14;
    
    /** Masa Máxima remolcable Técnicamente Admisible con frenos mecánicos */
    @JsonProperty("O.2.1")
    private String o21;
    
    /** Masa Máxima remolcable Técnicamente Admisible con frenos de inercia */
    @JsonProperty("O.2.2")
    private String o22;
    
    /** Masa Máxima remolcable Técnicamente Admisible con frenos hidráulicos o neumáticos */
    @JsonProperty("O.2.3")
    private String o23;
    
    /** Tipo de freno de servicio */
    @JsonProperty("O.3")
    private String o3;
    
    // Section P - Engine
    /** Cilindrada */
    @JsonProperty("P.1")
    private String p1;
    
    /** Número y disposición de los cilindros */
    @JsonProperty("P.1.1")
    private String p11;
    
    /** Potencia de motor */
    @JsonProperty("P.2")
    private String p2;
    
    /** Potencia fiscal */
    @JsonProperty("P.2.1")
    private String p21;
    
    /** Tipo de combustible o fuente de energía */
    @JsonProperty("P.3")
    private String p3;
    
    /** Código de identificación del motor */
    @JsonProperty("P.5")
    private String p5;
    
    /** Fabricante o marca del motor */
    @JsonProperty("P.5.1")
    private String p51;
    
    // Section Q - Power/weight ratio
    /** Relación potencia / masa */
    @JsonProperty("Q")
    private String q;
    
    // Section R - Color
    /** Color */
    @JsonProperty("R")
    private String r;
    
    // Section S - Seating
    /** Nº de plazas asiento / N° de asientos o sillines */
    @JsonProperty("S.1")
    private String s1;
    
    /** Field S.1.1 (legacy) */
    @JsonProperty("S.1.1")
    private String s11;
    
    /** Cinturones de seguridad */
    @JsonProperty("S.1.2")
    private String s12;
    
    /** Nº de plazas de pie */
    @JsonProperty("S.2")
    private String s2;
    
    // Section T - Top speed
    /** Velocidad máxima */
    @JsonProperty("T")
    private String t;
    
    // Section U - Noise
    /** Nivel sonoro en parada */
    @JsonProperty("U.1")
    private String u1;
    
    /** Velocidad del motor a la que se mide el nivel sonoro o vehículo parado */
    @JsonProperty("U.2")
    private String u2;
    
    // Section V - Emissions
    /** Emisiones de CO2 */
    @JsonProperty("V.7")
    private String v7;
    
    /** Emisiones de CO */
    @JsonProperty("V.8")
    private String v8;
    
    /** Nivel de emisiones */
    @JsonProperty("V.9")
    private String v9;
    
    // Section Z - Year and series
    /** Año y número de la serie corta */
    @JsonProperty("Z")
    private String z;
    
    // Section EP - Protection structure
    /** Estructura de protección */
    @JsonProperty("EP")
    private String ep;
    
    /** Marca de la estructura de protección */
    @JsonProperty("EP.1")
    private String ep1;
    
    /** Modelo de la estructura de protección */
    @JsonProperty("EP.2")
    private String ep2;
    
    /** N° de homologación de la estructura de protección */
    @JsonProperty("EP.3")
    private String ep3;
    
    /** Nº identificativo de la estructura de protección */
    @JsonProperty("EP.4")
    private String ep4;
    
    // Additional fields
    @JsonProperty("numEjes")
    private String numEjes;
    
    @JsonProperty("numRuedas")
    private String numRuedas;
    
    @JsonProperty("numNeumaticos")
    private String numNeumaticos;
    
    @JsonProperty("homologaciones")
    private List<String> homologaciones;
    
    @JsonProperty("observaciones")
    private List<String> observaciones;
    
    @JsonProperty("reformas")
    private List<String> reformas;
    
    @JsonProperty("fechaEmision")
    private String fechaEmision;
    
    /**
     * Default constructor for Jackson deserialization.
     */
    public TechnicalCardData() {
    }
    
    // Getters and setters for all fields
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    
    public String getCertificado() { return certificado; }
    public void setCertificado(String certificado) { this.certificado = certificado; }
    
    public String getA1() { return a1; }
    public void setA1(String a1) { this.a1 = a1; }
    
    public String getA2() { return a2; }
    public void setA2(String a2) { this.a2 = a2; }
    
    public String getB1() { return b1; }
    public void setB1(String b1) { this.b1 = b1; }
    
    public String getB2() { return b2; }
    public void setB2(String b2) { this.b2 = b2; }
    
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    
    public String getCl() { return cl; }
    public void setCl(String cl) { this.cl = cl; }
    
    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }
    
    public String getD1() { return d1; }
    public void setD1(String d1) { this.d1 = d1; }
    
    public String getD2() { return d2; }
    public void setD2(String d2) { this.d2 = d2; }
    
    public String getD3() { return d3; }
    public void setD3(String d3) { this.d3 = d3; }
    
    public String getD6() { return d6; }
    public void setD6(String d6) { this.d6 = d6; }
    
    public String getE() { return e; }
    public void setE(String e) { this.e = e; }
    
    public String getF1() { return f1; }
    public void setF1(String f1) { this.f1 = f1; }
    
    public String getF11() { return f11; }
    public void setF11(String f11) { this.f11 = f11; }
    
    public String getF15() { return f15; }
    public void setF15(String f15) { this.f15 = f15; }
    
    public String getF2() { return f2; }
    public void setF2(String f2) { this.f2 = f2; }
    
    public String getF21() { return f21; }
    public void setF21(String f21) { this.f21 = f21; }
    
    public String getF3() { return f3; }
    public void setF3(String f3) { this.f3 = f3; }
    
    public String getF31() { return f31; }
    public void setF31(String f31) { this.f31 = f31; }
    
    public String getF4() { return f4; }
    public void setF4(String f4) { this.f4 = f4; }
    
    public String getF5() { return f5; }
    public void setF5(String f5) { this.f5 = f5; }
    
    public String getF51() { return f51; }
    public void setF51(String f51) { this.f51 = f51; }
    
    public String getF6() { return f6; }
    public void setF6(String f6) { this.f6 = f6; }
    
    public String getF61() { return f61; }
    public void setF61(String f61) { this.f61 = f61; }
    
    public String getF7() { return f7; }
    public void setF7(String f7) { this.f7 = f7; }
    
    public String getF71() { return f71; }
    public void setF71(String f71) { this.f71 = f71; }
    
    public String getF8() { return f8; }
    public void setF8(String f8) { this.f8 = f8; }
    
    public String getF81() { return f81; }
    public void setF81(String f81) { this.f81 = f81; }
    
    public String getG() { return g; }
    public void setG(String g) { this.g = g; }
    
    public String getG1() { return g1; }
    public void setG1(String g1) { this.g1 = g1; }
    
    public String getG2() { return g2; }
    public void setG2(String g2) { this.g2 = g2; }
    
    public String getJ() { return j; }
    public void setJ(String j) { this.j = j; }
    
    public String getJ1() { return j1; }
    public void setJ1(String j1) { this.j1 = j1; }
    
    public String getJ2() { return j2; }
    public void setJ2(String j2) { this.j2 = j2; }
    
    public String getJ3() { return j3; }
    public void setJ3(String j3) { this.j3 = j3; }
    
    public String getK() { return k; }
    public void setK(String k) { this.k = k; }
    
    public String getK1() { return k1; }
    public void setK1(String k1) { this.k1 = k1; }
    
    public String getK1Greek() { return k1Greek; }
    public void setK1Greek(String k1Greek) { this.k1Greek = k1Greek; }
    
    public String getK2() { return k2; }
    public void setK2(String k2) { this.k2 = k2; }
    
    public String getL() { return l; }
    public void setL(String l) { this.l = l; }
    
    public String getL0() { return l0; }
    public void setL0(String l0) { this.l0 = l0; }
    
    public String getL1() { return l1; }
    public void setL1(String l1) { this.l1 = l1; }
    
    public String getL2() { return l2; }
    public void setL2(String l2) { this.l2 = l2; }
    
    public String getM1() { return m1; }
    public void setM1(String m1) { this.m1 = m1; }
    
    public String getM4() { return m4; }
    public void setM4(String m4) { this.m4 = m4; }
    
    public String getO1() { return o1; }
    public void setO1(String o1) { this.o1 = o1; }
    
    public String getO11() { return o11; }
    public void setO11(String o11) { this.o11 = o11; }
    
    public String getO12() { return o12; }
    public void setO12(String o12) { this.o12 = o12; }
    
    public String getO13() { return o13; }
    public void setO13(String o13) { this.o13 = o13; }
    
    public String getO14() { return o14; }
    public void setO14(String o14) { this.o14 = o14; }
    
    public String getO21() { return o21; }
    public void setO21(String o21) { this.o21 = o21; }
    
    public String getO22() { return o22; }
    public void setO22(String o22) { this.o22 = o22; }
    
    public String getO23() { return o23; }
    public void setO23(String o23) { this.o23 = o23; }
    
    public String getO3() { return o3; }
    public void setO3(String o3) { this.o3 = o3; }
    
    public String getP1() { return p1; }
    public void setP1(String p1) { this.p1 = p1; }
    
    public String getP11() { return p11; }
    public void setP11(String p11) { this.p11 = p11; }
    
    public String getP2() { return p2; }
    public void setP2(String p2) { this.p2 = p2; }
    
    public String getP21() { return p21; }
    public void setP21(String p21) { this.p21 = p21; }
    
    public String getP3() { return p3; }
    public void setP3(String p3) { this.p3 = p3; }
    
    public String getP5() { return p5; }
    public void setP5(String p5) { this.p5 = p5; }
    
    public String getP51() { return p51; }
    public void setP51(String p51) { this.p51 = p51; }
    
    public String getQ() { return q; }
    public void setQ(String q) { this.q = q; }
    
    public String getR() { return r; }
    public void setR(String r) { this.r = r; }
    
    public String getS1() { return s1; }
    public void setS1(String s1) { this.s1 = s1; }
    
    public String getS11() { return s11; }
    public void setS11(String s11) { this.s11 = s11; }
    
    public String getS12() { return s12; }
    public void setS12(String s12) { this.s12 = s12; }
    
    public String getS2() { return s2; }
    public void setS2(String s2) { this.s2 = s2; }
    
    public String getT() { return t; }
    public void setT(String t) { this.t = t; }
    
    public String getU1() { return u1; }
    public void setU1(String u1) { this.u1 = u1; }
    
    public String getU2() { return u2; }
    public void setU2(String u2) { this.u2 = u2; }
    
    public String getV7() { return v7; }
    public void setV7(String v7) { this.v7 = v7; }
    
    public String getV8() { return v8; }
    public void setV8(String v8) { this.v8 = v8; }
    
    public String getV9() { return v9; }
    public void setV9(String v9) { this.v9 = v9; }
    
    public String getZ() { return z; }
    public void setZ(String z) { this.z = z; }
    
    public String getEp() { return ep; }
    public void setEp(String ep) { this.ep = ep; }
    
    public String getEp1() { return ep1; }
    public void setEp1(String ep1) { this.ep1 = ep1; }
    
    public String getEp2() { return ep2; }
    public void setEp2(String ep2) { this.ep2 = ep2; }
    
    public String getEp3() { return ep3; }
    public void setEp3(String ep3) { this.ep3 = ep3; }
    
    public String getEp4() { return ep4; }
    public void setEp4(String ep4) { this.ep4 = ep4; }
    
    public String getNumEjes() { return numEjes; }
    public void setNumEjes(String numEjes) { this.numEjes = numEjes; }
    
    public String getNumRuedas() { return numRuedas; }
    public void setNumRuedas(String numRuedas) { this.numRuedas = numRuedas; }
    
    public String getNumNeumaticos() { return numNeumaticos; }
    public void setNumNeumaticos(String numNeumaticos) { this.numNeumaticos = numNeumaticos; }
    
    public List<String> getHomologaciones() { return homologaciones; }
    public void setHomologaciones(List<String> homologaciones) { this.homologaciones = homologaciones; }
    
    public List<String> getObservaciones() { return observaciones; }
    public void setObservaciones(List<String> observaciones) { this.observaciones = observaciones; }
    
    public List<String> getReformas() { return reformas; }
    public void setReformas(List<String> reformas) { this.reformas = reformas; }
    
    public String getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(String fechaEmision) { this.fechaEmision = fechaEmision; }
}