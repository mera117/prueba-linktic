package com.pichincha.bplibrzmtorangotasas.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pichincha.bplibrzmtorangotasas.api.configuration.SqsAWSConfiguration;
import com.pichincha.bplibrzmtorangotasas.domain.db.repositories.TermRateRangeRepository;
import com.pichincha.bplibrzmtorangotasas.model.dto.TermRateRangeDto;
import com.pichincha.bplibrzmtorangotasas.model.dto.response.ResponseGeneralPichincha;
import com.pichincha.bplibrzmtorangotasas.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CrudTermRateRangeServiceImplTest {
    @Mock
    private TermRateRangeRepository termRateRangeRepository;

    @InjectMocks
    private CrudTermRateRangeServiceImpl crudTermRateRangeService;

    @Mock
    private ResultSet resultSet;

    @Mock
    private SqsAWSConfiguration sqsAWSConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findTermRateRange() {
        TermRateRangeDto dto = new TermRateRangeDto();
        when(termRateRangeRepository.findAll()).thenReturn(Collections.singletonList(dto));

        ResponseGeneralPichincha<List<TermRateRangeDto>> response = crudTermRateRangeService.findTermRateRange();

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.getData());
        verify(termRateRangeRepository, times(1)).findAll();
    }
    @Test
    void testFindTermRateRangeWhenExceptionIsThrown() {
        Exception exception = new RuntimeException("Database error");
        doThrow(exception).when(termRateRangeRepository).findAll();

        ResponseGeneralPichincha<List<TermRateRangeDto>> response =
                crudTermRateRangeService.findTermRateRange();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals("Database error", response.getMessage());
        assertEquals(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME, response.getServiceName());
        assertEquals(null, response.getData());
    }


    @Test
    void saveTermRateRange() throws JsonProcessingException {
        TermRateRangeDto dto = new TermRateRangeDto();
        doNothing().when(termRateRangeRepository).save(any(TermRateRangeDto.class));

        ResponseGeneralPichincha<String> response = crudTermRateRangeService.saveTermRateRange(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_SUCESSFULL_SAVE, response.getData());
        verify(termRateRangeRepository, times(1)).save(any(TermRateRangeDto.class));
    }

    @Test
    void testSaveTermRateRangeWhenExceptionIsThrown() throws JsonProcessingException {
        // Arrange
        TermRateRangeDto dto = new TermRateRangeDto();
        Exception exception = new RuntimeException("Database error");
        doThrow(exception).when(termRateRangeRepository).save(dto);

        // Act
        ResponseGeneralPichincha<String> response = crudTermRateRangeService.saveTermRateRange(dto);

        // Assert
        assertEquals(Constants.Message.MESSAGE_ERROR_SAVE, response.getData());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals("Database error", response.getMessage());
        assertEquals(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME, response.getServiceName());
    }

    @Test
    void deleteTermRateRange() {
        TermRateRangeDto dto = new TermRateRangeDto();
        dto.setIdPlazo(1L);
        when(termRateRangeRepository.findById(anyLong())).thenReturn(dto);
        doNothing().when(termRateRangeRepository).deleteById(anyLong());

        ResponseGeneralPichincha<String> response = crudTermRateRangeService.deleteTermRateRange(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_SUCESSFULL_DELETE, response.getData());
        verify(termRateRangeRepository, times(1)).findById(anyLong());
        verify(termRateRangeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteTermRateRangeWhenNotFound() {
        // Arrange
        TermRateRangeDto dto = new TermRateRangeDto();
        dto.setIdPlazo(1L);
        TermRateRangeDto emptyDto = new TermRateRangeDto();
        when(termRateRangeRepository.findById(dto.getIdPlazo())).thenReturn(emptyDto);

        ResponseGeneralPichincha<String> response = crudTermRateRangeService.deleteTermRateRange(dto);

        assertEquals(Constants.Message.MESSAGE_ERROR_FINDBYID, response.getData());
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT.getReasonPhrase(), response.getMessage());
        assertEquals(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME, response.getServiceName());
    }

    @Test
    void testDeleteTermRateRangeWhenExceptionIsThrown() {
        // Arrange
        TermRateRangeDto dto = new TermRateRangeDto();
        dto.setIdPlazo(1L);
        Exception exception = new RuntimeException("Database error");
        when(termRateRangeRepository.findById(dto.getIdPlazo())).thenThrow(exception);
        ResponseGeneralPichincha<String> response = crudTermRateRangeService.deleteTermRateRange(dto);

        assertEquals(Constants.Message.MESSAGE_ERROR_DELETE,response.getData());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals("Database error", response.getMessage());
        assertEquals(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME, response.getServiceName());
    }


    @Test
    void testMapRow() throws SQLException {
        // Arrange
        TermRateRangeRepository.TermRateRangeDtoRowMapper mapper
                = new TermRateRangeRepository.TermRateRangeDtoRowMapper();


        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("description_plazo")).thenReturn("1-10");
        when(resultSet.getBoolean("estado")).thenReturn(true);



        // Act
        TermRateRangeDto result = mapper.mapRow(resultSet, 1);

        // Assert
        assertEquals(1L, result.getIdPlazo());
        assertEquals("1-10", result.getDescriptionPlazo());
        assertEquals(1L, result.getStatusId());

    }


    @Test
    public void testUpdateTermRateRange() {

        TermRateRangeDto termRateRangeDtoInicial = new TermRateRangeDto();
        termRateRangeDtoInicial.setDescriptionPlazo("ID_INICIAL");

        TermRateRangeDto termRateRangeDtoActualizado = new TermRateRangeDto();
        termRateRangeDtoActualizado.setDescriptionPlazo("ID_ACTUALIZADO");

        Mockito.when(termRateRangeRepository.findById(any()))
                .thenReturn(termRateRangeDtoInicial);

        ResponseGeneralPichincha<String> response = crudTermRateRangeService
                .updateTermRateRange(termRateRangeDtoActualizado);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_ERROR_FINDBYID, response.getData());
    }

    @Test
    public void testUpdateTermRateRange_Success() throws Exception {
        TermRateRangeDto dto = new TermRateRangeDto();
        dto.setIdPlazo(1L);
        dto.setActualizadoPor("testUser");
        dto.setFechaActualizacion("2023-01-01");
        dto.setIp("127.0.0.1");

        TermRateRangeDto initialDto = new TermRateRangeDto();
        initialDto.setIdPlazo(1L);

        when(termRateRangeRepository.findById(any(Long.class))).thenReturn(initialDto);

        ResponseGeneralPichincha<String> response = crudTermRateRangeService.updateTermRateRange(dto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_SUCESSFULL_UPDATE, response.getData());
        verify(termRateRangeRepository, times(1)).update(any(TermRateRangeDto.class));
        verify(sqsAWSConfiguration, times(1)).sendMessage(anyString());
    }

    @Test
    public void testUpdateTermRateRange_NotFound() throws Exception {
        TermRateRangeDto dto = new TermRateRangeDto();
        dto.setIdPlazo(1L);
        dto.setActualizadoPor("testUser");
        dto.setFechaActualizacion("2023-01-01");
        dto.setIp("127.0.0.1");

        TermRateRangeDto initialDto = new TermRateRangeDto();

        when(termRateRangeRepository.findById(any(Long.class))).thenReturn(initialDto);

        ResponseGeneralPichincha<String> response = crudTermRateRangeService.updateTermRateRange(dto);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_ERROR_FINDBYID, response.getData());
        verify(termRateRangeRepository, times(0)).update(any(TermRateRangeDto.class));
    }

    @Test
    void testFindTermRateRangeByDescription_ValidRange() {
        // Arrange
        String validRange = "5";
        List<TermRateRangeDto> termRateRangeList = new ArrayList<>();
        TermRateRangeDto termRateRangeDto = new TermRateRangeDto();
        termRateRangeDto.setDescriptionPlazo("1,5,10");
        termRateRangeDto.setIdPlazo(2L);
        termRateRangeList.add(termRateRangeDto);

        when(termRateRangeRepository.findTermRateRangeByDescriptionActive()).thenReturn(termRateRangeList);

        ResponseGeneralPichincha<List<TermRateRangeDto>> response = crudTermRateRangeService.findTermRateRangeByDescription(validRange);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.getData());

        List<TermRateRangeDto> dataList = (List<TermRateRangeDto>) response.getData();
        assertFalse(dataList.isEmpty());
        assertEquals(termRateRangeDto, dataList.get(0));
    }
    @Test
    void testFindTermRateRangeByDescription_ValidRange_a() {        
        String validRange = null;  
        ResponseGeneralPichincha<List<TermRateRangeDto>> response = crudTermRateRangeService.findTermRateRangeByDescription(validRange);
        assertTrue(response.getStatusCode()==HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    void testFindTermRateRangeByDescription_ValidRange_b() {
        // Arrange
        String validRange = "a";
        ResponseGeneralPichincha<List<TermRateRangeDto>> response = crudTermRateRangeService.findTermRateRangeByDescription(validRange);
        assertTrue(response.getStatusCode()==HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    void testFindTermRateRangeByDescription_ValidRange_c() {
        // Arrange
        String validRange = "1";
        List<TermRateRangeDto> termRateRangeList = new ArrayList<>();
        TermRateRangeDto termRateRangeDto = new TermRateRangeDto();
        termRateRangeDto.setDescriptionPlazo("1,5,10");
        termRateRangeDto.setIdPlazo(2L);
        termRateRangeList.add(termRateRangeDto);

        when(  termRateRangeRepository.findTermRateRangeByDescriptionActive()).thenReturn(new ArrayList<>());
        ResponseGeneralPichincha<List<TermRateRangeDto>> response = crudTermRateRangeService.findTermRateRangeByDescription(validRange);
        assertTrue(response.getStatusCode()==HttpStatus.INTERNAL_SERVER_ERROR.value());    }
  

}