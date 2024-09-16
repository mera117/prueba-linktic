package com.pichincha.bplibrzmtorangotasas.model.dto;

import com.pichincha.bplibrzmtorangotasas.model.dto.response.ResponseGeneralPichincha;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResponseGeneralPichinchaTest {

    @Test
    void testAllArgsConstructor() {
        ResponseGeneralPichincha<String> response = new ResponseGeneralPichincha<>("Service1", 200, "Success", "Data");

        assertEquals("Service1", response.getServiceName());
        assertEquals(200, response.getStatusCode());
        assertEquals("Success", response.getMessage());
        assertEquals("Data", response.getData());
    }

    @Test
    void testNoArgsConstructor() {
        ResponseGeneralPichincha<String> response = new ResponseGeneralPichincha<>();

        assertNull(response.getServiceName());
        assertEquals(0, response.getStatusCode());
        assertNull(response.getMessage());
        assertNull(response.getData());

        response.setServiceName("Service2");
        response.setStatusCode(404);
        response.setMessage("Not Found");
        response.setData("No Data");

        assertEquals("Service2", response.getServiceName());
        assertEquals(404, response.getStatusCode());
        assertEquals("Not Found", response.getMessage());
        assertEquals("No Data", response.getData());
    }

    @Test
    void testGettersAndSetters() {
        ResponseGeneralPichincha<Integer> response = new ResponseGeneralPichincha<>();
        response.setServiceName("Service3");
        response.setStatusCode(500);
        response.setMessage("Internal Server Error");
        response.setData(123);

        assertEquals("Service3", response.getServiceName());
        assertEquals(500, response.getStatusCode());
        assertEquals("Internal Server Error", response.getMessage());
        assertEquals(123, response.getData());
    }
}