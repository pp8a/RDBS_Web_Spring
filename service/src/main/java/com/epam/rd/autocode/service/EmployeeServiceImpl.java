package com.epam.rd.autocode.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

public class EmployeeServiceImpl implements EmployeeService {
	ConnectionSource source;
	
	private static final String GET_IMPLOYEE_ID = "SELECT * FROM EMPLOYEE WHERE ID = ?";
	private static final String GET_DEPARTMENT_ID = "SELECT * FROM DEPARTMENT WHERE ID = ?";
	
	private static final String GET_ALL_SORT_DATE = "SELECT * FROM EMPLOYEE ORDER BY HIREDATE LIMIT ? OFFSET ?";	
	private static final String GET_ALL_SORT_LASTNAME = "SELECT * FROM EMPLOYEE ORDER BY LASTNAME LIMIT ? OFFSET ?";
	private static final String GET_ALL_SORT_SALARY = "SELECT * FROM EMPLOYEE ORDER BY SALARY LIMIT ? OFFSET ?";
	
	private static final String GET_ALL_SORT_DEPARTMENT_NAME_LASTNAME = "SELECT EMP.* "
			+ "FROM EMPLOYEE AS EMP "
			+ "LEFT JOIN DEPARTMENT AS DEP ON EMP.DEPARTMENT = DEP.ID "
			+ "ORDER BY DEP.NAME, EMP.LASTNAME "
			+ "LIMIT ? OFFSET ?";
	
	private static final String GET_DEPARTMENT_SORT_DATE = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = ? ORDER BY HIREDATE LIMIT ? OFFSET ?";
	private static final String GET_DEPARTMENT_SORT_SALARY = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = ? ORDER BY SALARY LIMIT ? OFFSET ?";
	private static final String GET_DEPARTMENT_SORT_LASTNAME = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = ? ORDER BY LASTNAME LIMIT ? OFFSET ?";
	
	private static final String GET_MANAGER_SORT_DATE = "SELECT * FROM EMPLOYEE WHERE MANAGER = ? ORDER BY HIREDATE LIMIT ? OFFSET ?";
	private static final String GET_MANAGER_SORT_SALARY = "SELECT * FROM EMPLOYEE WHERE MANAGER = ? ORDER BY SALARY LIMIT ? OFFSET ?";
	private static final String GET_MANAGER_SORT_LASTNAME = "SELECT * FROM EMPLOYEE WHERE MANAGER = ? ORDER BY LASTNAME LIMIT ? OFFSET ?";
	
	private static final String GET_DAPARTMENT_TOP_NTH_SALARY = "SELECT * FROM EMPLOYEE "
			+ "WHERE DEPARTMENT = ? ORDER BY SALARY DESC LIMIT 1 OFFSET ?";
	
	
	public EmployeeServiceImpl() {
		this.source = ConnectionSource.instance();
	}

	@Override
	public List<Employee> getAllSortByHireDate(Paging paging) {
		List<Employee> employees = new ArrayList<>();
		allSortEmployee(paging, employees, GET_ALL_SORT_DATE);
		return employees;
	}

