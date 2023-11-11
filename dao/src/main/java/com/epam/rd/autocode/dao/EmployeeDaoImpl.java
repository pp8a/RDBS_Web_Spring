package com.epam.rd.autocode.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

public class EmployeeDaoImpl implements EmployeeDao{	
	private final ConnectionSource source;
	private static final String GET_ID = "SELECT * FROM EMPLOYEE WHERE ID = ?";
	private static final String GET_ALL = "SELECT * FROM EMPLOYEE";
	private static final String DELETE = "DELETE FROM EMPLOYEE WHERE ID = ?";
	private static final String INSERT = "INSERT INTO "
			+ "EMPLOYEE (ID, FIRSTNAME, LASTNAME, MIDDLENAME, POSITION, MANAGER, HIREDATE, SALARY, DEPARTMENT) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE EMPLOYEE "
			+ "SET FIRSTNAME = ?, LASTNAME = ?, MIDDLENAME = ?, POSITION = ?, MANAGER = ?, HIREDATE = ?, SALARY = ?, DEPARTMENT = ? "
			+ "WHERE ID = ?";
	
	private static final String FINDBYDEPT = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = ?";
	private static final String FINDBYMGR = "SELECT * FROM EMPLOYEE WHERE MANAGER = ?";

	public EmployeeDaoImpl() {
		super();
		this.source = ConnectionSource.instance();
	}

	@Override
	public Optional<Employee> getById(BigInteger Id) {
		try(Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(GET_ID);
			statement.setBigDecimal(1, new BigDecimal(Id));
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()) {
				Employee employee = extractEmployeeFromResultSet(resultSet);
				return Optional.of(employee);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Error connection ", e);
		}
		return Optional.empty();
	}

	@Override
	public List<Employee> getAll() {
		List<Employee> employees = new ArrayList<>();
		try(Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(GET_ALL);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Employee employee = extractEmployeeFromResultSet(resultSet);
				employees.add(employee);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Error connection ", e);
		}
		return employees;
	}

	@Override
	public Employee save(Employee t) {
		return !getById(t.getId()).isPresent() ? insertEmployee(t) : updateEmployee(t);
	}
	
	private Employee insertEmployee(Employee employee) {
		try(Connection connection = source.createConnection()){
			PreparedStatement statement = connection.prepareStatement(INSERT);
			statement.setBigDecimal(1, new BigDecimal(employee.getId()));
			statement.setString(2, employee.getFullName().getFirstName());			
			statement.setString(3, employee.getFullName().getLastName());
			statement.setString(4, employee.getFullName().getMiddleName());
			statement.setString(5, employee.getPosition().name());
			statement.setBigDecimal(6, employee.getManagerId() != null ? new BigDecimal(employee.getManagerId()) : BigDecimal.ZERO);
			statement.setDate(7, Date.valueOf(employee.getHired()));				
			statement.setBigDecimal(8, employee.getSalary());
			statement.setBigDecimal(9, new BigDecimal(employee.getDepartmentId()));		
			
			statement.execute();
			
			return employee;
			
		} catch (SQLException e) {
			throw new RuntimeException("Error connection ", e);
	    }
		
	}
	
	private Employee updateEmployee(Employee employee) {
		try(Connection connection = source.createConnection()){
			PreparedStatement statement = connection.prepareStatement(UPDATE);				
			statement.setString(1, employee.getFullName().getFirstName());
			statement.setString(2, employee.getFullName().getLastName());
			statement.setString(3, employee.getFullName().getMiddleName());
			statement.setString(4, employee.getPosition().name());
			statement.setBigDecimal(5, employee.getManagerId() != null ? new BigDecimal(employee.getManagerId()) : BigDecimal.ZERO);
			statement.setDate(6, Date.valueOf(employee.getHired()));				
			statement.setBigDecimal(7, employee.getSalary());
			statement.setBigDecimal(8, new BigDecimal(employee.getDepartmentId()));
			
			statement.setBigDecimal(9, new BigDecimal(employee.getId()));
			statement.executeUpdate();
			
			return employee;
		} catch (SQLException e) {
			throw new RuntimeException("Error connection ", e);
	    }
	}

	@Override
	public void delete(Employee t) {
		try(Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(DELETE);
			statement.setBigDecimal(1, new BigDecimal(t.getId()));
			statement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Error connection ", e);
		}
		
	}

	@Override
	public List<Employee> getByDepartment(Department department) {
		List<Employee> employees = new ArrayList<>();
		try(Connection connection = source.createConnection()){
			PreparedStatement statement = connection.prepareStatement(FINDBYDEPT);
			statement.setBigDecimal(1, new BigDecimal(department.getId()));
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Employee employee = extractEmployeeFromResultSet(rs);
				employees.add(employee);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Error connection ", e);
		}
		return employees;
	}

	@Override
	public List<Employee> getByManager(Employee employee) {
		List<Employee> employees = new ArrayList<>();
		try (Connection connection = source.createConnection()){
			PreparedStatement statement = connection.prepareStatement(FINDBYMGR);
			statement.setBigDecimal(1, new BigDecimal(employee.getId()));			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Employee manager = extractEmployeeFromResultSet(rs);
				employees.add(manager);				
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Error connection ", e);
		}
		return employees;
	}

	private Employee extractEmployeeFromResultSet(ResultSet resultSet) {
		try {
			return new Employee(new BigInteger(resultSet.getString("ID")),
					new FullName(resultSet.getString("FIRSTNAME"),
							resultSet.getString("LASTNAME"),
							resultSet.getString("MIDDLENAME")),
					Position.valueOf(resultSet.getString("POSITION")),
					resultSet.getDate("HIREDATE").toLocalDate(),
					resultSet.getBigDecimal("SALARY"),
					resultSet.getBigDecimal("MANAGER") != null ? new BigInteger(resultSet.getString("MANAGER")) : BigInteger.ZERO,
					resultSet.getBigDecimal("DEPARTMENT") !=null ? new BigInteger(resultSet.getString("DEPARTMENT")) : BigInteger.ZERO
					);			
		} catch (SQLException e) {
			throw new RuntimeException("Get employees from database", e);
		}
		
	}
	
}
