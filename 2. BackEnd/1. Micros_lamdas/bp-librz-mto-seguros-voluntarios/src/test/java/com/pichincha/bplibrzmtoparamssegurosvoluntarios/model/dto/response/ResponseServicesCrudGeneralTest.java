package com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseServicesCrudGeneralTest {
    @Test
    void testGettersAndSetters() {
        ResponseServicesCrudGeneral response = new ResponseServicesCrudGeneral();
        response.setMessage("Operation successful");

        assertEquals("Operation successful", response.getMessage());
    }

    @Test
    void testNoArgsConstructor() {
        ResponseServicesCrudGeneral response = new ResponseServicesCrudGeneral();

        assertNull(response.getMessage());
    }
}