//package com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto;
//
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class VoluntaryInsuranceStatusDtoTest {
//
//
//    @Test
//    public void testGettersAndSetters() {
//        VoluntaryInsuranceStatusDto dto = new VoluntaryInsuranceStatusDto();
//
//        dto.setStatusDescription("Active");
//        dto.setId(1);
//        dto.setNombrePlan("Plan 1");
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
//        assertThat(dto.getStatusDescription()).isEqualTo("Active");
//        assertThat(dto.getId()).isEqualTo(1);
//        assertThat(dto.getNombrePlan()).isEqualTo("Plan 1");
//        assertThat(dto.getValorPrima()).isEqualTo(100.85);
//        assertThat(dto.getFallecimientoAccidental()).isEqualTo(20.3);
//        assertThat(dto.getInhabilitacionTotal()).isEqualTo(21.3);
//        assertThat(dto.getEnfermedadesGraves()).isEqualTo(22.3);
//        assertThat(dto.getCanastaFallecimiento()).isEqualTo(23.3);
//        assertThat(dto.getEstado()).isEqualTo(true);
//        assertThat(dto.getAsalariado()).isEqualTo(true);
//        assertThat(dto.getIndependiente()).isEqualTo(true);
//        assertThat(dto.getPensionado()).isEqualTo(true);
//        assertThat(dto.getEdadMinima()).isEqualTo(18);
//        assertThat(dto.getEdadMinimaDias()).isEqualTo(0);
//        assertThat(dto.getEdadMaxima()).isEqualTo(65);
//        assertThat(dto.getEdadMaximaDias()).isEqualTo(364);
//    }
//}