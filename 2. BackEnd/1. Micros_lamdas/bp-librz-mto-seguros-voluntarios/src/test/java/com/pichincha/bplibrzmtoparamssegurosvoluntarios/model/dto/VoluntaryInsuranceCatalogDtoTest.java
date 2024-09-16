//package com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class VoluntaryInsuranceCatalogDtoTest {
//
//    @Test
//    public void testSerialization() throws JsonProcessingException {
//        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
//        dto.setId(1);
//        dto.setNombrePlan("Plan Básico");
//        dto.setValorPrima(BigDecimal.valueOf(100.85));
//        dto.setFallecimientoAccidental(BigDecimal.valueOf(20.3));
//        dto.setInhabilitacionTotal(BigDecimal.valueOf(21.3));
//        dto.setEnfermedadesGraves(BigDecimal.valueOf(22.3));
//        dto.setCanastaFallecimiento(BigDecimal.valueOf(23.3));
//        dto.setEstado(true);
//        dto.setAsalariado(true);
//        dto.setIndependiente(true);
//        dto.setPensionado(true);
//        dto.setEdadMinima(18);
//        dto.setEdadMinimaDias(0);
//        dto.setEdadMaxima(65);
//        dto.setEdadMaximaDias(364);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonString = mapper.writeValueAsString(dto);
//
//        assertNotNull(jsonString);
//        assertTrue(jsonString.contains("\"id\":1"));
//        assertTrue(jsonString.contains("\"nombre_plan\":\"Plan Básico\""));
//    }
//
//    @Test
//    public void testDeserialization() throws JsonProcessingException {
//        String jsonString = "{\"id_plan\":1,\"nombre_plan\":\"Plan Básico\",\"valor_prima\":\"100.00\","
//                + "\"fallecimiento_accidental\":\"Sí\",\"inhabilitacion\":\"No\",\"enfermedades_graves\":\"Sí\","
//                + "\"canasta_fallecimiento\":\"Sí\",\"status_id\":1,\"asalariado\":\"Sí\",\"independiente\":\"No\","
//                + "\"pensionado\":\"Sí\",\"edad_minima\":18,\"edad_minima_dias\":6570,\"edad_maxima\":65,\"edad_maxima_dias\":23725}";
//
//        ObjectMapper mapper = new ObjectMapper();
//        VoluntaryInsuranceCatalogDto dto = mapper.readValue(jsonString, VoluntaryInsuranceCatalogDto.class);
//
//        assertNotNull(dto);
//        assertEquals(1, dto.getId());
//        assertEquals("Plan Básico", dto.getNombrePlan());
//        assertEquals(100.85, dto.getValorPrima());
//        assertEquals(20.3, dto.getFallecimientoAccidental());
//        assertEquals(21.3, dto.getInhabilitacionTotal());
//        assertEquals(22.3, dto.getEnfermedadesGraves());
//        assertEquals(23.3, dto.getCanastaFallecimiento());
//        assertEquals(true, dto.getEstado());
//        assertEquals(true, dto.getAsalariado());
//        assertEquals(true, dto.getIndependiente());
//        assertEquals(true, dto.getPensionado());
//        assertEquals(18, dto.getEdadMinima());
//        assertEquals(0, dto.getEdadMinimaDias());
//        assertEquals(65, dto.getEdadMaxima());
//        assertEquals(23725, dto.getEdadMaximaDias());
//    }
//
//    @Test
//    public void testGettersAndSetters() {
//        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
//        dto.setIdPlan(1L);
//        dto.setNombrePlan("Plan Básico");
//        dto.setValorPrima("100.00");
//        dto.setFallecimientoAccidental("Sí");
//        dto.setInhabilitacion("No");
//        dto.setEnfermedadesGraves("Sí");
//        dto.setCanastaFallecimiento("Sí");
//        dto.setStatusId(1L);
//        dto.setAsalariado("Sí");
//        dto.setIndependiente("No");
//        dto.setPensionado("Sí");
//        dto.setEdadMinima(18);
//        dto.setEdadMinimaDias(6570);
//        dto.setEdadMaxima(65);
//        dto.setEdadMaximaDias(23725);
//
//        assertEquals(1L, dto.getIdPlan());
//        assertEquals("Plan Básico", dto.getNombrePlan());
//        assertEquals("100.00", dto.getValorPrima());
//        assertEquals("Sí", dto.getFallecimientoAccidental());
//        assertEquals("No", dto.getInhabilitacion());
//        assertEquals("Sí", dto.getEnfermedadesGraves());
//        assertEquals("Sí", dto.getCanastaFallecimiento());
//        assertEquals(1L, dto.getStatusId());
//        assertEquals("Sí", dto.getAsalariado());
//        assertEquals("No", dto.getIndependiente());
//        assertEquals("Sí", dto.getPensionado());
//        assertEquals(18, dto.getEdadMinima());
//        assertEquals(6570, dto.getEdadMinimaDias());
//        assertEquals(65, dto.getEdadMaxima());
//        assertEquals(23725, dto.getEdadMaximaDias());
//    }
//}