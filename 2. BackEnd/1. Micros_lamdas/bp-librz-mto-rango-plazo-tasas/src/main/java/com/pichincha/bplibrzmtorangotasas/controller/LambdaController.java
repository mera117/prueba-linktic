
package com.pichincha.bplibrzmtorangotasas.controller;

import com.pichincha.bplibrzmtorangotasas.model.dto.TermRateRangeDto;
import com.pichincha.bplibrzmtorangotasas.model.dto.response.ResponseGeneralPichincha;
import com.pichincha.bplibrzmtorangotasas.services.CrudTermRateRangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class LambdaController {

    @Autowired
    private CrudTermRateRangeService crudTermRateRangeService;

    @Bean(name = "findTermRateRangeFinal")
    public Supplier<ResponseGeneralPichincha<List<TermRateRangeDto>>> findTermRateRangeFinalSupplier() {
        return () -> {
            return crudTermRateRangeService.findTermRateRange();
        };
    }

    @Bean(name = "saveTermRateRangeFinal")
    public Function<Map<String, TermRateRangeDto>, ResponseGeneralPichincha<String>> saveTermRateRangeFinalFunction() {
        return (request) -> {
            TermRateRangeDto body = request.get("body");
            return crudTermRateRangeService.saveTermRateRange(body);
        };
    }

    @Bean(name = "updateTermRateRangeFinal")
    public Function<Map<String, TermRateRangeDto>, ResponseGeneralPichincha<String>> updateTermRateRangeFinalFunction() {
        return (request) -> {
            TermRateRangeDto body = request.get("body");
            return crudTermRateRangeService.updateTermRateRange(body);
        };
    }

    @Bean(name = "deleteTermRateRangeFinal")
    public Function<Map<String, TermRateRangeDto>, ResponseGeneralPichincha<String>> deleteTermRateRangeFinalFunction() {
        return (request) -> {
            TermRateRangeDto body = request.get("body");
            return crudTermRateRangeService.deleteTermRateRange(body);
        };
    }

    @Bean(name = "findTermRateRangeByRangeFinal")
    public Function<Map<String, Object>, ResponseGeneralPichincha<List<TermRateRangeDto>>> findTermRateRangeByRangeFinal() {
        return request -> {
            Map<String, Object> body = (Map<String, Object>) request.get("body");
            String rangoPlazo = body != null && body.containsKey("rangoPlazo") ? body.get("rangoPlazo").toString() : "";
            return crudTermRateRangeService.findTermRateRangeByDescription(rangoPlazo);
        };
    }
}