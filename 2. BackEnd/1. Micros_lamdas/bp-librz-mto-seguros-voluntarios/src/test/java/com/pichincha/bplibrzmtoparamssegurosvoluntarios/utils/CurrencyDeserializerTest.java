package com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyDeserializerTest {
    private CurrencyDeserializer currencyDeserializer;

    @BeforeEach
    public void setUp() {
        currencyDeserializer = new CurrencyDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        // Mocking JsonParser
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getText()).thenReturn("$1,234.56");

        // Mocking DeserializationContext
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        // Deserialize the mocked JSON value
        BigDecimal result = currencyDeserializer.deserialize(jsonParser, deserializationContext);

        // Assert the result is as expected
        assertThat(result).isEqualByComparingTo(new BigDecimal("1234.56"));
    }

    @Test
    public void testDeserializeWithoutCurrencySymbol() throws IOException {
        // Mocking JsonParser
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getText()).thenReturn("1234.56");

        // Mocking DeserializationContext
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        // Deserialize the mocked JSON value
        BigDecimal result = currencyDeserializer.deserialize(jsonParser, deserializationContext);

        // Assert the result is as expected
        assertThat(result).isEqualByComparingTo(new BigDecimal("1234.56"));
    }

}