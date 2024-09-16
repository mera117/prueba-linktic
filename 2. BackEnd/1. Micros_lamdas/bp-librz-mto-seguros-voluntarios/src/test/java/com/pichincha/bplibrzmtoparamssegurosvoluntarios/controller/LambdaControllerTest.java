package com.pichincha.bplibrzmtoparamssegurosvoluntarios.controller;

import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.VoluntaryInsuranceCatalogDto;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.response.ResponseGeneralPichincha;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.services.CrudVoluntaryInsuranceService;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LambdaControllerTest {

    @InjectMocks
    private LambdaController lambdaController;

    @Mock
    private CrudVoluntaryInsuranceService crudVoluntaryInsuranceService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findVoluntaryInsuranceFinalSupplier() {
        Supplier<ResponseGeneralPichincha<List<VoluntaryInsuranceCatalogDto>>> supplier
                = lambdaController.findVoluntaryInsuranceFinalSupplier();

        ResponseGeneralPichincha<List<VoluntaryInsuranceCatalogDto>> expectedResponse =
                new ResponseGeneralPichincha<>();

        when(crudVoluntaryInsuranceService.findVoluntaryInsurance()).thenReturn(expectedResponse);
        assertEquals(expectedResponse, supplier.get());
    }

    @Test
    void saveVoluntaryInsuranceFinalFunction() {
        Function<Map<String, VoluntaryInsuranceCatalogDto>,
                ResponseGeneralPichincha<String>> function = lambdaController.saveVoluntaryInsuranceFinalFunction();

        Map<String, VoluntaryInsuranceCatalogDto> request = new HashMap<>();
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        request.put("body", dto);

        ResponseGeneralPichincha<String> expectedResponse = new ResponseGeneralPichincha<>();
        when(crudVoluntaryInsuranceService.saveVoluntaryInsurance(dto)).thenReturn(expectedResponse);

        assertEquals(expectedResponse, function.apply(request));
    }

    @Test
    public void testUpdateVoluntaryInsuranceFinalFunction() {
        Map<String, VoluntaryInsuranceCatalogDto> request = new HashMap<>();
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        request.put("body", dto);

        // Arrange
        ResponseGeneralPichincha<String> expectedResponse = new ResponseGeneralPichincha<>();
        expectedResponse.setData("Update successful");
        expectedResponse.setStatusCode(200);
        expectedResponse.setMessage("OK");
        expectedResponse.setServiceName("TestService");

        when(crudVoluntaryInsuranceService.updateVoluntaryInsurance(dto)).thenReturn(expectedResponse);

        Function<Map<String, VoluntaryInsuranceCatalogDto>, ResponseGeneralPichincha<String>> function
                = lambdaController.updateVoluntaryInsuranceFinalFunction();

        Map<String, VoluntaryInsuranceCatalogDto> requesttwo = new HashMap<>();
        request.put("body", dto);

        // Act
        ResponseGeneralPichincha<String> response = function.apply(request);

        // Assert
        assertEquals(expectedResponse.getData(), response.getData());
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        assertEquals(expectedResponse.getServiceName(), response.getServiceName());

        verify(crudVoluntaryInsuranceService, times(1)).updateVoluntaryInsurance(dto);
    }

    @Test
    public void testDeleteVoluntaryInsuranceFinalFunction() {

        Function<Map<String, VoluntaryInsuranceCatalogDto>,
                ResponseGeneralPichincha<String>> function = lambdaController.deleteVoluntaryInsuranceFinalFunction();
        Map<String, VoluntaryInsuranceCatalogDto> request = new HashMap<>();
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        request.put("body", dto);

        // Arrange
        ResponseGeneralPichincha<String> expectedResponse = new ResponseGeneralPichincha<>();
        expectedResponse.setData("Delete successful");
        expectedResponse.setStatusCode(200);
        expectedResponse.setMessage("OK");
        expectedResponse.setServiceName("TestService");

        when(crudVoluntaryInsuranceService.deleteVoluntaryInsurance(dto)).thenReturn(expectedResponse);

        Map<String, VoluntaryInsuranceCatalogDto> requestTwo = new HashMap<>();
        request.put("body", dto);

        // Act
        ResponseGeneralPichincha<String> response = function.apply(request);

        // Assert
        assertEquals(expectedResponse.getData(), response.getData());
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        assertEquals(expectedResponse.getServiceName(), response.getServiceName());

        verify(crudVoluntaryInsuranceService, times(1)).deleteVoluntaryInsurance(dto);
    }


    @Test
    void testUpdateStatusVoluntaryInsuranceFinalFunction_Success() {
        // Arrange
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        ResponseGeneralPichincha<String> expectedResponse = new ResponseGeneralPichincha<>();
        expectedResponse.setData("Success");

        Map<String, VoluntaryInsuranceCatalogDto> requestMap = new HashMap<>();
        requestMap.put("body", dto);

        when(crudVoluntaryInsuranceService.updateStatusVoluntaryInsurance(dto)).thenReturn(expectedResponse);

        Function<Map<String, VoluntaryInsuranceCatalogDto>, ResponseGeneralPichincha<String>> function =
                lambdaController.updateStatusVoluntaryInsuranceFinalFunction();

        // Act
        ResponseGeneralPichincha<String> actualResponse = function.apply(requestMap);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(crudVoluntaryInsuranceService).updateStatusVoluntaryInsurance(dto);
    }

    @Test
    void testUpdateStatusVoluntaryInsuranceFinalFunction_Exception() {
        // Arrange
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        Map<String, VoluntaryInsuranceCatalogDto> requestMap = new HashMap<>();
        requestMap.put("body", dto);

        Exception exception = new RuntimeException("Error");

        when(crudVoluntaryInsuranceService.updateStatusVoluntaryInsurance(dto)).thenThrow(exception);

        Function<Map<String, VoluntaryInsuranceCatalogDto>, ResponseGeneralPichincha<String>> function =
                lambdaController.updateStatusVoluntaryInsuranceFinalFunction();

        // Act & Assert
        try {
            function.apply(requestMap);
        } catch (Exception e) {
            assertEquals(exception, e);
        }
    }
}