package com.pichincha.bplibrzmtorangotasas.controller;

import com.pichincha.bplibrzmtorangotasas.domain.db.repositories.TermRateRangeRepository;
import com.pichincha.bplibrzmtorangotasas.model.dto.TermRateRangeDto;
import com.pichincha.bplibrzmtorangotasas.model.dto.response.ResponseGeneralPichincha;
import com.pichincha.bplibrzmtorangotasas.services.CrudTermRateRangeService;
import com.pichincha.bplibrzmtorangotasas.services.impl.CrudTermRateRangeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LambdaControllerTest {

    @InjectMocks
    private LambdaController lambdaController;

    @Mock
    private CrudTermRateRangeService crudTermRateRangeService;

    @Mock
    private TermRateRangeRepository termRateRangeRepository;

    @Mock
    private CrudTermRateRangeServiceImpl crudTermRateRangeServiceImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findTermRateRangeFinalSupplier() {
        Supplier<ResponseGeneralPichincha<List<TermRateRangeDto>>> supplier
                = lambdaController.findTermRateRangeFinalSupplier();

        ResponseGeneralPichincha<List<TermRateRangeDto>> expectedResponse =
                new ResponseGeneralPichincha<>();

        when(crudTermRateRangeService.findTermRateRange()).thenReturn(expectedResponse);
        assertEquals(expectedResponse, supplier.get());
    }

    @Test
    void saveTermRateRangeFinalFunction() {
        Function<Map<String, TermRateRangeDto>,
                ResponseGeneralPichincha<String>> function = lambdaController.saveTermRateRangeFinalFunction();

        Map<String, TermRateRangeDto> request = new HashMap<>();
        TermRateRangeDto dto = new TermRateRangeDto();
        request.put("body", dto);

        ResponseGeneralPichincha<String> expectedResponse = new ResponseGeneralPichincha<>();
        when(crudTermRateRangeService.saveTermRateRange(dto)).thenReturn(expectedResponse);

        assertEquals(expectedResponse, function.apply(request));
    }

    @Test
    void updateTermRateRangeFinalFunction() {
        Map<String, TermRateRangeDto> request = new HashMap<>();
        TermRateRangeDto dto = new TermRateRangeDto();
        request.put("body", dto);

        // Arrange
        ResponseGeneralPichincha<String> expectedResponse = new ResponseGeneralPichincha<>();
        expectedResponse.setData("Update successful");
        expectedResponse.setStatusCode(200);
        expectedResponse.setMessage("OK");
        expectedResponse.setServiceName("TestService");

        when(crudTermRateRangeService.updateTermRateRange(dto)).thenReturn(expectedResponse);

        Function<Map<String, TermRateRangeDto>, ResponseGeneralPichincha<String>> function
                = lambdaController.updateTermRateRangeFinalFunction();

        Map<String, TermRateRangeDto> requesttwo = new HashMap<>();
        request.put("body", dto);

        // Act
        ResponseGeneralPichincha<String> response = function.apply(request);

        // Assert
        assertEquals(expectedResponse.getData(), response.getData());
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        assertEquals(expectedResponse.getServiceName(), response.getServiceName());

        verify(crudTermRateRangeService, times(1)).updateTermRateRange(dto);
    }

    @Test
    void deleteTermRateRangeFinalFunction() {
        Function<Map<String, TermRateRangeDto>,
                ResponseGeneralPichincha<String>> function = lambdaController.deleteTermRateRangeFinalFunction();
        Map<String, TermRateRangeDto> request = new HashMap<>();
        TermRateRangeDto dto = new TermRateRangeDto();
        request.put("body", dto);

        // Arrange
        ResponseGeneralPichincha<String> expectedResponse = new ResponseGeneralPichincha<>();
        expectedResponse.setData("Delete successful");
        expectedResponse.setStatusCode(200);
        expectedResponse.setMessage("OK");
        expectedResponse.setServiceName("TestService");

        when(crudTermRateRangeService.deleteTermRateRange(dto)).thenReturn(expectedResponse);

        Map<String, TermRateRangeDto> requestTwo = new HashMap<>();
        request.put("body", dto);

        // Act
        ResponseGeneralPichincha<String> response = function.apply(request);

        // Assert
        assertEquals(expectedResponse.getData(), response.getData());
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        assertEquals(expectedResponse.getServiceName(), response.getServiceName());

        verify(crudTermRateRangeService, times(1)).deleteTermRateRange(dto);
    }


    @Test
    void findTermRateRangeByRangeFinal() {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("body", Collections.singletonMap("rangoPlazo", "5"));

        List<TermRateRangeDto> termRateRangeList = new ArrayList<>();
        TermRateRangeDto termRateRangeDto = new TermRateRangeDto();
        termRateRangeDto.setDescriptionPlazo("1,5,10");
        termRateRangeDto.setIdPlazo(2L);
        termRateRangeList.add(termRateRangeDto);

        ResponseGeneralPichincha<List<TermRateRangeDto>> expectedResponse = new ResponseGeneralPichincha<>();
        expectedResponse.setData(termRateRangeList);
        expectedResponse.setStatusCode(HttpStatus.OK.value());
        expectedResponse.setMessage(HttpStatus.OK.getReasonPhrase());
        expectedResponse.setServiceName("TestService");

        when(crudTermRateRangeService.findTermRateRangeByDescription("5")).thenReturn(expectedResponse);

        // Act
        ResponseGeneralPichincha<?> response = lambdaController.findTermRateRangeByRangeFinal().apply(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getData() instanceof List);

        List<?> dataList = (List<?>) response.getData();
        assertFalse(dataList.isEmpty());

        TermRateRangeDto result = (TermRateRangeDto) dataList.get(0);
        assertEquals(2L, result.getIdPlazo());
        assertEquals("1,5,10", result.getDescriptionPlazo());

        verify(crudTermRateRangeService, times(1)).findTermRateRangeByDescription("5");
    }

    @Test
    void testIsEmptyOrNull() {
        assertTrue(CrudTermRateRangeServiceImpl.isEmptyOrNull(null));
        assertTrue(CrudTermRateRangeServiceImpl.isEmptyOrNull(""));
        assertFalse(CrudTermRateRangeServiceImpl.isEmptyOrNull("not empty"));
    }

    @Test
    void testIsIntegerParseable() {
        assertTrue(CrudTermRateRangeServiceImpl.isIntegerParseable("123"));
        assertFalse(CrudTermRateRangeServiceImpl.isIntegerParseable("abc"));
        assertFalse(CrudTermRateRangeServiceImpl.isIntegerParseable(null));
    }

    @Test
    void testMatchesGroup() {
        CrudTermRateRangeServiceImpl service = new CrudTermRateRangeServiceImpl();
        assertTrue(service.matchesGroup("1,2,3", 2));
        assertTrue(service.matchesGroup("1-3", 2));
        assertTrue(service.matchesGroup(">5", 6));
        assertTrue(service.matchesGroup("<5", 4));
        assertFalse(service.matchesGroup("1,2,3", 4));
        assertFalse(service.matchesGroup("1-3", 4));
        assertFalse(service.matchesGroup(">5", 5));
        assertFalse(service.matchesGroup("<5", 5));
    }

    @Test
    void testValidateCommaSeparated() {
        CrudTermRateRangeServiceImpl service = new CrudTermRateRangeServiceImpl();
        assertTrue(service.validateCommaSeparated("1,2,3", 2));
        assertFalse(service.validateCommaSeparated("1,2,3", 4));
    }

    @Test
    void testValidateRango() {
        CrudTermRateRangeServiceImpl service = new CrudTermRateRangeServiceImpl();
        assertTrue(service.validateRango("1-3", 2));
        assertFalse(service.validateRango("1-3", 4));
    }

    @Test
    void testValidateGreaterThan() {
        CrudTermRateRangeServiceImpl service = new CrudTermRateRangeServiceImpl();
        assertTrue(service.validateGreaterThan(">5", 6));
        assertFalse(service.validateGreaterThan(">5", 5));
    }

    @Test
    void testValidateLessThan() {
        CrudTermRateRangeServiceImpl service = new CrudTermRateRangeServiceImpl();
        assertTrue(service.validateLessThan("<5", 4));
        assertFalse(service.validateLessThan("<5", 5));
    }
}