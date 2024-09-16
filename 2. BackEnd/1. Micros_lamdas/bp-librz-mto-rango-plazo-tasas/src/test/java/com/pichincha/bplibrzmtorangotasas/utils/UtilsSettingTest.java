package com.pichincha.bplibrzmtorangotasas.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.bplibrzmtorangotasas.model.dto.request.LogResquestDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UtilsSettingTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(UtilsSettingTest.class);

    @Test
    void testObjectSqsMessage() throws JsonProcessingException {
        // Arrange
        String user = "testUser";
        String dateHour = "2024-05-21T21:51:37.553Z";
        String ip = "127.0.0.1";
        String idTransaction = "12345";
        String accion = "test";
        String data = "Test data";

        // Act
        String result = UtilsSetting.objectSqsMessage(user, dateHour, ip, idTransaction, data,accion,data);

        // Assert
        LogResquestDto expectedLogRequestDto = new LogResquestDto(user, dateHour,accion, ip, idTransaction, objectMapper.writeValueAsString(data),objectMapper.writeValueAsString(data));
        String expectedJson = objectMapper.writeValueAsString(expectedLogRequestDto);
        assertEquals(expectedJson, result);
    }

    @Test
    void testFindDifferences() {
        Long id = 1l;
        TestDto obj1 = new TestDto(1, "test1", "value1", "string", "2024-05-21T21:51:37.553Z");
        TestDto obj2 = new TestDto(1, "test2", "value1", "string", "2024-05-21T21:51:37.553Z");

        Map<String, Object> result = UtilsSetting.findDifferences(id, obj1, obj2,false);

        assertEquals(id, result.get("idPlazo"));
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test2", result.get("name"));
    }

    // Helper class for testing
    static class TestDto {
        private int id;
        private String name;
        private String value;
        private String statusDescription;  // This field should be excluded
        private String fechaCreacion;      // This field should be excluded

        public TestDto(int id, String name, String value, String statusDescription, String fechaCreacion) {
            this.id = id;
            this.name = name;
            this.value = value;
            this.statusDescription = statusDescription;
            this.fechaCreacion = fechaCreacion;
        }

        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getStatusDescription() { return statusDescription; }
        public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
        public String getFechaCreacion() { return fechaCreacion; }
        public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    }
}