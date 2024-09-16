package com.pichincha.bplibrzmtoparamssegurosvoluntarios.domain.bd.repositories;

import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.VoluntaryInsuranceCatalogDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VoluntaryInsuranceCatalogRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${cloud.aws.parameter.store.table}")
    private String table;

    public List<VoluntaryInsuranceCatalogDto> findAll() {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ");
        queryBuilder.append(table);
        queryBuilder.append(" WHERE estado = true ORDER BY id ASC");
        return jdbcTemplate.query(queryBuilder.toString(), new VoluntaryInsuranceCatalogRowMapper());
    }

    public VoluntaryInsuranceCatalogDto findById(Integer id) {

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ");
        queryBuilder.append(table);
        queryBuilder.append(" WHERE id=?");
        return jdbcTemplate.query(queryBuilder.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, id);
            }
        }, new ResultSetExtractor<VoluntaryInsuranceCatalogDto>() {
            @Override
            public VoluntaryInsuranceCatalogDto extractData(ResultSet resultSet)
                    throws SQLException, DataAccessException {
                return resultSet.next() ? getPs(resultSet) : null;
            }
        });
    }

    public Long save(VoluntaryInsuranceCatalogDto entity) {

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(table);
        queryBuilder.append("(nombre_plan, valor_prima, fallecimiento_accidental, ");
        queryBuilder.append("inhabilitacion_total, enfermedades_graves, canasta_fallecimiento, ");
        queryBuilder.append("estado_plan, asalariado, independiente,  ");
        queryBuilder.append("pensionado, edad_minima, edad_minima_dias, ");
        queryBuilder.append("edad_maxima, edad_maxima_dias, estado) ");
        queryBuilder.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true);");
        jdbcTemplate.update(queryBuilder.toString(), entity.getNombrePlan(), entity.getValorPrima(),
                entity.getFallecimientoAccidental(), entity.getInhabilitacionTotal(), entity.getEnfermedadesGraves(),
                entity.getCanastaFallecimiento(), entity.getEstadoPlan(), entity.getAsalariado(),
                entity.getIndependiente(), entity.getPensionado(), entity.getEdadMinima(), entity.getEdadMinimaDias(),
                entity.getEdadMaxima(), entity.getEdadMaximaDias());
        queryBuilder = new StringBuilder("SELECT id FROM " + table + " WHERE id=(SELECT MAX(id) FROM " + table + ")");

        return jdbcTemplate.queryForObject(queryBuilder.toString(), Long.class);
    }

    public void update(VoluntaryInsuranceCatalogDto entity) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE ");
        queryBuilder.append(table);
        queryBuilder.append(" SET ");
        queryBuilder.append("nombre_plan=?, valor_prima=?, ");
        queryBuilder.append("fallecimiento_accidental=?, inhabilitacion_total=?, ");
        queryBuilder.append("enfermedades_graves=?, canasta_fallecimiento=?, ");
        queryBuilder.append("estado_plan=?, asalariado=?, independiente=?, ");
        queryBuilder.append("pensionado=?, edad_minima=?, edad_minima_dias=?, ");
        queryBuilder.append("edad_maxima=?, edad_maxima_dias=? ");
        queryBuilder.append("WHERE id=?");
        jdbcTemplate.update(queryBuilder.toString(), entity.getNombrePlan(), entity.getValorPrima(),
                entity.getFallecimientoAccidental(), entity.getInhabilitacionTotal(), entity.getEnfermedadesGraves(),
                entity.getCanastaFallecimiento(), entity.getEstadoPlan(), entity.getAsalariado(),
                entity.getIndependiente(), entity.getPensionado(), entity.getEdadMinima(), entity.getEdadMinimaDias(),
                entity.getEdadMaxima(), entity.getEdadMaximaDias(), entity.getId());
    }

    public void updateStatus(VoluntaryInsuranceCatalogDto entity) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE ");
        queryBuilder.append(table);
        queryBuilder.append(" SET ");
        queryBuilder.append(" estado=? , estado_plan=? ");
        queryBuilder.append("WHERE id=?");
        jdbcTemplate.update(queryBuilder.toString(),
                entity.getEstado(),
                entity.getEstadoPlan(),
                entity.getId());
    }

    public void deleteById(Integer id) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE ");
        queryBuilder.append(table);
        queryBuilder.append(" SET ");
        queryBuilder.append("estado=? ");
        queryBuilder.append("WHERE id=? ");
        jdbcTemplate.update(queryBuilder.toString(), false, id);
    }

    public VoluntaryInsuranceCatalogDto getPs(ResultSet resultSet) throws SQLException {
        VoluntaryInsuranceCatalogDto entity = new VoluntaryInsuranceCatalogDto();
        entity.setId(resultSet.getInt("id"));
        entity.setNombrePlan(resultSet.getString("nombre_plan"));
        entity.setValorPrima(convertToBigDecimal(resultSet.getString("valor_prima")));
        entity.setFallecimientoAccidental(convertToBigDecimal(resultSet.getString("fallecimiento_accidental")));
        entity.setInhabilitacionTotal(convertToBigDecimal(resultSet.getString("inhabilitacion_total")));
        entity.setEnfermedadesGraves(convertToBigDecimal(resultSet.getString("enfermedades_graves")));
        entity.setCanastaFallecimiento(convertToBigDecimal(resultSet.getString("canasta_fallecimiento")));
        entity.setEstado(resultSet.getBoolean("estado"));
        entity.setEstadoPlan(resultSet.getBoolean("estado_plan"));
        entity.setAsalariado(resultSet.getBoolean("asalariado"));
        entity.setIndependiente(resultSet.getBoolean("independiente"));
        entity.setPensionado(resultSet.getBoolean("pensionado"));
        entity.setEdadMinima(resultSet.getInt("edad_minima"));
        entity.setEdadMinimaDias(resultSet.getInt("edad_minima_dias"));
        entity.setEdadMaxima(resultSet.getInt("edad_maxima"));
        entity.setEdadMaximaDias(resultSet.getInt("edad_maxima_dias"));
        return entity;
    }

    public class VoluntaryInsuranceCatalogRowMapper implements RowMapper<VoluntaryInsuranceCatalogDto> {
        @Override
        public VoluntaryInsuranceCatalogDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            VoluntaryInsuranceCatalogDto entity = new VoluntaryInsuranceCatalogDto();
            entity.setId(rs.getInt("id"));
            entity.setNombrePlan(rs.getString("nombre_plan"));
            entity.setValorPrima(convertToBigDecimal(rs.getString("valor_prima")));
            entity.setFallecimientoAccidental(convertToBigDecimal(rs.getString("fallecimiento_accidental")));
            entity.setInhabilitacionTotal(convertToBigDecimal(rs.getString("inhabilitacion_total")));
            entity.setEnfermedadesGraves(convertToBigDecimal(rs.getString("enfermedades_graves")));
            entity.setCanastaFallecimiento(convertToBigDecimal(rs.getString("canasta_fallecimiento")));
            entity.setEstado(rs.getBoolean("estado"));
            entity.setEstadoPlan(rs.getBoolean("estado_plan"));
            entity.setAsalariado(rs.getBoolean("asalariado"));
            entity.setIndependiente(rs.getBoolean("independiente"));
            entity.setPensionado(rs.getBoolean("pensionado"));
            entity.setEdadMinima(rs.getInt("edad_minima"));
            entity.setEdadMinimaDias(rs.getInt("edad_minima_dias"));
            entity.setEdadMaxima(rs.getInt("edad_maxima"));
            entity.setEdadMaximaDias(rs.getInt("edad_maxima_dias"));

            return entity;
        }

    }

    private BigDecimal convertToBigDecimal(String value) throws SQLException {
        try {
            String sanitizedValue = value.replaceAll("[^\\d,.,-]", "");
            if (sanitizedValue.contains(",") && sanitizedValue.contains(".")) {
                if (sanitizedValue.lastIndexOf(",") > sanitizedValue.lastIndexOf(".")) {
                    sanitizedValue = sanitizedValue.replace(".", "").replace(",", ".");
                } else {
                    sanitizedValue = sanitizedValue.replace(",", "");
                }
            } else if (sanitizedValue.contains(",")) {
                sanitizedValue = sanitizedValue.replace(",", ".");
            }
            return new BigDecimal(sanitizedValue);
        } catch (NumberFormatException e) {
            throw new SQLException("Error al convertir valor a BigDecimal: " + value, e);
        }
    }

}