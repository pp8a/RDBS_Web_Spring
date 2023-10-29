package com.epam.rd.tasks.sqlqueries;

/**
 * Implement sql queries like described
 */
public class SqlQueries {
    //Select all employees sorted by last name in ascending order
    //language=HSQLDB
    String select01 = "SELECT * FROM EMPLOYEE ORDER BY LASTNAME ASC";

    //Select employees having no more than 5 characters in last name sorted by last name in ascending order
    //language=HSQLDB
    String select02 = "SELECT * FROM EMPLOYEE WHERE LENGTH(LASTNAME) <= 5 ORDER BY LASTNAME ASC";

    //Select employees having salary no less than 2000 and no more than 3000
    //language=HSQLDB
    String select03 = "SELECT * FROM EMPLOYEE WHERE SALARY > 2000 AND SALARY <= 3000";

    //Select employees having salary no more than 2000 or no less than 3000
    //language=HSQLDB
    String select04 = "SELECT * FROM EMPLOYEE WHERE SALARY <= 2000 OR SALARY >= 3000";

    //Select all employees assigned to departments and corresponding department
    //language=HSQLDB
    String select05 = "SELECT * FROM EMPLOYEE AS EMPL JOIN DEPARTMENT AS DEPT ON DEPT.ID = EMPL.DEPARTMENT";

    //Select all employees and corresponding department name if there is one.
    //Name column containing name of the department "depname".
    //language=HSQLDB
    String select06 = "SELECT EMPL.*, DEPT.NAME AS depname FROM EMPLOYEE AS EMPL LEFT JOIN DEPARTMENT AS DEPT ON DEPT.ID = EMPL.DEPARTMENT";

    //Select total salary pf all employees. Name it "total".
    //language=HSQLDB
    String select07 = "SELECT SUM(SALARY) AS total FROM EMPLOYEE";

    //Select all departments and amount of employees assigned per department
    //Name column containing name of the department "depname".
    //Name column containing employee amount "staff_size".
    //language=HSQLDB
    String select08 = "SELECT DEPT.NAME AS depname, COUNT(EMPL.ID) AS staff_size "
    		+ "FROM DEPARTMENT AS DEPT "
    		+ "INNER JOIN EMPLOYEE AS EMPL ON EMPL.DEPARTMENT = DEPT.ID "
    		+ "GROUP BY DEPT.NAME";

    //Select all departments and values of total and average salary per department
    //Name column containing name of the department "depname".
    //language=HSQLDB
    String select09 = "SELECT DEPT.NAME AS depname, SUM(EMPL.SALARY) AS total, AVG(EMPL.SALARY) AS average "
    		+ "FROM DEPARTMENT AS DEPT "
    		+ "JOIN EMPLOYEE AS EMPL ON EMPL.DEPARTMENT = DEPT.ID "
    		+ "GROUP BY DEPT.NAME";

    //Select lastnames of all employees and lastnames of their managers if an employee has a manager.
    //Name column containing employee's lastname "employee".
    //Name column containing manager's lastname "manager".
    //language=HSQLDB
    String select10 = "SELECT EMPL1.LASTNAME AS employee, EMPL2.LASTNAME AS manager "
    		+ "FROM EMPLOYEE AS EMPL1 "
    		+ "LEFT JOIN EMPLOYEE AS EMPL2 ON EMPL2.ID = EMPL1.MANAGER";


}
