package com.pichincha.bplibrzmtorangotasas.services;


import com.pichincha.bplibrzmtorangotasas.model.dto.TermRateRangeDto;
import com.pichincha.bplibrzmtorangotasas.model.dto.response.ResponseGeneralPichincha;

import java.util.List;

public interface CrudTermRateRangeService {

    ResponseGeneralPichincha<List<TermRateRangeDto>> findTermRateRange();

    ResponseGeneralPichincha<String> saveTermRateRange(TermRateRangeDto termRateRangeDto);

    ResponseGeneralPichincha<String> updateTermRateRange(
            TermRateRangeDto termRateRangeDto);

    ResponseGeneralPichincha<String> deleteTermRateRange(
            TermRateRangeDto termRateRangeDto);

    ResponseGeneralPichincha<List<TermRateRangeDto>> findTermRateRangeByDescription(String rangoPlazo);

}
