package com.pichincha.bplibrzmtorangotasas.domain.db.repositories;

import com.pichincha.bplibrzmtorangotasas.model.dto.TermRateRangeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class TermRateRangeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private TermRateRangeRepository repository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(createDto()));

        List<TermRateRangeDto> result = repository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1-10", result.get(0).getDescriptionPlazo());
    }

   
    @Test
    void save() {
        TermRateRangeDto dto = createDto();

        repository.save(dto);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);

        verify(jdbcTemplate, times(1)).update(sqlCaptor.capture(), paramsCaptor.capture());

        assertEquals("INSERT INTO null (description_plazo,estado) VALUES (?,?);",
                sqlCaptor.getValue());

        Object[] params = paramsCaptor.getValue();
        assertEquals(2, params.length);
        assertEquals("1-10", params[0]);
        assertEquals(true, params[1]);
       

    }


    @Test
    void update() {
        TermRateRangeDto dto = createDto();

        repository.update(dto);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);

        verify(jdbcTemplate, times(1)).update(sqlCaptor.capture(), paramsCaptor.capture());

        assertEquals("UPDATE null SET description_plazo=?, estado=?  WHERE id=?",
                sqlCaptor.getValue());

        Object[] params = paramsCaptor.getValue();
        assertEquals(3, params.length);
        assertEquals("1-10", params[0]);
        assertEquals(true, params[1]);
    }
    @Test
    void deleteById() {
        repository.deleteById(1L);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }



    private TermRateRangeDto createDto() {
        TermRateRangeDto dto = new TermRateRangeDto();
        dto.setDescriptionPlazo("1-10");
        dto.setStatusId(1L);
        return dto;
    }
}
