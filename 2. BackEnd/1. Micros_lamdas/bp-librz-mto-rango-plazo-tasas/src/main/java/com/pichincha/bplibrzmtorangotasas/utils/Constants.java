package com.pichincha.bplibrzmtorangotasas.utils;

public class Constants {

    public static class Message{

        public static final String MESSAGE_SUCESSFULL_SAVE="Se ha actualizado exitosamente la información";

        public static final String MESSAGE_SUCESSFULL_UPDATE="Se ha  modificado exitosamente la información";

        public static final String MESSAGE_SUCESSFULL_DELETE="Se ha eliminado exitosamente la información";

        public static final String MESSAGE_ERROR_SAVE="Se ha generado un error inesperado";

        public static final String MESSAGE_ERROR_UPDATE="No se ha Modificado su registro, presenta un error, favor validar ";

        public static final String MESSAGE_ERROR_DELETE="No se ha Eliminado su registro, presenta un error, favor validar";

        public static final String MESSAGE_ERROR_FINDBYID="No existe el registro que desea modificar";

        private Message() {
            throw new UnsupportedOperationException();
        }
    }

    public static class GeneralMessage{

         public  static final String GENERAL_MESSAGE_SERVICE_NAME="rangos-plazo-tasas";

         public static final String MESSAGE_ILEGAL_ARGUMEN="Rango vacío o nulo";

        public static final String MESSAGE_ILEGAL_ARGUMEN_PARSE="Rango inválido";
        public static final String MESSAGE_ILEGAL_ARGUMEN_EMPTY="No se encontraron elementos que coincidan con el rango proporcionado";

        private GeneralMessage() {
            throw new UnsupportedOperationException();
        }
    }
    private Constants() {
        throw new UnsupportedOperationException();
    }
}
