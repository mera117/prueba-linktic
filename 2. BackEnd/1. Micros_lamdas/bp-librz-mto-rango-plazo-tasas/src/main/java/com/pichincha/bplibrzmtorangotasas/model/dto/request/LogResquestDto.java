package com.pichincha.bplibrzmtorangotasas.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LogResquestDto {
	private String usuario;
	private String fecha_hora;
	private String accion;
	private String ip;
	private String transaccion;
	private String valorAnterior;
	private String valorModificado;
}
