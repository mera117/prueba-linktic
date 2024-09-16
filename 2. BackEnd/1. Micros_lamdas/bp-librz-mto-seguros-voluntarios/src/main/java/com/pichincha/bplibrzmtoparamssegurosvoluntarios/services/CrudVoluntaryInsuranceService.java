package com.pichincha.bplibrzmtoparamssegurosvoluntarios.services;



import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.VoluntaryInsuranceCatalogDto;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.response.ResponseGeneralPichincha;

import java.util.List;


public interface CrudVoluntaryInsuranceService {

    ResponseGeneralPichincha<List<VoluntaryInsuranceCatalogDto>> findVoluntaryInsurance();

    ResponseGeneralPichincha<String> saveVoluntaryInsurance(VoluntaryInsuranceCatalogDto voluntaryInsuranceCatalogDto);


    ResponseGeneralPichincha<String> updateVoluntaryInsurance(
            VoluntaryInsuranceCatalogDto voluntaryInsuranceCatalogDto);

    ResponseGeneralPichincha<String> deleteVoluntaryInsurance(
            VoluntaryInsuranceCatalogDto voluntaryInsuranceGeneralRequestDto);

    ResponseGeneralPichincha<String> updateStatusVoluntaryInsurance(
            VoluntaryInsuranceCatalogDto voluntaryInsuranceCatalogDto);

}
