package com.pichincha.bplibrzmtorangotasas.model;

import com.pichincha.bplibrzmtorangotasas.model.dto.request.LogResquestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogResquestDtoTest {
    @Test
    void testLogResquestDtoConstructor() {
        // Arrange
        String usuario = "usuario_test";
        String fechaHora = "2024-05-21 21:51:37";
        String ip = "127.0.0.1";
        String transaccion = "12345";
        String valorModificado = "Valor modificado de prueba";
        String accion = "Accion";

        // Act
        LogResquestDto logRequestDto = new LogResquestDto(usuario, fechaHora,accion, ip, transaccion,valorModificado, valorModificado);

        // Assert
        assertEquals(usuario, logRequestDto.getUsuario());
        assertEquals(fechaHora, logRequestDto.getFecha_hora());
        assertEquals(ip, logRequestDto.getIp());
        assertEquals(transaccion, logRequestDto.getTransaccion());
        assertEquals(valorModificado, logRequestDto.getValorModificado());
    }

    @Test
    void testLogResquestDtoSettersAndGetters() {
        // Arrange
        LogResquestDto logRequestDto = new LogResquestDto(null, null, null, null, null,null,null);
        String usuario = "usuario_test";
        String fechaHora = "2024-05-21 21:51:37";
        String ip = "127.0.0.1";
        String transaccion = "12345";
        String valorModificado = "Valor modificado de prueba";

        // Act
        logRequestDto.setUsuario(usuario);
        logRequestDto.setFecha_hora(fechaHora);
        logRequestDto.setIp(ip);
        logRequestDto.setTransaccion(transaccion);
        logRequestDto.setValorModificado(valorModificado);

        // Assert
        assertEquals(usuario, logRequestDto.getUsuario());
        assertEquals(fechaHora, logRequestDto.getFecha_hora());
        assertEquals(ip, logRequestDto.getIp());
        assertEquals(transaccion, logRequestDto.getTransaccion());
        assertEquals(valorModificado, logRequestDto.getValorModificado());
    }

}