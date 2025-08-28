package com.interiordesign.dao;

import com.interiordesign.model.Furniture;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Simple DAO using JdbcTemplate to read furniture data.
 */
@Repository
public class FurnitureDAO {

    private final JdbcTemplate jdbcTemplate;

    public FurnitureDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Furniture> furnitureRowMapper = (rs, rowNum) -> {
        Furniture f = new Furniture();
        f.setId(rs.getLong("id"));
        f.setName(rs.getString("name"));
        f.setWidth(rs.getDouble("width"));
        f.setDepth(rs.getDouble("depth"));
        f.setPrice(rs.getInt("price"));
        f.setCategory(rs.getString("category"));
        return f;
    };

    public List<Furniture> findAll() {
        String sql = "SELECT id, name, width, depth, price, category FROM furniture ORDER BY id";
        return jdbcTemplate.query(sql, furnitureRowMapper);
    }

    public Furniture findByCategory(String category) {
        String sql = "SELECT id, name, width, depth, price, category FROM furniture WHERE category = ? LIMIT 1";
        List<Furniture> list = jdbcTemplate.query(sql, furnitureRowMapper, category);
        return list.isEmpty() ? null : list.get(0);
    }
}
