package com.pichincha.bplibrzmtorangotasas.domain.db.repositories;

import com.pichincha.bplibrzmtorangotasas.model.dto.TermRateRangeDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class TermRateRangeRepository {

	private final JdbcTemplate jdbcTemplate;

	@Value("${cloud.aws.parameter.store.table}")
	private String table;

	@Autowired
	public TermRateRangeRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<TermRateRangeDto> findAll() {
		StringBuilder stringBuilder = new StringBuilder("SELECT * FROM " + table + " ORDER BY id ASC");
		return jdbcTemplate.query(stringBuilder.toString(), new TermRateRangeDtoRowMapper());
	}

	public TermRateRangeDto findById(Long id) {
		StringBuilder stringBuilder = new StringBuilder("SELECT * FROM " + table + " WHERE id = ? ");
		String sql = stringBuilder.toString();
		return jdbcTemplate.query(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, id);
			}
		}, new ResultSetExtractor<TermRateRangeDto>() {
			@Override
			public TermRateRangeDto extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				return resultSet.next() ? new TermRateRangeDto(resultSet) : null;
			}
		});
	}

	public void save(TermRateRangeDto entity) {
		boolean stado = (entity.getStatusId() == 1l);
		StringBuilder stringBuilder = new StringBuilder(
				"INSERT INTO " + table + " (description_plazo,estado) VALUES (?,?);");
		jdbcTemplate.update(stringBuilder.toString(), entity.getDescriptionPlazo(), stado);

	}

	public void update(TermRateRangeDto entity) {
		boolean stado = (entity.getStatusId() == 1l);
		StringBuilder stringBuilder = new StringBuilder("UPDATE "+table+" SET description_plazo=?, estado=?  WHERE id=?");

		jdbcTemplate.update(stringBuilder.toString(),
				entity.getDescriptionPlazo(), stado, entity.getIdPlazo());
	}

	public void deleteById(Long id) {
		StringBuilder stringBuilder = new StringBuilder("DELETE FROM "+table+" WHERE id=?");
		jdbcTemplate.update(stringBuilder.toString(), id);
	}

	public List<TermRateRangeDto> findTermRateRangeByDescriptionActive() {
		StringBuilder stringBuilder = new StringBuilder("SELECT * FROM "+table+" where estado = true  ORDER BY id ASC");
		String query = stringBuilder.toString();
		return jdbcTemplate.query(query, new TermRateRangeDtoRowMapper());
	}

	public static class TermRateRangeDtoRowMapper implements RowMapper<TermRateRangeDto> {
		@Override
		public TermRateRangeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			TermRateRangeDto entity = new TermRateRangeDto();
			entity.setIdPlazo(rs.getLong("id"));
			entity.setDescriptionPlazo(rs.getString("description_plazo"));
			entity.setStatusId(rs.getBoolean("estado") ? 1l : 2l);
			entity.setStatusDescription(rs.getBoolean("estado") ? "Activo" : "Inactivo");
			return entity;
		}
	}

}