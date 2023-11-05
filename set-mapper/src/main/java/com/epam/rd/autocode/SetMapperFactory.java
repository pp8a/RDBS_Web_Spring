package com.epam.rd.autocode;


import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;


import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SetMapperFactory {
	public SetMapper<Set<Employee>> employeesSetMapper() {
		return resultSet -> {
	        try {
	            Set<Employee> employees = new HashSet<>();
	            while (resultSet.next()) {
	                employees.add(getEmployee(resultSet));
	            }
	            return employees;
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	    };
	}

	private Employee getEmployee(ResultSet resultSet) {
	    try {
	        return new Employee(new BigInteger(resultSet.getString("id")),
	            new FullName(resultSet.getString("firstName"),
	                resultSet.getString("lastName"),
	                resultSet.getString("middleName")),
	            Position.valueOf(resultSet.getString("position")),
	            resultSet.getDate("hireDate").toLocalDate(),
	            resultSet.getBigDecimal("salary"),
	            getEmployeeById(resultSet, resultSet.getInt("manager")));
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }

	}

	/** 
	 * @resultSet.getRow() Этот метод возвращает номер текущей строки, на которой находится курсор в результирующем наборе данных. 
	 * В этом контексте, он используется для сохранения текущей позиции курсора в переменной rowToReturn.
	 * @resultSet.beforeFirst(); Этот метод перемещает курсор перед первую строку результирующего набора данных. 
	 * Когда курсор находится перед первой строкой, вызов resultSet.next() переместит его на первую строку. 
	 * Это действие не возвращает данные, оно только готовит курсор к началу итерации по результирующему набору данных.
	 * @resultSet.absolute(rowToReturn): Этот метод перемещает курсор на указанную позицию, которая была сохранена в переменной rowToReturn. 
	 * Это позволяет вернуть курсор на ту строку, на которой он находился до начала поиска.
	 */
	private Employee getEmployeeById(ResultSet resultSet, int idManager) {
	    if (idManager == 0) {
	        return null;
	    }

	    try {
	        Employee employee = null;	        
	        int rowToReturn = resultSet.getRow();	        
	        resultSet.beforeFirst();
	        while (resultSet.next() && employee == null) {
	            if (resultSet.getInt("id") == idManager) {
	                employee = getEmployee(resultSet);
	            }
	        }
	        resultSet.absolute(rowToReturn);
	        return employee;
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}

}


/*
 * Employee{id=7844, fullName=FullName{firstName=JOHN, lastName=TURNER, middleName=MARIA}, position=SALESMAN, hired=1981-09-08, salary=1500.00000, 
manager=Employee{id=7698, fullName=FullName{firstName=JOHN, lastName=BLAKE, middleName=MARIA}, position=MANAGER, hired=1981-05-01, salary=2850.00000, manager=null}}, 
 */




