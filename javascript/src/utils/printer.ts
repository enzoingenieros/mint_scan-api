import { FIELD_DESCRIPTIONS } from '../models/constants.ts';
import type { TechnicalCardData } from '../models/types.ts';

function getFieldDescription(code: string): string {
  if (code in FIELD_DESCRIPTIONS) {
    return `${FIELD_DESCRIPTIONS[code]} (${code})`;
  }
  return code;
}

export function printTechnicalCardData(data: TechnicalCardData): void {
  // Datos generales
  console.log('\n--- Datos Generales ---');
  console.log(`Matrícula: ${data.matricula || 'N/D'}`);
  console.log(`Certificado: ${data.certificado || 'N/D'}`);
  console.log(`Fecha de emisión: ${data.fechaEmision || 'N/D'}`);
  
  // Sección A - Datos del fabricante
  console.log('\n--- Sección A: Homologación ---');
  console.log(`${getFieldDescription('A.1')}: ${data['A.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('A.2')}: ${data['A.2'] || 'N/D'}`);
  
  // Sección B - Fechas
  console.log('\n--- Sección B: Fechas ---');
  console.log(`${getFieldDescription('B.1')}: ${data['B.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('B.2')}: ${data['B.2'] || 'N/D'}`);
  
  // Sección D - Datos del vehículo
  console.log('\n--- Sección D: Vehículo ---');
  console.log(`${getFieldDescription('D.1')}: ${data['D.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('D.2')}: ${data['D.2'] || 'N/D'}`);
  console.log(`${getFieldDescription('D.3')}: ${data['D.3'] || 'N/D'}`);
  console.log(`${getFieldDescription('D.6')}: ${data['D.6'] || 'N/D'}`);
  
  // Sección E - Identificación
  console.log('\n--- Sección E: Identificación ---');
  console.log(`${getFieldDescription('E')}: ${data['E'] || 'N/D'}`);
  
  // Sección F - Masas
  console.log('\n--- Sección F: Masas (kg) ---');
  console.log(`${getFieldDescription('F.1')}: ${data['F.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.1.1')}: ${data['F.1.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.1.5')}: ${data['F.1.5'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.2')}: ${data['F.2'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.2.1')}: ${data['F.2.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.3')}: ${data['F.3'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.3.1')}: ${data['F.3.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.4')}: ${data['F.4'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.5')}: ${data['F.5'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.5.1')}: ${data['F.5.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.6')}: ${data['F.6'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.7')}: ${data['F.7'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.7.1')}: ${data['F.7.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('F.8')}: ${data['F.8'] || 'N/D'}`);
  
  // Sección G - Masas
  console.log('\n--- Sección G: Masa en servicio ---');
  console.log(`${getFieldDescription('G')}: ${data['G'] || 'N/D'}`);
  console.log(`${getFieldDescription('G.1')}: ${data['G.1'] || 'N/D'}`);
  
  // Sección J - Categoría
  console.log('\n--- Sección J: Categoría ---');
  console.log(`${getFieldDescription('J')}: ${data['J'] || 'N/D'}`);
  console.log(`${getFieldDescription('J.1')}: ${data['J.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('J.2')}: ${data['J.2'] || 'N/D'}`);
  console.log(`${getFieldDescription('J.3')}: ${data['J.3'] || 'N/D'}`);
  
  // Sección K - Homologación
  console.log('\n--- Sección K: Homologación de tipo ---');
  console.log(`${getFieldDescription('K')}: ${data['K'] || 'N/D'}`);
  console.log(`${getFieldDescription('K.1')}: ${data['K.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('K.2')}: ${data['K.2'] || 'N/D'}`);
  
  // Sección L - Dimensiones
  console.log('\n--- Sección L: Dimensiones (mm) ---');
  console.log(`${getFieldDescription('L')}: ${data['L'] || 'N/D'}`);
  console.log(`${getFieldDescription('L.0')}: ${data['L.0'] || 'N/D'}`);
  console.log(`${getFieldDescription('L.1')}: ${data['L.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('L.2')}: ${data['L.2'] || 'N/D'}`);
  
  // Sección M - Distancias
  console.log('\n--- Sección M: Más dimensiones (mm) ---');
  console.log(`${getFieldDescription('M.1')}: ${data['M.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('M.4')}: ${data['M.4'] || 'N/D'}`);
  
  // Sección O - Remolque
  console.log('\n--- Sección O: Remolque (kg) ---');
  console.log(`${getFieldDescription('O.1')}: ${data['O.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('O.1.1')}: ${data['O.1.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('O.1.2')}: ${data['O.1.2'] || 'N/D'}`);
  console.log(`${getFieldDescription('O.1.3')}: ${data['O.1.3'] || 'N/D'}`);
  console.log(`${getFieldDescription('O.1.4')}: ${data['O.1.4'] || 'N/D'}`);
  console.log(`${getFieldDescription('O.2.1')}: ${data['O.2.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('O.2.2')}: ${data['O.2.2'] || 'N/D'}`);
  console.log(`${getFieldDescription('O.2.3')}: ${data['O.2.3'] || 'N/D'}`);
  console.log(`${getFieldDescription('O.3')}: ${data['O.3'] || 'N/D'}`);
  
  // Sección P - Motor
  console.log('\n--- Sección P: Motor ---');
  console.log(`${getFieldDescription('P.1')}: ${data['P.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('P.1.1')}: ${data['P.1.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('P.2')}: ${data['P.2'] || 'N/D'}`);
  console.log(`${getFieldDescription('P.2.1')}: ${data['P.2.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('P.3')}: ${data['P.3'] || 'N/D'}`);
  console.log(`${getFieldDescription('P.5')}: ${data['P.5'] || 'N/D'}`);
  console.log(`${getFieldDescription('P.5.1')}: ${data['P.5.1'] || 'N/D'}`);
  
  // Sección Q - Relación potencia/peso
  console.log('\n--- Sección Q: Relación potencia/peso ---');
  console.log(`${getFieldDescription('Q')}: ${data['Q'] || 'N/D'}`);
  
  // Sección R - Color
  console.log('\n--- Sección R: Color ---');
  console.log(`${getFieldDescription('R')}: ${data['R'] || 'N/D'}`);
  
  // Sección S - Plazas
  console.log('\n--- Sección S: Plazas ---');
  console.log(`${getFieldDescription('S.1')}: ${data['S.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('S.1.1')}: ${data['S.1.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('S.2')}: ${data['S.2'] || 'N/D'}`);
  
  // Sección T - Velocidad
  console.log('\n--- Sección T: Velocidad ---');
  console.log(`${getFieldDescription('T')}: ${data['T'] || 'N/D'}`);
  
  // Sección U - Ruido
  console.log('\n--- Sección U: Ruido ---');
  console.log(`${getFieldDescription('U.1')}: ${data['U.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('U.2')}: ${data['U.2'] || 'N/D'}`);
  
  // Sección V - Emisiones
  console.log('\n--- Sección V: Emisiones ---');
  console.log(`${getFieldDescription('V.7')}: ${data['V.7'] || 'N/D'}`);
  console.log(`${getFieldDescription('V.8')}: ${data['V.8'] || 'N/D'}`);
  console.log(`${getFieldDescription('V.9')}: ${data['V.9'] || 'N/D'}`);
  
  // Sección Z - Observaciones
  console.log('\n--- Sección Z: Observaciones ---');
  console.log(`${getFieldDescription('Z')}: ${data['Z'] || 'N/D'}`);
  
  // Sección EP - Equipamiento especial
  console.log('\n--- Sección EP: Equipamiento especial ---');
  console.log(`${getFieldDescription('EP')}: ${data['EP'] || 'N/D'}`);
  console.log(`${getFieldDescription('EP.1')}: ${data['EP.1'] || 'N/D'}`);
  console.log(`${getFieldDescription('EP.2')}: ${data['EP.2'] || 'N/D'}`);
  console.log(`${getFieldDescription('EP.3')}: ${data['EP.3'] || 'N/D'}`);
  console.log(`${getFieldDescription('EP.4')}: ${data['EP.4'] || 'N/D'}`);
  
  // Otros campos
  console.log('\n--- Otros datos ---');
  console.log(`Número de ejes: ${data.numEjes || 'N/D'}`);
  console.log(`Número de ruedas: ${data.numRuedas || 'N/D'}`);
  console.log(`Número de neumáticos: ${data.numNeumaticos || 'N/D'}`);
  
  // Arrays
  if (data.homologaciones && data.homologaciones.length > 0) {
    console.log('\n--- Homologaciones ---');
    data.homologaciones.forEach((h, i) => {
      console.log(`  ${i + 1}. ${h}`);
    });
  }
  
  if (data.observaciones && data.observaciones.length > 0) {
    console.log('\n--- Observaciones adicionales ---');
    data.observaciones.forEach((o, i) => {
      console.log(`  ${i + 1}. ${o}`);
    });
  }
  
  if (data.reformas && data.reformas.length > 0) {
    console.log('\n--- Reformas ---');
    data.reformas.forEach((r, i) => {
      console.log(`  ${i + 1}. ${r}`);
    });
  }
}