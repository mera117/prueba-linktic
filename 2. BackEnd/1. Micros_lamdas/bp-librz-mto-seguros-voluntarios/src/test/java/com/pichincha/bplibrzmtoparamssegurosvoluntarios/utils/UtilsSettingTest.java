package com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.LogResquestDto;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.VoluntaryInsuranceCatalogDto;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UtilsSettingTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
        LogResquestDto expectedLogRequestDto = new LogResquestDto(user, dateHour, accion, ip, idTransaction, objectMapper.writeValueAsString(data),objectMapper.writeValueAsString(data));
        String expectedJson = objectMapper.writeValueAsString(expectedLogRequestDto);
        assertEquals(expectedJson, result);
    }


    @Test
    void testFindDifferences() {
        Integer id = 1;
        TestDto obj1 = new TestDto(1, "test1", "value1", "string", "2024-05-21T21:51:37.553Z");
        TestDto obj2 = new TestDto(1, "test2", "value1", "string", "2024-05-21T21:51:37.553Z");

        Map<String, Object> result = UtilsSetting.findDifferences(id, obj1, obj2, false);

        assertEquals(id, result.get("id"));
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
        private String fechaHora;     // This field should be excluded

        public TestDto(int id, String name, String value, String statusDescription, String fechaHora) {
            this.id = id;
            this.name = name;
            this.value = value;
            this.statusDescription = statusDescription;
            this.fechaHora = fechaHora;
        }

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getStatusDescription() {
            return statusDescription;
        }

        public void setStatusDescription(String statusDescription) {
            this.statusDescription = statusDescription;
        }

        public String getFechaCreacion() {
            return fechaHora;
        }

        public void setFechaCreacion(String fechaCreacion) {
            this.fechaHora = fechaCreacion;
        }
    }


    @Test
    void saveRegisternewTest() {
        // Instancia de la clase de ejemplo
        VoluntaryInsuranceCatalogDto obj = new VoluntaryInsuranceCatalogDto();
        obj.setId(1);
        obj.setNombrePlan("Ejemplo");
        obj.setUsuario("Usuario");

        // Método que se va a probar
        Map<String, Object> resultMap = UtilsSetting.saveRegisternew(1, obj);

        // Verificación de que los campos esperados están presentes en el mapa resultante
        assertTrue(resultMap.containsKey("nombrePlan"));
        assertFalse(resultMap.containsKey("Usuario")); // Este campo debe ser excluido

        // Verificación de que los valores son los esperados
        assertEquals("Ejemplo:", resultMap.get("nombrePlan"));
        assertEquals(1, resultMap.get("id"));
    }

    @Test
    void testFindDifferencesDelete() {

        JsonObject obj1 = new JsonObject();
        obj1.addProperty("estadoPlan", false);

        JsonObject obj2 = new JsonObject();
        obj2.addProperty("estadoPlan", true);

        Map<String, Object> resultOldData = UtilsSetting.findDifferencesDelete(5, obj1, obj2, true);

        assertEquals(5, resultOldData.get("id"));
        assertEquals("false", resultOldData.get("estadoPlan"));

        Map<String, Object> resultNewData = UtilsSetting.findDifferencesDelete(5, obj1, obj2, false);

        assertEquals(5, resultNewData.get("id"));
        assertEquals("true", resultNewData.get("estadoPlan"));

        JsonObject obj3 = new JsonObject();
        obj3.addProperty("estadoPlan", false);

        JsonObject obj4 = new JsonObject();
        obj4.addProperty("estadoPlan", false);

        Map<String, Object> resultNoDiff = UtilsSetting.findDifferencesDelete(5, obj3, obj4, true);
        assertEquals(1, resultNoDiff.size());
        assertEquals(5, resultNoDiff.get("id"));
        assertNull(resultNoDiff.get("estadoPlan"));
    }
}
