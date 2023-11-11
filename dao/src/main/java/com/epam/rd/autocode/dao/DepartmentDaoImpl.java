package com.epam.rd.autocode.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;

public class DepartmentDaoImpl implements DepartmentDao {
	ConnectionSource source;
	private static final String GET_ID = "SELECT * FROM DEPARTMENT WHERE ID = ?";
	private static final String GET_ALL = "SELECT * FROM DEPARTMENT";
	private static final String INSERT = "INSERT INTO "
			+ "DEPARTMENT (ID, NAME, LOCATION) VALUES (?, ?, ?)";
	private static final String UPDATE ="UPDATE DEPARTMENT SET NAME = ?, LOCATION = ? WHERE ID = ?";
	private static final String DELETE = "DELETE FROM DEPARTMENT WHERE ID = ?";
	
	public DepartmentDaoImpl() {
		super();
		this.source = ConnectionSource.instance();
	}

	@Override
	public Optional<Department> getById(BigInteger Id) {
		try (Connection connection = source.createConnection()){
			PreparedStatement statement = connection.prepareStatement(GET_ID);
			statement.setBigDecimal(1, new BigDecimal(Id));
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Department department = extractDepartmentFromResultSet(rs);
				return Optional.of(department);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return Optional.empty();
	}

	private Department extractDepartmentFromResultSet(ResultSet rs) throws SQLException {
		Department department = new Department(
				new BigInteger(rs.getString("ID")),
				rs.getString("NAME"),
				rs.getString("LOCATION"));
		return department;
	}

	@Override
	public List<Department> getAll() {
		List<Department> departments = new ArrayList<>();
		try (Connection connection = source.createConnection()){
			PreparedStatement statement = connection.prepareStatement(GET_ALL);			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Department department = extractDepartmentFromResultSet(rs);
				departments.add(department);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return departments;
	}

	@Override
	public Department save(Department t) {		
		return !getById(t.getId()).isPresent() ? insertDeprtment(t) : updateDeprtment(t); 
	}
	
	private Department insertDeprtment(Department department) {		
		try (Connection connection = source.createConnection()){
			PreparedStatement statement = connection.prepareStatement(INSERT);
			statement.setBigDecimal(1, new BigDecimal(department.getId()));
			statement.setString(2, department.getName());
			statement.setString(3, department.getLocation());
			statement.execute();
			
			return department;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private Department updateDeprtment(Department department) {
		try (Connection connection = source.createConnection()){
			PreparedStatement statement = connection.prepareStatement(UPDATE);			
			statement.setString(1, department.getName());
			statement.setString(2, department.getLocation());
			statement.setBigDecimal(3, new BigDecimal(department.getId()));
			statement.execute();
			
			return department;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(Department t) {
		try (Connection connection = source.createConnection()){
			PreparedStatement statement = connection.prepareStatement(DELETE);
			statement.setBigDecimal(1, new BigDecimal(t.getId()));
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

}
