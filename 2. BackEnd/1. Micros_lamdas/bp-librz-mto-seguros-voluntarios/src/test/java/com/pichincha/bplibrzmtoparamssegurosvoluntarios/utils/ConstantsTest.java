package com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTest {

    @Test
    public void testMessageConstants() {
        assertEquals("Se ha guardado exitosamente la información", Constants.Message.MESSAGE_SUCESSFULL_SAVE);

        assertEquals("Se ha actualizado exitosamente la información", Constants.Message.MESSAGE_SUCESSFULL_UPDATE);
        assertEquals("Se ha eliminado exitosamente la información", Constants.Message.MESSAGE_SUCESSFULL_DELETE);
        assertEquals("Se ha generado un error inesperado", Constants.Message.MESSAGE_ERROR_SAVE);
        assertEquals("No se ha Modificado su registro, presenta un error, favor validar", Constants.Message.MESSAGE_ERROR_UPDATE);
        assertEquals("No se ha Eliminado su registro, presenta un error, favor validar", Constants.Message.MESSAGE_ERROR_DELETE);
        assertEquals("No existe el registro que desea modificar", Constants.Message.MESSAGE_ERROR_FINDBYID);
    }

    @Test
    public void testGeneralMessageConstants() {
       // assertEquals("estes es el valor del secreto", Constants.GeneralMessage.GENERAL_MESSAGE_LOGS_SERCRET);
        assertEquals("seguros-voluntarios", Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME);
    }
}
