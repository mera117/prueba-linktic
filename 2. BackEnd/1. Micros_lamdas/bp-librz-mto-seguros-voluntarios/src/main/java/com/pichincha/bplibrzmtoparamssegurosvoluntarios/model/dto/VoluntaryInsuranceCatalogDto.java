package com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils.CurrencyDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoluntaryInsuranceCatalogDto {

    private Integer id;

    @JsonProperty("nombre_plan")
    private String nombrePlan;

    @JsonProperty("valor_prima")
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private BigDecimal valorPrima;

    @JsonProperty("fallecimiento_accidental")
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private BigDecimal fallecimientoAccidental;

    @JsonProperty("inhabilitacion_total")
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private BigDecimal inhabilitacionTotal;

    @JsonProperty("enfermedades_graves")
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private BigDecimal enfermedadesGraves;

    @JsonProperty("canasta_fallecimiento")
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private BigDecimal canastaFallecimiento;

    @JsonProperty("estado")
    private Boolean estado;

    @JsonProperty("estado_plan")
    private Boolean estadoPlan;

    @JsonProperty("asalariado")
    private Boolean asalariado;

    @JsonProperty("independiente")
    private Boolean independiente;

    @JsonProperty("pensionado")
    private Boolean pensionado;

    @JsonProperty("edad_minima")
    private int edadMinima;

    @JsonProperty("edad_minima_dias")
    private int edadMinimaDias;

    @JsonProperty("edad_maxima")
    private int edadMaxima;

    @JsonProperty("edad_maxima_dias")
    private int edadMaximaDias;

    @JsonProperty("usuario")
    private String usuario;

    @JsonProperty("fecha_hora")
    private String fechaHora;

    @JsonProperty("accion")
    private String accion;

    private String ip;
}
