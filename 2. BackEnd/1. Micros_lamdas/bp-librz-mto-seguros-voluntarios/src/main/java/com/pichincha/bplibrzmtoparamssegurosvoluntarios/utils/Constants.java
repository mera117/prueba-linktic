package com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils;

public class Constants {

    public class Message {

        public static final String MESSAGE_SUCESSFULL_SAVE = "Se ha guardado exitosamente la información";

        public static final String MESSAGE_SUCESSFULL_UPDATE = "Se ha actualizado exitosamente la información";

        public static final String MESSAGE_SUCESSFULL_DELETE = "Se ha eliminado exitosamente la información";

        public static final String MESSAGE_ERROR_SAVE = "Se ha generado un error inesperado";

        public static final String MESSAGE_ERROR_UPDATE = "No se ha Modificado su registro, presenta un error, favor validar";

        public static final String MESSAGE_ERROR_DELETE = "No se ha Eliminado su registro, presenta un error, favor validar";

        public static final String MESSAGE_ERROR_FINDBYID = "No existe el registro que desea modificar";

        public static final String EXCEPTION_MESSAGE = "Exception";

        public static final String MESSAGE_CREATE_ACCION = "Ingresar";
        public static final String MESSAGE_UPDATE_ACCION = "Modificar";
        public static final String MESSAGE_DELETE_ACCION = "Eliminar";

        private Message() {
            throw new UnsupportedOperationException();
        }
    }

    public class GeneralMessage {

        public static final String GENERAL_MESSAGE_SERVICE_NAME = "seguros-voluntarios";

        private GeneralMessage() {
            throw new UnsupportedOperationException();
        }
    }
}
