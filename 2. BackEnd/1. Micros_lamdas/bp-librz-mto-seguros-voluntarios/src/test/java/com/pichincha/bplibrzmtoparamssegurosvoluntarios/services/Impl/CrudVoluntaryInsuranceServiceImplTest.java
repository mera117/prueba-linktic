package com.pichincha.bplibrzmtoparamssegurosvoluntarios.services.Impl;


import com.pichincha.bplibrzmtoparamssegurosvoluntarios.api.configuration.SqsAWSConfiguration;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.domain.bd.repositories.VoluntaryInsuranceCatalogRepository;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.VoluntaryInsuranceCatalogDto;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.response.ResponseGeneralPichincha;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils.Constants;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils.UtilsSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CrudVoluntaryInsuranceServiceImplTest {


    @Mock
    private VoluntaryInsuranceCatalogRepository voluntaryInsuranceCatalogRepository;

    @InjectMocks
    private CrudVoluntaryInsuranceServiceImpl crudVoluntaryInsuranceService;

    @Mock
    private SqsAWSConfiguration sqsAWSConfiguration;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindVoluntaryInsurance() {
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        when(voluntaryInsuranceCatalogRepository.findAll()).thenReturn(Collections.singletonList(dto));

        ResponseGeneralPichincha<List<VoluntaryInsuranceCatalogDto>> response = crudVoluntaryInsuranceService.findVoluntaryInsurance();

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.getData());
        verify(voluntaryInsuranceCatalogRepository, times(1)).findAll();
    }

    @Test
    void testFindVoluntaryInsuranceWhenExceptionIsThrown() {
        Exception exception = new RuntimeException("Database error");
        doThrow(exception).when(voluntaryInsuranceCatalogRepository).findAll();

        ResponseGeneralPichincha<List<VoluntaryInsuranceCatalogDto>> response =
                crudVoluntaryInsuranceService.findVoluntaryInsurance();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals("Database error", response.getMessage());
        assertEquals(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME, response.getServiceName());
        assertEquals(null, response.getData());
    }

    @Test
    public void testSaveVoluntaryInsuranceSuccess() {
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        when(voluntaryInsuranceCatalogRepository.save(any(VoluntaryInsuranceCatalogDto.class))).thenReturn(1L);

        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService.saveVoluntaryInsurance(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_SUCESSFULL_SAVE, response.getData());
        verify(sqsAWSConfiguration, times(1)).sendMessage(anyString());
        verify(voluntaryInsuranceCatalogRepository, times(1)).save(dto);
    }


    @Test
    void testSaveVoluntaryInsuranceWhenExceptionIsThrown() {
        // Arrange
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        Exception exception = new RuntimeException("Database error");
        doThrow(exception).when(voluntaryInsuranceCatalogRepository).save(dto);

        // Act
        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService.saveVoluntaryInsurance(dto);

        // Assert
        assertEquals(Constants.Message.MESSAGE_ERROR_SAVE, response.getData());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals("Database error", response.getMessage());
        assertEquals(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME, response.getServiceName());
    }

    @Test
    void testUpdateVoluntaryInsuranceWhenExceptionIsThrown() {
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        dto.setId(1);
        Exception exception = new RuntimeException("Database error");
        when(voluntaryInsuranceCatalogRepository.findById(dto.getId())).thenThrow(exception);
        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService.updateVoluntaryInsurance(dto);

        // Assert
        assertEquals(Constants.Message.MESSAGE_ERROR_UPDATE, response.getData());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals("Database error", response.getMessage());
        assertEquals(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME, response.getServiceName());
    }

    @Test
    public void testUpdateVoluntaryInsurance_Success() throws Exception {
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        dto.setId(1);
        dto.setUsuario("testUser");
        dto.setFechaHora("2023-01-01");
        dto.setIp("127.0.0.1");

        VoluntaryInsuranceCatalogDto initialDto = new VoluntaryInsuranceCatalogDto();
        initialDto.setId(1);

        when(voluntaryInsuranceCatalogRepository.findById(any(Integer.class))).thenReturn(initialDto);

        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService.updateVoluntaryInsurance(dto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_SUCESSFULL_UPDATE, response.getData());
        verify(voluntaryInsuranceCatalogRepository, times(1)).update(any(VoluntaryInsuranceCatalogDto.class));
        verify(sqsAWSConfiguration, times(1)).sendMessage(anyString());
    }

    @Test
    public void testUpdateVoluntaryInsurance_NotFound() {
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        dto.setId(1);

        when(voluntaryInsuranceCatalogRepository.findById(dto.getId())).thenReturn(null);

        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService.updateVoluntaryInsurance(dto);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_ERROR_FINDBYID, response.getData());
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.getMessage());
        assertEquals(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME, response.getServiceName());

    }

    @Test
    void testDeleteVoluntaryInsurance() {
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        dto.setId(1);
        when(voluntaryInsuranceCatalogRepository.findById(anyInt())).thenReturn(dto);
        doNothing().when(voluntaryInsuranceCatalogRepository).deleteById(anyInt());

        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService.deleteVoluntaryInsurance(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_SUCESSFULL_DELETE, response.getData());
        verify(voluntaryInsuranceCatalogRepository, times(1)).findById(anyInt());
        verify(voluntaryInsuranceCatalogRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testDeleteVoluntaryInsuranceWhenNotFound() {
        // Arrange
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        dto.setId(112);
        VoluntaryInsuranceCatalogDto emptyDto = new VoluntaryInsuranceCatalogDto();
        when(voluntaryInsuranceCatalogRepository.findById(dto.getId())).thenReturn(emptyDto);

        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService.deleteVoluntaryInsurance(dto);

        assertEquals(Constants.Message.MESSAGE_ERROR_FINDBYID, response.getData());
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT.getReasonPhrase(), response.getMessage());
        assertEquals(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME, response.getServiceName());
    }

    @Test
    void testDeleteVoluntaryInsuranceWhenExceptionIsThrown() {
        // Arrange
        VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
        dto.setId(1);
        Exception exception = new RuntimeException("Database error");
        when(voluntaryInsuranceCatalogRepository.findById(dto.getId())).thenThrow(exception);
        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService.deleteVoluntaryInsurance(dto);

        assertEquals(Constants.Message.MESSAGE_ERROR_DELETE, response.getData());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals("Database error", response.getMessage());
        assertEquals(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME, response.getServiceName());
    }

    @Test
    void testMapRow() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("nombre_plan")).thenReturn("Plan A");
        when(resultSet.getString("valor_prima")).thenReturn("100.85");
        when(resultSet.getString("fallecimiento_accidental")).thenReturn("20.3");
        when(resultSet.getString("inhabilitacion_total")).thenReturn("21.3");
        when(resultSet.getString("enfermedades_graves")).thenReturn("22.3");
        when(resultSet.getString("canasta_fallecimiento")).thenReturn("23.3");

        when(resultSet.getBoolean("estado")).thenReturn(true);
        when(resultSet.getBoolean("asalariado")).thenReturn(true);
        when(resultSet.getBoolean("independiente")).thenReturn(true);
        when(resultSet.getBoolean("pensionado")).thenReturn(true);

        when(resultSet.getInt("edad_minima")).thenReturn(18);
        when(resultSet.getInt("edad_minima_dias")).thenReturn(0);
        when(resultSet.getInt("edad_maxima")).thenReturn(80);
        when(resultSet.getInt("edad_maxima_dias")).thenReturn(364);


        VoluntaryInsuranceCatalogDto resultDto = voluntaryInsuranceCatalogRepository.new VoluntaryInsuranceCatalogRowMapper().mapRow(resultSet, 1);


        assertNotNull(resultDto);
        assertEquals(1, resultDto.getId());
        assertEquals("Plan A", resultDto.getNombrePlan());
        assertEquals(BigDecimal.valueOf(100.85), resultDto.getValorPrima());
        assertEquals(BigDecimal.valueOf(20.3), resultDto.getFallecimientoAccidental());
        assertEquals(BigDecimal.valueOf(21.3), resultDto.getInhabilitacionTotal());
        assertEquals(BigDecimal.valueOf(22.3), resultDto.getEnfermedadesGraves());
        assertEquals(BigDecimal.valueOf(23.3), resultDto.getCanastaFallecimiento());
        assertTrue(resultDto.getEstado());
        assertTrue(resultDto.getAsalariado());
        assertTrue(resultDto.getIndependiente());
        assertTrue(resultDto.getPensionado());
        assertEquals(18, resultDto.getEdadMinima());
        assertEquals(0, resultDto.getEdadMinimaDias());
        assertEquals(80, resultDto.getEdadMaxima());
        assertEquals(364, resultDto.getEdadMaximaDias());
    }

    @Test
    void testUpdateStatusVoluntaryInsurance_Success() {
        // Arrange
        VoluntaryInsuranceCatalogDto initialInsurance = new VoluntaryInsuranceCatalogDto();
        VoluntaryInsuranceCatalogDto updatedInsurance = new VoluntaryInsuranceCatalogDto();
        updatedInsurance.setId(1); // Establece las propiedades necesarias

        when(voluntaryInsuranceCatalogRepository.findById(1)).thenReturn(initialInsurance);
        doNothing().when(sqsAWSConfiguration).sendMessage(anyString());

        // Act
        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService.
                updateStatusVoluntaryInsurance(updatedInsurance);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_SUCESSFULL_DELETE, response.getData());
        verify(voluntaryInsuranceCatalogRepository, times(1)).updateStatus(any(VoluntaryInsuranceCatalogDto.class));
        verify(sqsAWSConfiguration, times(1)).sendMessage(anyString());
    }

    @Test
    void testUpdateStatusVoluntaryInsurance_NotFound() {
        // Arrange
        VoluntaryInsuranceCatalogDto updatedInsurance = new VoluntaryInsuranceCatalogDto();
        updatedInsurance.setId(1);

        when(voluntaryInsuranceCatalogRepository.findById(1)).thenReturn(null);

        // Act
        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService
                .updateStatusVoluntaryInsurance(updatedInsurance);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_ERROR_FINDBYID, response.getData());
        verify(voluntaryInsuranceCatalogRepository, never()).updateStatus(any(VoluntaryInsuranceCatalogDto.class));
        verify(sqsAWSConfiguration, never()).sendMessage(anyString());
    }

    @Test
    void testUpdateStatusVoluntaryInsurance_Exception() {
        // Arrange
        VoluntaryInsuranceCatalogDto updatedInsurance = new VoluntaryInsuranceCatalogDto();
        updatedInsurance.setId(1);

        when(voluntaryInsuranceCatalogRepository.findById(1)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseGeneralPichincha<String> response = crudVoluntaryInsuranceService
                .updateStatusVoluntaryInsurance(updatedInsurance);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals(Constants.Message.MESSAGE_ERROR_DELETE, response.getData());
    }

}