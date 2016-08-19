package me.quanli.report.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import me.quanli.commons.util.PropertiesUtils;

@Component
public class TargetDao {

    private NamedParameterJdbcTemplate template;

    private void buildTemplate() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(PropertiesUtils.getProperty("dataSource.target.url"));
        dataSource.setUsername(PropertiesUtils.getProperty("dataSource.target.user"));
        dataSource.setPassword(PropertiesUtils.getProperty("dataSource.target.password"));
        template = new NamedParameterJdbcTemplate(dataSource);
    }

    public NamedParameterJdbcTemplate getTemplate() {
        if (template == null) {
            buildTemplate();
        }
        return template;
    }

    public SqlRowSet queryForRowSet(String sql, Map<String, Object> parameters) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        for (Entry<String, Object> parameter : parameters.entrySet()) {
            paramSource.addValue(parameter.getKey(), parameter.getValue());
        }
        return getTemplate().queryForRowSet(sql, paramSource);
    }

    public List<Map<String, Object>> queryForList(String sql, Map<String, Object> parameters) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        for (Entry<String, Object> parameter : parameters.entrySet()) {
            paramSource.addValue(parameter.getKey(), parameter.getValue());
        }
        return getTemplate().queryForList(sql, paramSource);
    }

}
