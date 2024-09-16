package com.pichincha.bplibrzmtorangotasas.model.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGeneralPichincha<T> {

    public static final String SERVICE_NAME_ATTRIBUTE = "serviceName";
    private String  serviceName;
    private int statusCode;
    private String message;
    private Object data;
}
