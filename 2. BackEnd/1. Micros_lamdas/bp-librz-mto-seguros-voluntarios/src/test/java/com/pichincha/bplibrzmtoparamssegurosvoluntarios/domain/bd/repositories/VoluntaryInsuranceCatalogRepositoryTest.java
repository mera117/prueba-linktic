package com.pichincha.bplibrzmtoparamssegurosvoluntarios.domain.bd.repositories;

import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.VoluntaryInsuranceCatalogDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class VoluntaryInsuranceCatalogRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private ResultSet resultSet;

	@InjectMocks
	private VoluntaryInsuranceCatalogRepository repository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindAll() {

		List<VoluntaryInsuranceCatalogDto> expectedList = Collections.emptyList();

		when(jdbcTemplate.query(anyString(),
				any(VoluntaryInsuranceCatalogRepository.VoluntaryInsuranceCatalogRowMapper.class)))
				.thenReturn(expectedList);

		List<VoluntaryInsuranceCatalogDto> resultList = repository.findAll();

		assertEquals(expectedList, resultList);
		;
	}

	@Test
	void testFindById() {

		Integer id = 1;
		VoluntaryInsuranceCatalogDto expectedDto = new VoluntaryInsuranceCatalogDto();

		when(jdbcTemplate.query(anyString(), any(PreparedStatementSetter.class), any(ResultSetExtractor.class)))
				.thenReturn(expectedDto);

		VoluntaryInsuranceCatalogDto resultDto = repository.findById(id);

		assertEquals(expectedDto, resultDto);
	}

	@Test
	void testSave() {
		VoluntaryInsuranceCatalogDto dto = createSampleDto();

		when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

		repository.save(dto);

		verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));

	}

	@Test
	void testDeleteById() {
		Integer id = 1;
		when(jdbcTemplate.update(anyString(), eq(false), eq(id))).thenReturn(1);
		repository.deleteById(id);
		verify(jdbcTemplate, times(1)).update(anyString(), eq(false), eq(id));
	}

	@Test
	void testUpdate() {

		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any())).thenReturn(1);

		VoluntaryInsuranceCatalogDto dto = createSampleDto();

		assertDoesNotThrow(() -> {
			repository.update(dto);

		});
	}

	@Test
	void testGetPs() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);

		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getString("nombre_plan")).thenReturn("Plan A");
		when(resultSet.getString("valor_prima")).thenReturn("100.85");
		when(resultSet.getString("fallecimiento_accidental")).thenReturn("20.3");
		when(resultSet.getString("inhabilitacion_total")).thenReturn("21.3");
		when(resultSet.getString("enfermedades_graves")).thenReturn("22.3");
		when(resultSet.getString("canasta_fallecimiento")).thenReturn("23.3");

		when(resultSet.getBoolean("estado")).thenReturn(true);
		when(resultSet.getBoolean("asalariado")).thenReturn(true);
		when(resultSet.getBoolean("independiente")).thenReturn(true);
		when(resultSet.getBoolean("pensionado")).thenReturn(true);

		when(resultSet.getInt("edad_minima")).thenReturn(18);
		when(resultSet.getInt("edad_minima_dias")).thenReturn(0);
		when(resultSet.getInt("edad_maxima")).thenReturn(80);
		when(resultSet.getInt("edad_maxima_dias")).thenReturn(364);

		VoluntaryInsuranceCatalogDto resultDto = repository.getPs(resultSet);
		assertNotNull(resultDto);
		assertEquals(1, resultDto.getId());
		assertEquals("Plan A", resultDto.getNombrePlan());
		assertEquals(BigDecimal.valueOf(100.85), resultDto.getValorPrima());
		assertEquals(BigDecimal.valueOf(20.3), resultDto.getFallecimientoAccidental());
		assertEquals(BigDecimal.valueOf(21.3), resultDto.getInhabilitacionTotal());
		assertEquals(BigDecimal.valueOf(22.3), resultDto.getEnfermedadesGraves());
		assertEquals(BigDecimal.valueOf(23.3), resultDto.getCanastaFallecimiento());
		assertTrue(resultDto.getEstado());
		assertTrue(resultDto.getAsalariado());
		assertTrue(resultDto.getIndependiente());
		assertTrue(resultDto.getPensionado());
		assertEquals(18, resultDto.getEdadMinima());
		assertEquals(0, resultDto.getEdadMinimaDias());
		assertEquals(80, resultDto.getEdadMaxima());
		assertEquals(364, resultDto.getEdadMaximaDias());
	}

	@Test
	void testMapRow() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);

		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getString("nombre_plan")).thenReturn("Plan A");
		when(resultSet.getString("valor_prima")).thenReturn("100.85");
		when(resultSet.getString("fallecimiento_accidental")).thenReturn("20.3");
		when(resultSet.getString("inhabilitacion_total")).thenReturn("21.3");
		when(resultSet.getString("enfermedades_graves")).thenReturn("22.3");
		when(resultSet.getString("canasta_fallecimiento")).thenReturn("23.3");

		when(resultSet.getBoolean("estado")).thenReturn(true);
		when(resultSet.getBoolean("asalariado")).thenReturn(true);
		when(resultSet.getBoolean("independiente")).thenReturn(true);
		when(resultSet.getBoolean("pensionado")).thenReturn(true);

		when(resultSet.getInt("edad_minima")).thenReturn(18);
		when(resultSet.getInt("edad_minima_dias")).thenReturn(0);
		when(resultSet.getInt("edad_maxima")).thenReturn(80);
		when(resultSet.getInt("edad_maxima_dias")).thenReturn(364);

		VoluntaryInsuranceCatalogDto resultDto = repository.new VoluntaryInsuranceCatalogRowMapper().mapRow(resultSet,
				1);

		assertNotNull(resultDto);
		assertEquals(1, resultDto.getId());
		assertEquals("Plan A", resultDto.getNombrePlan());
		assertEquals(BigDecimal.valueOf(100.85), resultDto.getValorPrima());
		assertEquals(BigDecimal.valueOf(20.3), resultDto.getFallecimientoAccidental());
		assertEquals(BigDecimal.valueOf(21.3), resultDto.getInhabilitacionTotal());
		assertEquals(BigDecimal.valueOf(22.3), resultDto.getEnfermedadesGraves());
		assertEquals(BigDecimal.valueOf(23.3), resultDto.getCanastaFallecimiento());
		assertTrue(resultDto.getEstado());
		assertTrue(resultDto.getAsalariado());
		assertTrue(resultDto.getIndependiente());
		assertTrue(resultDto.getPensionado());
		assertEquals(18, resultDto.getEdadMinima());
		assertEquals(0, resultDto.getEdadMinimaDias());
		assertEquals(80, resultDto.getEdadMaxima());
		assertEquals(364, resultDto.getEdadMaximaDias());
	}

	private VoluntaryInsuranceCatalogDto createSampleDto() {
		VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
		dto.setId(1);
		dto.setNombrePlan("Plan 1");
		dto.setValorPrima(BigDecimal.valueOf(100.85));
		dto.setFallecimientoAccidental(BigDecimal.valueOf(20.3));
		dto.setInhabilitacionTotal(BigDecimal.valueOf(21.3));
		dto.setEnfermedadesGraves(BigDecimal.valueOf(22.3));
		dto.setCanastaFallecimiento(BigDecimal.valueOf(23.3));
		dto.setEstado(true);
		dto.setAsalariado(true);
		dto.setIndependiente(true);
		dto.setPensionado(true);
		dto.setEdadMinima(18);
		dto.setEdadMinimaDias(0);
		dto.setEdadMaxima(80);
		dto.setEdadMaximaDias(364);
		return dto;
	}

	@Test
	void testConvertToBigDecimal_validNumber() throws Exception {
		Method method = VoluntaryInsuranceCatalogRepository.class.getDeclaredMethod("convertToBigDecimal",
				String.class);
		method.setAccessible(true);

		String value = "100.85";
		BigDecimal result = (BigDecimal) method.invoke(repository, value);
		assertEquals(BigDecimal.valueOf(100.85), result);
	}

	@Test
	void testConvertToBigDecimal_withComma() throws Exception {
		Method method = VoluntaryInsuranceCatalogRepository.class.getDeclaredMethod("convertToBigDecimal",
				String.class);
		method.setAccessible(true);

		String value = "1.234,56";
		BigDecimal result = (BigDecimal) method.invoke(repository, value);
		assertEquals(BigDecimal.valueOf(1234.56), result);
	}

	@Test
	void testConvertToBigDecimal_withThousandsSeparator() throws Exception {
		Method method = VoluntaryInsuranceCatalogRepository.class.getDeclaredMethod("convertToBigDecimal",
				String.class);
		method.setAccessible(true);

		String value = "1,000.0";
		BigDecimal result = (BigDecimal) method.invoke(repository, value);
		assertEquals(BigDecimal.valueOf(1000.00), result);
	}

	@Test
	void testConvertToBigDecimal_withNonNumericCharacters() throws Exception {
		Method method = VoluntaryInsuranceCatalogRepository.class.getDeclaredMethod("convertToBigDecimal",
				String.class);
		method.setAccessible(true);

		String value = "$ 1.234,56";
		BigDecimal result = (BigDecimal) method.invoke(repository, value);
		assertEquals(BigDecimal.valueOf(1234.56), result);
	}

	@Test
	void testConvertToBigDecimal_invalidNumber() throws Exception {
		Method method = VoluntaryInsuranceCatalogRepository.class.getDeclaredMethod("convertToBigDecimal",
				String.class);
		method.setAccessible(true);

		String value = "invalid";

		try {
			method.invoke(repository, value);
			fail("Expected SQLException was not thrown");
		} catch (InvocationTargetException e) {
			assertTrue(e.getCause() instanceof SQLException);
			assertTrue(e.getCause().getCause() instanceof NumberFormatException);
		}
	}

	@Test
	public void testUpdateStatus() {
		VoluntaryInsuranceCatalogDto dto = new VoluntaryInsuranceCatalogDto();
		dto.setId(5);
		dto.setEstado(false);
		dto.setEstadoPlan(true);

		repository.updateStatus(dto);
	}

}