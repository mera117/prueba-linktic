
package com.pichincha.bplibrzmtoparamssegurosvoluntarios.controller;

import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.VoluntaryInsuranceCatalogDto;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.response.ResponseGeneralPichincha;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.services.CrudVoluntaryInsuranceService;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class LambdaController {

     private final CrudVoluntaryInsuranceService crudVoluntaryInsuranceService;

    public LambdaController(CrudVoluntaryInsuranceService crudVoluntaryInsuranceService) {
        this.crudVoluntaryInsuranceService = crudVoluntaryInsuranceService;
    }


    @Bean(name = "findVoluntaryInsuranceFinal")
    public Supplier<ResponseGeneralPichincha<List<VoluntaryInsuranceCatalogDto>>> findVoluntaryInsuranceFinalSupplier() {
        return () ->
             crudVoluntaryInsuranceService.findVoluntaryInsurance();

    }

    @Bean(name = "saveVoluntaryInsuranceFinal")
    public Function<Map<String,VoluntaryInsuranceCatalogDto>, ResponseGeneralPichincha<String>> saveVoluntaryInsuranceFinalFunction() {
        return request -> {
            try{
            VoluntaryInsuranceCatalogDto body= request.get("body");
            return crudVoluntaryInsuranceService.saveVoluntaryInsurance(body);
        }catch (Exception e) {
                log.error(Constants.Message.EXCEPTION_MESSAGE, e.getCause());
                throw e;
            }
        };
    }

    @Bean(name = "updateVoluntaryInsuranceFinal")
    public Function<Map<String,VoluntaryInsuranceCatalogDto>, ResponseGeneralPichincha<String>> updateVoluntaryInsuranceFinalFunction() {
        return request -> {
            try {
            VoluntaryInsuranceCatalogDto body= request.get("body");
            return crudVoluntaryInsuranceService.updateVoluntaryInsurance(body);
        }catch (Exception e) {
                log.error(Constants.Message.EXCEPTION_MESSAGE, e.getCause());
                throw e;
            }
        };
    }

    @Bean(name = "updateStatusVoluntaryInsuranceFinal")
    public Function<Map<String,VoluntaryInsuranceCatalogDto>, ResponseGeneralPichincha<String>>
    updateStatusVoluntaryInsuranceFinalFunction() {
        return request -> {
            try {
                VoluntaryInsuranceCatalogDto body= request.get("body");
                return crudVoluntaryInsuranceService.updateStatusVoluntaryInsurance(body);
            }catch (Exception e) {
                log.error(Constants.Message.EXCEPTION_MESSAGE, e.getCause());
                throw e;
            }
        };
    }

    @Bean(name = "deleteVoluntaryInsuranceFinal")
    public Function<Map<String,VoluntaryInsuranceCatalogDto>, ResponseGeneralPichincha<String>> deleteVoluntaryInsuranceFinalFunction() {
        return request -> {
            try {
                VoluntaryInsuranceCatalogDto body = request.get("body");
                return crudVoluntaryInsuranceService.deleteVoluntaryInsurance(body);
            } catch (Exception e) {
                log.error(Constants.Message.EXCEPTION_MESSAGE, e.getCause());
                throw e;
            }
        };
    }
}