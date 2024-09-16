package com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.LogResquestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.*;

public class UtilsSetting {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String objectSqsMessage(String user, String dateHour, String ip, String idTransaction, Object newData,
                                          String accion, Object oldData) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new LogResquestDto(user, dateHour, accion, ip, idTransaction,
                newData != null ? objectMapper.writeValueAsString(newData) : "", oldData != null ? objectMapper.writeValueAsString(oldData) : ""));
    }

    public static Map<String, Object> saveRegisternew(Integer id, Object obj) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        final Set<String> excludeFields = new HashSet<>(Arrays.asList(
                "usuario", "fechaHora", "statusDescription", "ip", "id"
        ));

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (excludeFields.contains(fieldName)) {
                continue;
            }
            try {
                Object value = field.get(obj);
                if (value != null) {
                    resultMap.put(fieldName, value + ":" + "");
                }
            } catch (IllegalAccessException e) {
                resultMap.put(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage());
                return resultMap;
            }
        }
        return resultMap;
    }


    public static Map<String, Object> findDifferences(Integer id, Object obj1, Object obj2, boolean isOldData) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);

        final Set<String> EXCLUDED_FIELDS = new HashSet<>(Arrays.asList(
                "usuario", "fechaHora", "statusDescription", "ip"
        ));

        // Convert objects to JSON objects using Gson
        Gson gson = new Gson();
        JsonObject json1 = gson.toJsonTree(obj1).getAsJsonObject();
        JsonObject json2 = gson.toJsonTree(obj2).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : json1.entrySet()) {
            String key = entry.getKey();
            if (EXCLUDED_FIELDS.contains(key)) {
                continue;
            }
            JsonElement value1 = entry.getValue();
            JsonElement value2 = json2.get(key);

            if (value2 != null && !value1.equals(value2)) {
                String jsonAjuste;
                if (isOldData) {
                    jsonAjuste = (value1.toString()).replace("\"", "");
                } else {
                    jsonAjuste = (value2.toString()).replace("\"", "");
                }
                resultMap.put(key, jsonAjuste);
            }
        }

        return resultMap;
    }

    public static Map<String, Object> findDifferencesDelete(Integer id, Object obj1, Object obj2, boolean isOldData) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);

        // Only include "estado_plan" field
        final String INCLUDED_FIELD = "estadoPlan";

        Gson gson = new Gson();
        JsonObject json1 = gson.toJsonTree(obj1).getAsJsonObject();
        JsonObject json2 = gson.toJsonTree(obj2).getAsJsonObject();

        JsonElement value1 = json1.get(INCLUDED_FIELD);
        JsonElement value2 = json2.get(INCLUDED_FIELD);

        if (value1 != null && value2 != null && !value1.equals(value2)) {
            String jsonAjuste = isOldData ? value1.toString().replace("\"", "") :
                    value2.toString().replace("\"", "");
            resultMap.put(INCLUDED_FIELD, jsonAjuste);
        }

        return resultMap;
    }

}
