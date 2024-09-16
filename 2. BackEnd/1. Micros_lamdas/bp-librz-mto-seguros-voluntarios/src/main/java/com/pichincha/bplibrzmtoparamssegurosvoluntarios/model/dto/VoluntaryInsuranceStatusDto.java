package com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class VoluntaryInsuranceStatusDto extends VoluntaryInsuranceCatalogDto{
    @JsonProperty("description_status")
    private String statusDescription;
}
