package com.pichincha.bplibrzmtorangotasas.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TermRateRangeDto {
	@JsonProperty("id_plazo")
	private Long idPlazo;

	@JsonProperty("description_plazo")
	private String descriptionPlazo;

	@JsonProperty("status_id")
	private Long statusId;

	@JsonProperty("creado_por")
	private String creadoPor;

	@JsonProperty("fecha_creacion")
	private String fechaCreacion;

	@JsonProperty("actualizado_por")
	private String actualizadoPor;

	@JsonProperty("fecha_actualizacion")
	private String fechaActualizacion;

	private String ip;

	@JsonProperty("description_status")
	private String statusDescription;

	public TermRateRangeDto(ResultSet resultSet) throws SQLException {
		this.idPlazo = resultSet.getLong("id");
		this.descriptionPlazo = resultSet.getString("description_plazo");
		this.statusDescription = resultSet.getBoolean("estado") ? "Activo" : "Inactivo";
		this.statusId = resultSet.getBoolean("estado") ? 1l: 2l;

	}
}