	private void allSortEmployee(Paging paging, List<Employee> employees, String sql) {
		try (Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, paging.itemPerPage);
			statement.setInt(2, (paging.page-1) * paging.itemPerPage);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {				
				Employee employee = mapEmployee(rs);
				employees.add(employee);
			}
		} catch (SQLException e) {
			System.out.println("Failed to retrieve employees " + e.getMessage());
			throw new RuntimeException("Failed to retrieve employees", e);
		}
	}

	private Employee mapEmployee(ResultSet rs) throws SQLException {		
		BigDecimal managerId = rs.getBigDecimal("MANAGER");
		BigDecimal departmentId = rs.getBigDecimal("DEPARTMENT");		
		Employee manager = (managerId != null) ? getEmployeeById(managerId) : null;
		Department department = (departmentId != null) ? getDepartmentById(departmentId) : null;
				
		Employee employee = new Employee(
				rs.getBigDecimal("ID").toBigInteger(),
				new FullName(rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("MIDDLENAME")),
				Position.valueOf(rs.getString("POSITION")),
				rs.getDate("HIREDATE").toLocalDate(),
				rs.getBigDecimal("SALARY"),
				manager,	
				department
				);
		
		return employee;
	}
	
	private Employee getEmployeeById(BigDecimal idManager) {			
		try (Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(GET_IMPLOYEE_ID);
			statement.setBigDecimal(1, idManager);
			ResultSet rs = statement.executeQuery();
			Employee employee = null;
			if(rs.next()) {	
				BigDecimal departmentId = rs.getBigDecimal("DEPARTMENT");		
				employee = new Employee(
						rs.getBigDecimal("ID").toBigInteger(),
						new FullName(rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("MIDDLENAME")),
						Position.valueOf(rs.getString("POSITION")),
						rs.getDate("HIREDATE").toLocalDate(),
						rs.getBigDecimal("SALARY"),
						null,	
						(departmentId != null) ? getDepartmentById(departmentId) : null
						);						
			}
			return employee;
		} catch (SQLException e) {
			System.out.println("Failed to retrieve employee by ID" + e.getMessage());
			throw new RuntimeException("Failed to retrieve employee by ID", e);
		}
	}
	
	
	private Department getDepartmentById(BigDecimal departmentId) {		
		try (Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(GET_DEPARTMENT_ID);
			statement.setBigDecimal(1, departmentId);
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				return new Department(
						rs.getBigDecimal("ID").toBigInteger(), 
						rs.getString("NAME"), 
						rs.getString("LOCATION"));
			} else {
				System.out.println("Department not found with ID: " + departmentId);
                throw new RuntimeException("Department not found with ID: " + departmentId);
            }
			
		} catch (SQLException e) {
			System.out.println("Failed to retrieve department by ID " + e.getMessage());
			throw new RuntimeException("Failed to retrieve department by ID", e);
		}
		
	}


	@Override
	public List<Employee> getAllSortByLastname(Paging paging) {
		List<Employee> employees = new ArrayList<>();
		allSortEmployee(paging, employees, GET_ALL_SORT_LASTNAME);
		return employees;
	}

	@Override
	public List<Employee> getAllSortBySalary(Paging paging) {
		List<Employee> employees = new ArrayList<>();
		allSortEmployee(paging, employees, GET_ALL_SORT_SALARY);
		return employees;
	}

	@Override
	public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
		List<Employee> employees = new ArrayList<>();
		allSortEmployee(paging, employees, GET_ALL_SORT_DEPARTMENT_NAME_LASTNAME);
		return employees;
	}

	@Override
	public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
		List<Employee> employees = new ArrayList<>();
		departmentSortEmployee(department, paging, employees, GET_DEPARTMENT_SORT_DATE);
		return employees;
	}

	private void departmentSortEmployee(Department department, Paging paging, List<Employee> employees, String sql) {
		try (Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setBigDecimal(1, new BigDecimal(department.getId()));
			statement.setInt(2, paging.itemPerPage);
			statement.setInt(3, (paging.page-1) * paging.itemPerPage);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {				
				Employee employee = mapEmployee(rs);
				employees.add(employee);
			}
		} catch (SQLException e) {
			System.out.println("Failed to retrieve employees " + e.getMessage());
			throw new RuntimeException("Failed to retrieve employees", e);
		}
	}

	@Override
	public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
		List<Employee> employees = new ArrayList<>();
		departmentSortEmployee(department, paging, employees, GET_DEPARTMENT_SORT_SALARY);
		return employees;
	}

	@Override
	public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
		List<Employee> employees = new ArrayList<>();
		departmentSortEmployee(department, paging, employees, GET_DEPARTMENT_SORT_LASTNAME);
		return employees;
	}

	@Override
	public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
		List<Employee> employees = new ArrayList<>();
		managerSortEmployee(manager, paging, employees, GET_MANAGER_SORT_LASTNAME);
		return employees;
	}
	
	private void managerSortEmployee(Employee manager, Paging paging, List<Employee> employees, String sql) {
		try (Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setBigDecimal(1, new BigDecimal(manager.getId()));
			statement.setInt(2, paging.itemPerPage);
			statement.setInt(3, (paging.page-1) * paging.itemPerPage);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {				
				Employee employee = mapEmployee(rs);
				employees.add(employee);
			}
		} catch (SQLException e) {
			System.out.println("Failed to retrieve employees " + e.getMessage());
			throw new RuntimeException("Failed to retrieve employees", e);
		}
	}

	@Override
	public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
		List<Employee> employees = new ArrayList<>();
		managerSortEmployee(manager, paging, employees, GET_MANAGER_SORT_DATE);
		return employees;
	}

	@Override
	public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
		List<Employee> employees = new ArrayList<>();
		managerSortEmployee(manager, paging, employees, GET_MANAGER_SORT_SALARY);
		return employees;
	}

	@Override
	public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
		try (Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(GET_IMPLOYEE_ID);
			statement.setBigDecimal(1, new BigDecimal(employee.getId()));
			ResultSet rs = statement.executeQuery();
			Employee employeeResult = null;
			while (rs.next()) {
				BigDecimal managerId = rs.getBigDecimal("MANAGER");
				BigDecimal departmentId = rs.getBigDecimal("DEPARTMENT");		
				Employee manager = (managerId != null) ? getEmployeeByIdAllManager(managerId): null;
				Department department = (departmentId != null) ? getDepartmentById(departmentId) : null;
						
				employeeResult = new Employee(
						rs.getBigDecimal("ID").toBigInteger(),
						new FullName(rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("MIDDLENAME")),
						Position.valueOf(rs.getString("POSITION")),
						rs.getDate("HIREDATE").toLocalDate(),
						rs.getBigDecimal("SALARY"),
						manager,	
						department
						);	
			}			
			return employeeResult;
		} catch (SQLException e) {
			System.out.println("Failed to retrieve employees " + e.getMessage());
			throw new RuntimeException("Failed to retrieve employees", e);
		}
	}
	
	private Employee getEmployeeByIdAllManager(BigDecimal idManager) {			
		try (Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(GET_IMPLOYEE_ID);
			statement.setBigDecimal(1, idManager);
			ResultSet rs = statement.executeQuery();
			Employee employee = null;
			if(rs.next()) {	
				BigDecimal managerId = rs.getBigDecimal("MANAGER");
				BigDecimal departmentId = rs.getBigDecimal("DEPARTMENT");		
				employee = new Employee(
						rs.getBigDecimal("ID").toBigInteger(),
						new FullName(rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("MIDDLENAME")),
						Position.valueOf(rs.getString("POSITION")),
						rs.getDate("HIREDATE").toLocalDate(),
						rs.getBigDecimal("SALARY"),
						(managerId != null) ? getEmployeeByIdAllManager(managerId): null,	
						(departmentId != null) ? getDepartmentById(departmentId) : null
						);						
			}
			return employee;
		} catch (SQLException e) {
			System.out.println("Failed to retrieve employee by ID" + e.getMessage());
			throw new RuntimeException("Failed to retrieve employee by ID", e);
		}
	}

	@Override
	public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
		try (Connection connection = source.createConnection()) {
			PreparedStatement statement = connection.prepareStatement(GET_DAPARTMENT_TOP_NTH_SALARY);
			statement.setBigDecimal(1, new BigDecimal(department.getId()));
			statement.setInt(2, (salaryRank-1));// -1, так как нумерация начинается с 0
			ResultSet rs = statement.executeQuery();
			Employee employee = null;
			if(rs.next()) {
				employee = mapEmployee(rs);
			}
			return employee;
			
		} catch (SQLException e) {
			System.out.println("Failed to retrieve employee by ID" + e.getMessage());
			throw new RuntimeException("Failed to retrieve employee by ID", e);
		}		
	}
	
	
}
