package com.pichincha.bplibrzmtorangotasas.domain.db.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


import com.pichincha.bplibrzmtorangotasas.model.dto.TermRateRangeDto;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(MockitoExtension.class)
public class TermRateRangeRepositoryV2Test {
	@Mock
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
    private TermRateRangeRepository repository;
    
	@Test
	void testFindById() 
	{
		jdbcTemplate.execute(
				"INSERT INTO con_rango_plazo (id,minimo,estado,maximo,description_plazo) VALUES(5,1, true, 10,'1-10')");
		TermRateRangeDto result = repository.findById(1L);
		assertNotNull(result);	
	}
	@Test
	void testfindTermRateRangeByDescriptionActive() 
	{
		jdbcTemplate.execute(
				"INSERT INTO con_rango_plazo (id,minimo,estado,maximo) VALUES(5,1, true, 10)");
		List<TermRateRangeDto> result = repository.findTermRateRangeByDescriptionActive();
		assertTrue(result.size()>0);	
	}
}
