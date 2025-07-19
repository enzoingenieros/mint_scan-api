# Field Comparison: TechnicalCardData vs Expected JSON Keys

## Analysis Summary

Comparing the fields in `TechnicalCardData.java` with the expected JSON keys, I found the following discrepancies:

### ❌ Missing Fields in Java Model

The following fields are present in the expected JSON but **missing** in the Java model:

1. **C.I** - "Código ITV"
2. **C.L** - "Clasificación del vehículo"
3. **C.V** - "Control VIN"
4. **F.6.1** - "Longitud máxima carrozable"
5. **F.8.1** - "Voladizo máximo posterior carrozable"
6. **G.2** - "Masa Mínima Admisible del vehículo completado"
7. **S.1.2** - "Cinturones de seguridad"

### ⚠️ Potential Issues

1. **K.1 vs Κ.1**: The expected JSON has **Κ.1** (Greek letter Kappa) while the Java model has **K.1** (Latin letter K)
   - JSON: `"Κ.1": "N° de homologación del vehículo completado"`
   - Java: `@JsonProperty("K.1")`

2. **S.1.1**: The Java model has `S.1.1` but the expected JSON shows `S.1.2` for "Cinturones de seguridad"

### ✅ Fields Present in Both

All other fields from the expected JSON are correctly present in the Java model with matching names:
- A.1, A.2, B.1, B.2
- D.1, D.2, D.3, D.6
- E, EP, EP.1, EP.2, EP.3, EP.4
- F.1, F.1.1, F.1.5, F.2, F.2.1, F.3, F.3.1, F.4, F.5, F.5.1, F.6, F.7, F.7.1, F.8
- G, G.1
- J, J.1, J.2, J.3
- K, K.2
- L, L.0, L.1, L.2
- M.1, M.4
- O.1, O.1.1, O.1.2, O.1.3, O.1.4, O.2.1, O.2.2, O.2.3, O.3
- P.1, P.1.1, P.2, P.2.1, P.3, P.5, P.5.1
- Q, R
- S.1, S.2
- T
- U.1, U.2
- V.7, V.8, V.9
- Z

### 📝 Additional Fields in Java Model

The Java model includes additional fields not in the expected JSON:
- matricula
- certificado
- numEjes
- numRuedas
- numNeumaticos
- homologaciones
- observaciones
- reformas
- fechaEmision

## Recommended Changes

To ensure compatibility with the expected JSON structure, the following fields should be added to `TechnicalCardData.java`:

```java
// Section C - ITV specific fields
@JsonProperty("C.I")
private String ci;

@JsonProperty("C.L")
private String cl;

@JsonProperty("C.V")
private String cv;

// Additional F section fields
@JsonProperty("F.6.1")
private String f61;

@JsonProperty("F.8.1")
private String f81;

// Additional G section field
@JsonProperty("G.2")
private String g2;

// Fix S.1.1 to S.1.2
@JsonProperty("S.1.2")
private String s12;

// Fix K.1 to use Greek letter Kappa
@JsonProperty("Κ.1")
private String k1Greek;
```

Note: The K.1 vs Κ.1 issue needs special attention as it involves different character encodings (Latin vs Greek).